package common;

import domain.*;
import java.sql.*;
import java.util.*;

public class SimpleDatabaseHelper {
    public static void saveCustomer(Customer customer) {
        String sql = "INSERT INTO customers (id, name, email, phone, document_id, bonus_points) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO UPDATE SET bonus_points = ?";

        try (PreparedStatement stmt = SimpleDB.getConnection().prepareStatement(sql)) {
            stmt.setObject(1, customer.getCustomerId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getEmailAddress());
            stmt.setString(4, customer.getContactNumber());
            stmt.setString(5, customer.getDocumentId());
            stmt.setInt(6, customer.getBonusPoints());
            stmt.setInt(7, customer.getBonusPoints());
            stmt.executeUpdate();
            System.out.println("Клиент сохранен в БД");
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения клиента: " + e.getMessage());
        }
    }

    public static void saveReservation(Reservation reservation) {
        saveCustomer(reservation.getCustomer());

        String sql = "INSERT INTO reservations (ref, customer_id, date, status, total_cost) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = SimpleDB.getConnection().prepareStatement(sql)) {
            stmt.setString(1, reservation.getReservationRef());
            stmt.setObject(2, reservation.getCustomer().getCustomerId());
            stmt.setObject(3, reservation.getCreationDate());
            stmt.setString(4, reservation.getCurrentStatus().name());
            stmt.setBigDecimal(5, reservation.computeTotalCost());
            stmt.executeUpdate();
            System.out.println("Бронирование сохранено в БД");
        } catch (SQLException e) {
            System.out.println("Ошибка сохранения брони: " + e.getMessage());
        }
    }

    public static List<String> getAllReservations() {
        List<String> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Statement stmt = SimpleDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String info = String.format(
                        "Бронь %s от %s, статус: %s, сумма: %s",
                        rs.getString("ref"),
                        rs.getDate("date"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_cost")
                );
                reservations.add(info);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка загрузки: " + e.getMessage());
        }
        return reservations;
    }
}