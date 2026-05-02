package common;

import java.sql.*;


public class SimpleDB {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.postgresql.Driver");

                String url = "jdbc:postgresql://localhost:5432/travel_agency";
                String user = "postgres";
                String password = "2210";

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Подключено к базе данных!");
            } catch (Exception e) {
                System.out.println("Ошибка подключения: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение закрыто");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}