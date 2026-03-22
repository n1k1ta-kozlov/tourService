package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GuidedTour extends TravelComponent {
    private String destination;
    private int tourDay;

    public GuidedTour() {}

    public GuidedTour(Integer code, String title, BigDecimal baseCost, LocalDate startDate, LocalDate endDate,
                      String destination, int tourDay) {
        super(code, title, baseCost, startDate, endDate);
        this.destination = destination;
        this.tourDay = tourDay;
    }

    @Override
    public BigDecimal computeCost(int participants) {
        BigDecimal total = getBaseCost().multiply(BigDecimal.valueOf(participants));
        if (participants > 8) {
            total = total.multiply(BigDecimal.valueOf(0.92));
        }
        return total;
    }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public int getTourDay() { return tourDay; }
    public void setTourDay(int day) { this.tourDay = day; }
}