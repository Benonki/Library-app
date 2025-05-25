package Server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:h2:file:./data/library;DB_CLOSE_ON_EXIT=FALSE;AUTO_SERVER=TRUE");
            config.setUsername("sa");
            config.setPassword(""); // domyślnie puste
            config.setDriverClassName("org.h2.Driver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setLeakDetectionThreshold(5000);

            dataSource = new HikariDataSource(config);
            System.out.println("H2 Database connection pool initialized");
        } catch (Exception e) {
            System.out.println("Error while initializing H2 database pool");
            e.printStackTrace();
            closePool();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("H2 Database connection pool closed");
        }
    }
}
