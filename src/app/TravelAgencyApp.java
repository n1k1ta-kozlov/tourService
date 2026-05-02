package app;

import common.BusinessException;
import common.SimpleDB;
import common.SimpleDatabaseHelper;
import domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class TravelAgencyApp {
    public static void main(String[] args) {
        try {
            SimpleDB.getConnection();

            Customer customer = new Customer(
                    "Анна Ковальски",
                    "anna.kowalski@travel.com",
                    "+48501234567",
                    "AB9876543",
                    85
            );

            Accommodation hotel = new Accommodation(
                    101,
                    "Гранд Плаза Отель",
                    new BigDecimal("185.00"),
                    LocalDate.now(),
                    LocalDate.now().plusDays(4),
                    4,
                    3,
                    RoomCategory.STANDARD
            );

            GuidedTour tour = new GuidedTour(
                    202,
                    "Обзорная экскурсия по городу",
                    new BigDecimal("65.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Старый город",
                    1
            );

            AirTravel flight = new AirTravel(
                    303,
                    "Международный рейс",
                    new BigDecimal("420.00"),
                    LocalDate.now(),
                    LocalDate.now(),
                    "Варшава",
                    "Рим",
                    "LO123",
                    true
            );

            Map<TravelComponent, Integer> travelPlan = new HashMap<>();
            travelPlan.put(hotel, 2);
            travelPlan.put(tour, 3);
            travelPlan.put(flight, 2);

            Reservation reservation = new Reservation(customer, travelPlan);

            SimpleDatabaseHelper.saveReservation(reservation);

            System.out.println("=== Бронирование создано ===");
            System.out.println(reservation);
            System.out.println("\nИтоговая стоимость: " + reservation.computeTotalCost() + " USD");

            reservation.confirmReservation();
            System.out.println("\nСтатус после подтверждения: " + reservation.getCurrentStatus());

            reservation.finishReservation();
            System.out.println("Статус после завершения: " + reservation.getCurrentStatus());
            System.out.println("Бонусные баллы клиента: " + customer.getBonusPoints());

            System.out.println("\n=== Все бронирования в БД ===");
            for (String res : SimpleDatabaseHelper.getAllReservations()) {
                System.out.println(res);
            }

            SimpleDB.closeConnection();

        } catch (BusinessException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }
}