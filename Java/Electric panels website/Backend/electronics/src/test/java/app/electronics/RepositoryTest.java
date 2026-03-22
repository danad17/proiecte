package app.electronics;


import app.electronics.Items.ItemRepository;
import app.electronics.Items.Items;
import app.electronics.Review.Review;
import app.electronics.Review.ReviewRepository;
import app.electronics.User.User;
import app.electronics.User.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional

public class RepositoryTest {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewRepository reviewRepository;


    @ParameterizedTest
    @CsvSource({
            "1,Tablou comanda cu convertizor de frecventa motor 1.5 kW",
            "2,Tablou comanda cu convertizor de frecventa motor 1.5 kW",
            "2,Automatizare pompa submersibila monofazata 0.55 kW"
    })
    public void testIdMatchingNameParametrized(int id, String name) {

        Items item = itemRepository.findById(id).orElseThrow(() -> new AssertionError("Item-ul nu există"));

        assertEquals(name, item.getName());
    }

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "10"
    })
    public void productIsInStock(int id) {

        StockChecker stockChecker = new StockChecker();

        Items item = itemRepository.findById(id).orElseThrow(() -> new AssertionError("Item-ul nu există"));

        String result =  stockChecker.checkStock(item.getStockAvailable());

        assertEquals("Stock low", result);

    }


    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "4"
    })
    public void itHasBrand(int id) {
        BrandChecker brandChecker = new BrandChecker();

        Items item = itemRepository.findById(id).orElseThrow(() -> new AssertionError("Item-ul nu există"));

        String result =  brandChecker.checkBrand(item.getBrand());

        assertEquals("Nu", result);
    }

    @ParameterizedTest
    @CsvSource({
            "Tablouri automatizare pompe",
            "Tablouri pornire motoare monofazate",
            "Tablouri automatizare cu convertizor de frecventa",
            "Tablouri compensare energie reactiva",
            "Tablouri automatizare si control temperatura",
            "Tablouri cu automatizari diverse"
    })
    public void checkIfTypeHasProducts(String type){

        int count = 0;
        List<Items> itemsList = itemRepository.findAll();

        for(Items item : itemsList){
            if(item.getType().equals(type)) {
                if(item.getSubtype()!=null)
                    count++;
            }
        }
        boolean ok = count > 0;
        assertEquals(true, ok);
    }

    @Test
    void returnedUserShouldBeTrueIfEnabled() {
        String email = "david_dana17@yahoo.com";

        User user = userService.getUserByEmail(email);

        boolean value = user.isEnabled();
        assertEquals(true, value);
    }

    @ParameterizedTest
    @CsvSource({
            "Tablou comanda cu convertizor de frecventa motor 1.5 kW",
            "Automatizare pompa submersibila monofazata 0.55 kW"
    })
    void averageRatingOfItemShouldBeGreaterThan2_5(String name) {
        List<Review> reviews = reviewRepository.findIdByItemName(name);

        double average = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        System.out.println("Media: " + average);

        assertTrue(average > 2.5);
    }
}
