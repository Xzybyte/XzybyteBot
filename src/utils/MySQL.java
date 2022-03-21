package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private String user;
    private String password;
    private String database;
    private String host;
    private String port;
    private Connection connection;

    public MySQL(String user, String password, String database, String host, String port) {
        this.user = user;
        this.password = password;
        this.database = database;
        this.host = host;
        this.port = port;
    }

    public void connectToDb() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

}
