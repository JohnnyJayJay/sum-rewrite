package bettersum.sql;

import java.sql.*;

public class Database {

    private Connection connection;

    private String host, port, database, username, password;

    public void loadDriver(String driver) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Could not find driver");
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", host, port, database),
                    username, password);
        } catch (SQLException e) {
            System.err.println("Could not connect to database");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                System.err.println("Could not close connection");
                e.printStackTrace();
            }
        }
    }

    public boolean update(String sql, Object... parameters) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            for (int i = 1; i <= parameters.length; i++) {
                statement.setObject(i, parameters[i - 1]);
            }
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Could not execute update");
            e.printStackTrace();
        }
        return false;
    }

    public DatabaseResult query(String sql, Object... parameters) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql,
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            for (int i = 1; i <= parameters.length; i++) {
                statement.setObject(i, parameters[i - 1]);
            }
            ResultSet resultSet = statement.executeQuery();
            return new DatabaseResult(resultSet);
        } catch (SQLException e) {
            System.err.println("Could not execute query");
            e.printStackTrace();
        }
        return null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
