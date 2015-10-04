package ManagerApplications;


import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

public class MyDBConnection {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private Properties dbProp;
    public Connection myConnection;

    public MyDBConnection() {
        loadProperties();
    }

    public void init() {
        try {
            Class.forName(DRIVER);
            myConnection = DriverManager.getConnection(dbProp.getProperty("url"), dbProp.getProperty("username"), dbProp.getProperty("password"));
        } catch (Exception e) {
            System.out.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    public Connection getMyConnection() {
        return myConnection;
    }

    public void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void destroy() {
        if (myConnection != null) {
            try {
                myConnection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadProperties() {
        FileInputStream is = null;
        try {
            is = new FileInputStream("src//resources//db.properties");
            dbProp = new Properties();
            dbProp.load(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getQuery(String name) {
        return name;
    }
}
