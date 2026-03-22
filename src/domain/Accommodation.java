package domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Accommodation extends TravelComponent {
    private int starRating;
    private int durationNights;
    private RoomCategory category;

    public Accommodation() {}

    public Accommodation(Integer code, String title, BigDecimal baseCost, LocalDate startDate, LocalDate endDate,
                         int starRating, int durationNights, RoomCategory category) {
        super(code, title, baseCost, startDate, endDate);
        this.starRating = starRating;
        this.durationNights = durationNights;
        this.category = category;
    }

    @Override
    public BigDecimal computeCost(int guests) {
        BigDecimal base = getBaseCost().multiply(BigDecimal.valueOf(guests));

        double nightsFactor = switch (durationNights) {
            case 1 -> 1.15;
            case 2 -> 1.35;
            case 3 -> 1.65;
            default -> 1.9;
        };

        double starsFactor = 1.0 + (starRating * 0.08);

        return base.multiply(BigDecimal.valueOf(nightsFactor))
                .multiply(BigDecimal.valueOf(starsFactor));
    }

    public int getStarRating() { return starRating; }
    public void setStarRating(int rating) { this.starRating = rating; }
    public int getDurationNights() { return durationNights; }
    public void setDurationNights(int nights) { this.durationNights = nights; }
    public RoomCategory getCategory() { return category; }
    public void setCategory(RoomCategory category) { this.category = category; }
}