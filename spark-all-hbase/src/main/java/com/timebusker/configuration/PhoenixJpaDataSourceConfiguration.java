package com.timebusker.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * @Description:PhoenixJpaDataSourceConfiguration
 * @Author:Administrator
 * @Date2019/11/15 12:54
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.phoenix")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "phoenixJPAEntityManagerFactory",
        transactionManagerRef = "phoenixJPATransactionManager",
        basePackages = {"com.timebusker.phoenix.repository"}
)
public class PhoenixJpaDataSourceConfiguration {

    private String jdbcUrl;
    private String driverClassName;
    private String maxPoolSize;
    private String minIdle;
    private String validationTimeout;
    private String idleTimeout;
    private String connectionTestQuery;

    @Bean(name = "phoenixJPADataSource")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(Integer.parseInt(maxPoolSize));
        config.setMinimumIdle(Integer.parseInt(minIdle));
        config.setIdleTimeout(Long.parseLong(idleTimeout));
        config.setConnectionTestQuery(connectionTestQuery);
        HikariDataSource dataSource = new HikariDataSource(config);
        return dataSource;
    }

    @Bean(name = "phoenixJPAEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("phoenixJPADataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = builder.dataSource(dataSource)
                .packages("com.timebusker.phoenix.entity")
                .persistenceUnit("phoenix-jpa")
                .build();
        Properties properties = new Properties();
        // 关闭启动检测，避免语法不一致导致SQL执行失败
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        // 设置方言实现类
        properties.setProperty("hibernate.dialect", "com.ruesga.phoenix.dialect.PhoenixDialect");
        // 设置实体分段策略
        properties.setProperty("hibernate.physical_naming_strategy", "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl");
        // 设置拦截器
        properties.setProperty("hibernate.session_factory.interceptor", "com.timebusker.interceptor.PhoenixInterceptor");
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name = "phoenixJPATransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("phoenixJPAEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(String maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public String getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public String getValidationTimeout() {
        return validationTimeout;
    }

    public void setValidationTimeout(String validationTimeout) {
        this.validationTimeout = validationTimeout;
    }

    public String getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(String idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }
}
