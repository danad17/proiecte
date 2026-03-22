package app.electronics.Review;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

     Review getById(Integer id);

    List<Review> findByItemId(Long itemId);
    List<Review> findIdByItemName(String item_name);

}
