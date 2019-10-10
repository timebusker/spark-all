package com.timebusker.stream.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @DESC:MysqlDataSource
 * @author:timebusker
 * @date:2019/9/23
 */
public class MySQLExecuteUtil {

    private static volatile HikariDataSource dataSource;

    private static final HikariConfig config = new HikariConfig();

    private static final String SQL = "INSERT INTO TB_SPARK_STREAM_COUNT (WORD,CNT) VALUE (?,?)";

    public static Connection getConnection() throws Exception {
        return getDataSource().getConnection();
    }

    public static void execute(String word, int cnt) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(SQL);
        statement.setString(1, word);
        statement.setInt(2, cnt);
        statement.executeUpdate();
    }

    public static HikariDataSource getDataSource() {
        if (dataSource == null || dataSource.isClosed()) {
            synchronized (MySQLExecuteUtil.class) {
                config.setDriverClassName("com.mysql.jdbc.Driver");
                config.setJdbcUrl("jdbc:mysql://localhost:3306/kettle-test?useSSL=false");
                config.setUsername("root");
                config.setPassword("timebusker");
                config.setAutoCommit(true);
                config.setConnectionTimeout(120000);
                config.setIdleTimeout(60000);
                config.setMinimumIdle(1);
                config.setMaximumPoolSize(30);
                config.setMaxLifetime(120000);
                config.setInitializationFailTimeout(12000);
                dataSource = new HikariDataSource(config);
            }
        }
        return dataSource;
    }
}
