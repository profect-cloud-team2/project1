package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnectionChecker {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://172.16.24.70:2222/postgres";
        String username = "idong-gyu";
        String password = "qwer1234";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Connection OK");
        } catch (Exception e) {
            System.out.println("❌ Connection FAIL");
            e.printStackTrace();
        }
    }
}