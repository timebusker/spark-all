package com.timebusker.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Description:PhoenixDataSourceConfiguration
 * @Author:Administrator
 * @Date2019/11/13 22:09
 **/
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.phoenix")
@MapperScan(basePackages = "com.timebusker.phoenix.mapper.**", sqlSessionFactoryRef = "phoenixSqlSessionFactory")
public class PhoenixMyBatisDataSourceConfiguration {

    private String jdbcUrl;
    private String driverClassName;
    private String maxPoolSize;
    private String minIdle;
    private String validationTimeout;
    private String idleTimeout;
    private String connectionTestQuery;

    @Bean(name = "phoenixMyBatisDataSource")
    public DataSource dataSource() {
        Properties properties = new Properties();
        properties.put("jdbcUrl", jdbcUrl);
        properties.put("driverClassName", driverClassName);
        properties.put("maxPoolSize", maxPoolSize);
        properties.put("minIdle", minIdle);
        properties.put("validationTimeout", validationTimeout);
        properties.put("idleTimeout", idleTimeout);
        properties.put("connectionTestQuery", connectionTestQuery);
        HikariDataSourceFactory factory = new HikariDataSourceFactory();
        factory.setProperties(properties);
        return factory.getDataSource();
    }

    @Bean(name = "phoenixSqlSessionFactory")
    public SqlSessionFactory phoenixSqlSessionFactory(@Qualifier("phoenixMyBatisDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        ResourceLoader loader = new DefaultResourceLoader();
        String resource = "classpath:mybatis/mybatis-config.xml";
        factoryBean.setConfigLocation(loader.getResource(resource));
        factoryBean.setSqlSessionFactoryBuilder(new SqlSessionFactoryBuilder());
        return factoryBean.getObject();
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

class HikariDataSourceFactory extends UnpooledDataSourceFactory {
    // PooledDataSource 实例设置数据库配置项，此处使用池化数据源配置链接，对应的还有一个UnpooledDataSource
    // UnpooledDataSource 意味每次访问数据都会进行链接创建与释放
    public HikariDataSourceFactory() {
        this.dataSource = new HikariDataSource();
    }
}
