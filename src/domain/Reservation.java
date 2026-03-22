package domain;

import common.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Reservation {
    private final String reservationRef;
    private final Customer customer;
    private final Map<TravelComponent, Integer> componentParticipants;
    private final LocalDate creationDate;
    private ReservationStatus currentStatus;

    public Reservation(Customer customer, Map<TravelComponent, Integer> componentParticipants) {
        validateCustomer(customer);
        validateComponents(componentParticipants);

        this.customer = customer;
        this.componentParticipants = new HashMap<>(componentParticipants);
        this.reservationRef = generateReference();
        this.creationDate = LocalDate.now();
        this.currentStatus = ReservationStatus.PENDING;
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new BusinessException("Требуется информация о клиенте");
        }
    }

    private void validateComponents(Map<TravelComponent, Integer> components) {
        if (components == null || components.isEmpty()) {
            throw new BusinessException("Требуется как минимум один компонент поездки.");
        }

        for (Map.Entry<TravelComponent, Integer> entry : components.entrySet()) {
            TravelComponent comp = entry.getKey();
            Integer count = entry.getValue();

            if (comp == null) {
                throw new BusinessException("Компонент Travel не может быть пустым.");
            }
            if (count == null || count <= 0) {
                throw new BusinessException("Количество участников должно быть положительным.");
            }
            if (!comp.isValidOn(LocalDate.now())) {
                throw new BusinessException("Компонент '" + comp.getTitle() + "' не доступен");
            }

            if (comp instanceof Accommodation acc) {
                int maxGuests = switch (acc.getCategory()) {
                    case ECONOMY -> 2;
                    case STANDARD -> 3;
                    case DELUXE -> 5;
                };
                if (count > maxGuests) {
                    throw new BusinessException("Слишком много гостей для " + acc.getCategory() + " комнат");
                }
            }
        }
    }

    private String generateReference() {
        long timestamp = System.currentTimeMillis();
        int random = new Random().nextInt(9000) + 1000;
        return "REF" + timestamp + random;
    }

    public String getReservationRef() { return reservationRef; }
    public Customer getCustomer() { return customer; }
    public Map<TravelComponent, Integer> getComponentParticipants() { return new HashMap<>(componentParticipants); }
    public LocalDate getCreationDate() { return creationDate; }
    public ReservationStatus getCurrentStatus() { return currentStatus; }

    public BigDecimal computeTotalCost() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Map.Entry<TravelComponent, Integer> entry : componentParticipants.entrySet()) {
            sum = sum.add(entry.getKey().computeCost(entry.getValue()));
        }
        return sum.subtract(sum.multiply(BigDecimal.valueOf(customer.getDiscountRate())));
    }

    public void confirmReservation() {
        if (currentStatus != ReservationStatus.PENDING) {
            throw new IllegalStateException("Подтверждением подлежат только ожидающие подтверждения бронирования.");
        }
        currentStatus = ReservationStatus.CONFIRMED;
    }

    public void finishReservation() {
        if (currentStatus != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Завершить можно только подтвержденные бронирования.");
        }
        currentStatus = ReservationStatus.FINISHED;

        BigDecimal total = computeTotalCost();
        int pointsEarned = total.multiply(BigDecimal.valueOf(0.12)).intValue();
        customer.addBonusPoints(pointsEarned);
    }

    public void cancelReservation() {
        if (currentStatus == ReservationStatus.FINISHED || currentStatus == ReservationStatus.CANCELED) {
            throw new IllegalStateException("Отменить завершенное или уже отмененное бронирование невозможно.");
        }
        currentStatus = ReservationStatus.CANCELED;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return String.format("Бронирование[номер=%s, клиент=%s, услуг=%d, дата=%s, статус=%s]",
                reservationRef, customer.getName(), componentParticipants.size(),
                creationDate.format(formatter), currentStatus);
    }
}