package org.domeos.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import junit.framework.TestCase;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.util.ThreadState;
import org.domeos.basemodel.HttpResponseTemp;
import org.domeos.configuration.DomeosTestDataSourceConfig;
import org.domeos.configuration.H2ServerConfig;
import org.domeos.configuration.TestShiroConfig;
import org.domeos.configuration.WebSocketConfig;
import org.domeos.framework.api.consolemodel.auth.UserPassword;
import org.domeos.framework.api.model.auth.related.LoginType;
import org.domeos.framework.api.service.auth.UserService;
import org.domeos.framework.engine.model.CustomObjectMapper;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;

/**
 * Created by feiliu206363 on 2015/11/13.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration("classpath:application.properties")
@ComponentScan({"org.domeos.framework.api", "org.domeos.framework.engine", "org.domeos.global"})
@SpringBootTest(classes = {
        DomeosTestDataSourceConfig.class,
        H2ServerConfig.class,
        TestShiroConfig.class,
        WebSocketConfig.class})
public class BaseTestCase extends TestCase {
    public MockMvc mockMvc;
    protected static ThreadState subjectThreadState;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public WebApplicationContext wac;

    @Autowired
    public CustomObjectMapper objectMapper;

    @Autowired
    protected org.apache.shiro.mgt.SecurityManager securityManager;

    @Autowired
    protected UserService userService;

    protected void setSubject(Subject subject) {
        clearSubject();
        subjectThreadState = createThreadState(subject);
        subjectThreadState.bind();
    }

    protected Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    protected ThreadState createThreadState(Subject subject) {
        return new SubjectThreadState(subject);
    }

    /**
     * Clears Shiro's thread state, ensuring the thread remains clean for future test execution.
     */
    protected void clearSubject() {
        doClearSubject();
    }

    private static void doClearSubject() {
        if (subjectThreadState != null) {
            subjectThreadState.clear();
            subjectThreadState = null;
        }
    }

    protected static void setSecurityManager(SecurityManager securityManager) {
        SecurityUtils.setSecurityManager(securityManager);
    }

    protected static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    @AfterClass
    public static void tearDownShiro() {
        doClearSubject();
        try {
            SecurityManager securityManager = getSecurityManager();
            LifecycleUtils.destroy(securityManager);
        } catch (UnavailableSecurityManagerException e) {
            //we don't care about this when cleaning up the test environment
            //(for example, maybe the subclass is a unit test and it didn't
            // need a SecurityManager instance because it was using only
            // mock Subject instances)
        }
        setSecurityManager(null);
    }

    // helper method
    protected String readInJsonFromFile(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            byte[] buff = new byte[fileInputStream.available()];
            fileInputStream.read(buff);
            return new String(buff);
        } catch (Exception e) {
            return null;
        }
    }

    protected void displayInfo(HttpResponseTemp<?> response) {
        String body = null;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e1) {
            body = "json translate error when handle exception:" + response.getResultMsg();
        }
        System.out.println(body);
    }

    protected void login(String username, String password) {
        UserPassword userPassword = new UserPassword(username, password);
        userPassword.setLoginType(LoginType.USER);
        HttpResponseTemp<?> res = userService.normalLogin(userPassword);
        displayInfo(res);
    }
}
