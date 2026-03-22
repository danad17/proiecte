package app.electronics.Favourites;


import app.electronics.Cart.Cart;
import app.electronics.Cart.CartItemRepository;
import app.electronics.Items.ItemRepository;
import app.electronics.Items.Items;
import app.electronics.User.User;
import app.electronics.User.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavouriteService {

    private FavouriteRepository favouriteRepository;
    private CartItemRepository cartItemRepository;
    private UserRepository userRepository;
    private ItemRepository itemRepository;
    private FavouriteItemsRepository favouriteItemsRepository;


    public FavouritesDTO getOrCreateFavourite(int userId) {
        Favourite favourite = favouriteRepository.findByUserId(userId).orElseGet(()->createFavouriteForUser(userId));
        return toDto(favourite);
    }

    public FavouritesDTO addFavourite(int userId,int itemId) {

        Favourite favourite = favouriteRepository.findByUserId(userId)
                .orElseGet(() -> createFavouriteForUser(userId));


        Items item = itemRepository.findById((int) (long) itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Item-ul nu există"));

        favouriteItemsRepository.findByFavouriteIdAndItemId(favourite.getId(), (long) itemId).ifPresentOrElse(

        );
    }

    public FavouritesDTO deleteFavourite(int userId,int itemId) {

    }

    public FavouritesDTO emptyFavourite(int userId) {}


    private Favourite createFavouriteForUser(int userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User-ul nu există"));

        Favourite favourite = new Favourite();
        favourite.setUser(user);
        return favouriteRepository.save(favourite);
    }


    private FavouritesDTO toDto(Favourite favourite) {
        List<FavouriteItemsDTO> itemsDto = favourite.getFavouriteItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new FavouritesDTO(
                favourite.getId(),
                favourite.getUser().getId(),
                itemsDto
        );
    }

    private FavouriteItemsDTO toItemDTO(FavouriteItems favourite) {
        Items item = favourite.getItem();
        FavouriteItemsDTO dto = new FavouriteItemsDTO();
        dto.setFavouriteItemsId(favourite.getId());
        dto.setItemId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setPrice(item.getPrice());
        dto.setImage(item.getImage());
        dto.setBrand(item.getBrand());
        dto.setType(item.getType());
        dto.setSubtype(item.getSubtype());
        dto.setCategory(item.getCategory());
        return dto;
    }

}
