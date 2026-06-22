package com.example.forum.test.mysql1;

import java.sql.*;

/**
 * 微信搜索「System」，回复 Java
 *
 * @author dev
 * @date 1/26/24
 */
class DatabaseCreator {
    private static final String URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = System.getenv().getOrDefault("MYSQL_PASSWORD", "");
    private static final String DATABASE_NAME = "tech_forum";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            if (!databaseExists(conn, DATABASE_NAME)) {
                stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
                System.out.println("数据库创建成功");
            } else {
                System.out.println("数据库已经存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean databaseExists(Connection conn, String dbName) throws SQLException {
        ResultSet resultSet = conn.getMetaData().getCatalogs();

        while (resultSet.next()) {
            if (dbName.equals(resultSet.getString(1))) {
                return true;
            }
        }

        return false;
    }
}


