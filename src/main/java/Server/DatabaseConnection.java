package Server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

    private static HikariDataSource dataSource;

    static{
        try{
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:oracle:thin:@//localhost:1521/xe");
            config.setUsername("C##Admin");
            config.setPassword("qwer");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setLeakDetectionThreshold(5000);
            dataSource = new HikariDataSource(config);
            System.out.println("Database connection Pool initialized");
        }catch (Exception e){
            System.out.println("Error while initializing database pool");
            e.printStackTrace();
            closePool();
        }
    }

    public static Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    public static void closePool(){
        if(dataSource != null){
            dataSource.close();
            System.out.println("Database connection pool closed");
        }
    }
}
