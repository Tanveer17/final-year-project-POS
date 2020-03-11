package com.tanveer.model.database;

import com.tanveer.security.Login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;
    private static Login login;
    private static String userName;
    private static String password;
    private static Database database = new Database();

    private static void setDatabase(){
        login = Login.getLogin();
        userName = login.getUserName();
        password = login.getPassword();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/attire_cloths", "admin", "admin");
            System.out.println("connection succeded");
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    static {
        setDatabase();
    }

    public static Connection getConnection(){
        return connection;

    }


}
