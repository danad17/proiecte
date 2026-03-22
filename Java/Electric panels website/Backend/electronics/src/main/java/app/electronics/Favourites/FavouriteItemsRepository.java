package app.electronics.Favourites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface FavouriteItemsRepository extends JpaRepository<FavouriteItems,Integer> {


    Optional<FavouriteItems> findByFavouriteIdAndItemId(int favouriteId, Long itemId);
}
