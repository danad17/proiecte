package app.electronics.Favourites;

import lombok.Data;

import java.util.List;

@Data
public class FavouritesDTO {

    private int favouriteId;
    private int userId;
    private List<FavouriteItemsDTO> items;
    private int totalItemCount;

    FavouritesDTO(int favouriteId, int userId,List<FavouriteItemsDTO> items){
        this.favouriteId = favouriteId;
        this.userId = userId;
        this.items = items;
    }
}

@Data
class FavouriteItemsDTO {

    private Long favouriteItemsId;
    private Long itemId;
    private String name;
    private String description;
    private double price;
    private String image;
    private String brand;
    private String type;
    private String subtype;
    private String category;
    private int quantity;

}

@Data
class AddToFavourites {

    private Long itemId;

}