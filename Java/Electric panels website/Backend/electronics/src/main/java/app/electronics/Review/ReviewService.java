package app.electronics.Review;

import app.electronics.Items.ItemRepository;
import app.electronics.Items.Items;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final  ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;

    public ReviewService(ReviewRepository reviewRepository, ItemRepository itemRepository) {
        this.reviewRepository = reviewRepository;
        this.itemRepository = itemRepository;
    }

    public void addReview(ReviewRequest request) {
        Items item = itemRepository.findById((int) request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setItem(item);  // 👈 item MANAGED

        reviewRepository.save(review);
    }
    public List<Review> getReviews() {
        return reviewRepository.findAll();
    }

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> getReviewByItemId(Long item_id) {
        return reviewRepository.findByItemId(item_id);
    }
}
