package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class TravelComponent {
    private Integer code;
    private String title;
    private BigDecimal baseCost;
    private LocalDate startDate;
    private LocalDate endDate;

    public TravelComponent() {}

    public TravelComponent(Integer code, String title, BigDecimal baseCost, LocalDate startDate, LocalDate endDate) {
        this.code = code;
        this.title = title;
        this.baseCost = baseCost;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public abstract BigDecimal computeCost(int participants);

    public boolean isValidOn(LocalDate date) {
        return (date.isEqual(startDate) || date.isAfter(startDate)) &&
                (date.isEqual(endDate) || date.isBefore(endDate));
    }

    // Getters and setters
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getBaseCost() { return baseCost; }
    public void setBaseCost(BigDecimal cost) { this.baseCost = cost; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate date) { this.startDate = date; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate date) { this.endDate = date; }

    @Override
    public String toString() {
        return String.format("TravelComponent[code=%d, title=%s, cost=%.2f, period=%s to %s]",
                code, title, baseCost, startDate, endDate);
    }
}