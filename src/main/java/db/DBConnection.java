package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static DBConnection dbConnection;

    private Connection connection;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thogakade", "root", "dindragon8");
    }

    public static DBConnection getInstance() throws ClassNotFoundException, SQLException {
        return dbConnection!=null? dbConnection:(dbConnection = new DBConnection());
    }

    public Connection getConnection(){
        return connection;
    }

}
