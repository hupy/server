package org.domeos.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.domeos.global.GlobalConstant;
import org.domeos.util.DatabaseType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by feiliu206363 on 2017/4/5.
 */
@Configuration
@MapperScan(basePackages = "org.domeos.framework.api.mapper", sqlSessionFactoryRef = "domeosSqlSessionFactory")
public class DomeosTestDataSourceConfig {
    @Bean
    @Primary
    public DataSource domeosDataSource() throws Exception {
        DruidDataSource basicDataSource = new DruidDataSource();
        basicDataSource.setUrl("jdbc:h2:mem:domeos;MODE=MYSQL;INIT=RUNSCRIPT FROM './src/test/resources/init_domeos.sql'");
        basicDataSource.setMaxActive(80);
        basicDataSource.setMaxWait(50);
        basicDataSource.setDriverClassName("org.h2.Driver");
        basicDataSource.setMinEvictableIdleTimeMillis(20000);
        basicDataSource.setTimeBetweenEvictionRunsMillis(20000);
        return basicDataSource;
    }

    @Bean(name = "domeosSqlSessionFactory")
    @Primary
    public SqlSessionFactoryBean sqlSessionFactory() throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(domeosDataSource());
        GlobalConstant.DATABASETYPE = DatabaseType.H2;
        return sessionFactory;
    }
}
