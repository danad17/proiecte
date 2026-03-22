package app.electronics.Review;

import lombok.Data;

@Data
public class ReviewRequest {
    private double rating;
    private long itemId;
}

