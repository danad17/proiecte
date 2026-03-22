package app.electronics.Review;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getAll")
    public List<Review> getAllReviews() {
        return reviewService.getReviews();
    }

    @GetMapping("/getReviewByItemId/{id}")
    public List<Review> getByItemId(@PathVariable Long id) {
        return reviewService.getReviewByItemId(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addReview(@RequestBody ReviewRequest request) {
        reviewService.addReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
