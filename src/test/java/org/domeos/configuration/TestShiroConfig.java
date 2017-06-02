package org.domeos.configuration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.domeos.framework.shiro.authenticator.MultiLoginAuthenticator;
import org.domeos.framework.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import org.domeos.framework.shiro.filter.DmoShiroFilterFactoryBean;
import org.domeos.framework.shiro.filter.FrontFilter;
import org.domeos.framework.shiro.filter.ShiroRedirectFilter;
import org.domeos.framework.shiro.realm.JdbcRealm;
import org.domeos.framework.shiro.realm.LdapRealm;
import org.domeos.framework.shiro.realm.NewLdapContextFactory;
import org.domeos.global.GlobalConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by feiliu206363 on 2017/4/5.
 */
@Configuration
public class TestShiroConfig {
    @Autowired(required = false)
    Collection<SessionListener> sessionListeners;

    /**
     * FilterRegistrationBean
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);

        return filterRegistration;
    }

    /**
     * @return
     * @see org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean(name = "shiroFilter")
    public DmoShiroFilterFactoryBean shiroFilter() {
        DmoShiroFilterFactoryBean bean = new DmoShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/login/login.html");
        bean.setSuccessUrl("/");

        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl("/login/login.html");
        Map<String, Filter> filters = Maps.newLinkedHashMap();
        filters.put("front", new FrontFilter());
        filters.put("backend", new BackendFilter());
        filters.put("logout", logoutFilter);
        filters.put("anon", new AnonymousFilter());
        filters.put("user", new UserFilter());
        filters.put("redirect", new ShiroRedirectFilter());
        bean.setFilters(filters);

        Map<String, String> chains = Maps.newLinkedHashMap();
        chains.put("/api/user/logout", "logout,backend");
        chains.put("/health", "anon,backend");
        chains.put("/api/ci/build/autobuild", "anon,backend");
        chains.put("/api/ci/build/builddockerfile/**", "anon,backend");
        chains.put("/api/ci/build/compilefile/**", "anon,backend");
        chains.put("/api/ci/build/status", "anon,backend");
        chains.put("/api/ci/build/upload/**", "anon,backend");
        chains.put("/api/ci/build/download/**", "anon,backend");
        chains.put("/api/image/public/item/**", "anon,backend");
        chains.put("/api/agent/pod/**", "anon,backend");
        chains.put("/api/user/login", "anon,backend");
        chains.put("/api/global/registry/private/certification", "anon,backend");
        chains.put("/api/alarm/action/wrap/**", "anon,backend");
        chains.put("/api/alarm/group/users/**", "anon,backend");
        chains.put("/api/alarm/link/**", "anon,backend");
        chains.put("/api/alarm/send/**", "anon,backend");
        chains.put("/service/token/**", "anon,backend");
        chains.put("/api/deploy/updatejob", "anon,backend");
        chains.put("/api/k8sevent/report", "anon,backend");

        chains.put("/common/**", "anon,front");
        chains.put("/lib/**", "anon,front");
        chains.put("/index/**", "anon,front");
        chains.put("/login/**", "anon,front");
        chains.put("/index/**", "anon,front");
        chains.put("/maps/**", "anon,front");
        chains.put("/monitor/**", "anon,front");
        chains.put("/favicon.ico", "anon,front");
        chains.put("/api/**", "redirect,user,backend");
        chains.put("/console/**", "redirect,user,backend");
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }

    /**
     * @return
     * @see org.apache.shiro.mgt.SecurityManager
     */
    @Bean(name = "securityManager")
    @DependsOn(value = {"ldapContextFactory"})
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        List<Realm> realms = Lists.newArrayList();
        realms.add(jdbcRealm());
        realms.add(ldapRealm());
        manager.setRealms(realms);
        manager.setSessionManager(defaultWebSessionManager());
        manager.setAuthenticator(multiLoginAuthenticator(realms));
        return manager;
    }

    /**
     * @return
     * @see DefaultWebSessionManager
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(GlobalConstant.SHIRO_REDIS_SESSION_TIMEOUT);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setCacheManager(cacheManager());
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean(name = "sessionCookie")
    public SimpleCookie sessionCookie() {
        final SimpleCookie sessionCookie = new SimpleCookie("sessionCookie");
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(-1);
        return sessionCookie;
    }

    /**
     * @return
     * @see JdbcRealm --->AuthorizingRealm
     */
    @Bean
    @DependsOn(value = {"lifecycleBeanPostProcessor", "retryLimitHashedCredentialsMatcher"})
    public JdbcRealm jdbcRealm() {
        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setCredentialsMatcher(retryLimitHashedCredentialsMatcher());
        return jdbcRealm;
    }

    /**
     * @return
     * @see LdapRealm --->AuthorizingRealm
     */
    @Bean
    @DependsOn(value = {"lifecycleBeanPostProcessor", "ldapContextFactory"})
    public LdapRealm ldapRealm() {
        LdapRealm ldapRealm = new LdapRealm();
        ldapRealm.setContextFactory(ldapContextFactory());
        return ldapRealm;
    }

    /**
     * @return
     * @see NewLdapContextFactory --->LdapContextFactory
     */
    @Bean
    @DependsOn(value = {"lifecycleBeanPostProcessor"})
    public NewLdapContextFactory ldapContextFactory() {
        return new NewLdapContextFactory();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public MultiLoginAuthenticator multiLoginAuthenticator(Collection<Realm> realms) {
        MultiLoginAuthenticator multiLoginAuthenticator = new MultiLoginAuthenticator();
        multiLoginAuthenticator.setRealms(realms);
        return multiLoginAuthenticator;
    }

    @Bean
    public RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(2);
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        return credentialsMatcher;
    }

    @Bean
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");
        return cacheManager;
    }
}
