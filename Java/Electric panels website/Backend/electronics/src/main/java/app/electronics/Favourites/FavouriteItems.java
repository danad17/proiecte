package app.electronics.Favourites;

import app.electronics.Items.Items;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class FavouriteItems {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favourite_id", nullable = false)
    private Favourite favourite;

    // Referință la entitatea ta existentă Items
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    private Items item;

}
