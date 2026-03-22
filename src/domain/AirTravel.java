package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AirTravel extends TravelComponent {
    private String departureCity;
    private String arrivalCity;
    private String flightCode;
    private boolean luggageIncluded;

    public AirTravel() {}

    public AirTravel(Integer code, String title, BigDecimal baseCost, LocalDate startDate, LocalDate endDate,
                     String departureCity, String arrivalCity, String flightCode, boolean luggageIncluded) {
        super(code, title, baseCost, startDate, endDate);
        this.departureCity = departureCity;
        this.arrivalCity = arrivalCity;
        this.flightCode = flightCode;
        this.luggageIncluded = luggageIncluded;
    }

    @Override
    public BigDecimal computeCost(int passengers) {
        BigDecimal total = getBaseCost().multiply(BigDecimal.valueOf(passengers));
        if (luggageIncluded) {
            total = total.multiply(BigDecimal.valueOf(1.25));
        }
        return total;
    }

    public String getDepartureCity() { return departureCity; }
    public void setDepartureCity(String city) { this.departureCity = city; }
    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String city) { this.arrivalCity = city; }
    public String getFlightCode() { return flightCode; }
    public void setFlightCode(String code) { this.flightCode = code; }
    public boolean isLuggageIncluded() { return luggageIncluded; }
    public void setLuggageIncluded(boolean included) { this.luggageIncluded = included; }
}