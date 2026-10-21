package com.smushed.market_data.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component 
public class DatabaseConnection {
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPass;
    @Value("${spring.datasource.url}")
    private String dbURL;
    private final int maxRetries = 3;
    private final int waitTime = 200;

    public Connection getConnection() throws SQLException {
        SQLException lastException = null;
        for (int attempts = 0; attempts < maxRetries; attempts++) {
            try {
                return DriverManager.getConnection(dbURL, dbUser, dbPass);
            } catch (SQLException ex) {
                lastException = ex;
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException timerException) {
                    Thread.currentThread().interrupt();
                    throw new SQLException("Interrupted while retrying DB connection",
                        timerException.getCause());
                }
            }
        }
        throw new SQLException("DB Connection cannot be established. Ex: " + lastException);
    }
}
