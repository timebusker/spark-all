package com.timebusker.stream.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * @DESC:MysqlDataSource
 * @author:timebusker
 * @date:2019/9/23
 */
public class MySQLExecuteUtil {

    private static HikariDataSource dataSource;

    public static HikariDataSource getDataSource() {
        HikariConfig config = new HikariConfig();
        config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306?useSSL=false");
        config.setUsername("root");
        config.setPassword("timebusker");
        dataSource = InitDataSource.instance(config);
        return dataSource;
    }

    private static class InitDataSource {

        private static HikariDataSource dataSource;

        public static HikariDataSource instance(HikariConfig config) {
            if (dataSource == null || dataSource.isClosed()) {
                config.setAutoCommit(true);
                config.setConnectionTimeout(30000);
                config.setIdleTimeout(3000);
                config.setMinimumIdle(1);
                config.setMaximumPoolSize(30);
                config.setMaxLifetime(30000);
                config.setInitializationFailTimeout(3000);
                config.setPoolName("test___" + Math.random());
                dataSource = new HikariDataSource(config);
            }
            return dataSource;
        }
    }
}
