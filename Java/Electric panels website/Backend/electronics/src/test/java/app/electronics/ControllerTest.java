package app.electronics;


import app.electronics.Authentication.RegisterDTO;
import app.electronics.Items.ItemRepository;
import app.electronics.Items.Items;
import app.electronics.JwtConfiguration.JwtService;
import app.electronics.Review.Review;
import app.electronics.Review.ReviewRepository;
import app.electronics.Review.ReviewRequest;
import app.electronics.Review.ReviewService;
import app.electronics.User.User;
import app.electronics.User.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ReviewRepository reviewRepository;

    @ParameterizedTest
    @CsvSource({
            "1",
            "2",
            "11"
    })
    void getItemResponse200(int id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/items/getById/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getUsersResponseShouldBeUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/getUsers"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsersResponseShouldBeAuthorized() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/getUsers"))
                .andExpect(status().isOk());
    }


    @Test
    void loginWithRightCredentialsShouldReturn200() throws Exception {
        User user = new User();
        user.setEmail("david_dana17@yahoo.com");
        user.setPassword("parolaAdmin1.");

        mockMvc.perform(post("/auth/login", user.getEmail(), user.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void loginWithWrongCredentialsShouldReturn400() throws Exception {
        User user = new User();
        user.setEmail("david_dana17@yahoo.com");
        user.setPassword("parola.");

        mockMvc.perform(post("/auth/login", user.getEmail(), user.getPassword())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void signupWithExistingEmailShouldReturn401() throws Exception {
        RegisterDTO registerUser = new RegisterDTO();
        registerUser.setEmail("david_dana17@yahoo.com");
        registerUser.setPassword("parola.");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUser)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void createReviewShouldPass() throws Exception {

        Items item = new Items();
        item.setName("Test item");
        item.setDescription("Descriere test");
        item.setPrice(100.0);
        item.setImage("image.png");
        item.setType("Electronics");
        item.setSubtype("Subtip");
        item.setCategory("Categorie");
        item.setStockAvailable(10L);
        item.setBrand("Brand");

        item = itemRepository.saveAndFlush(item);

        ReviewRequest request = new ReviewRequest();
        request.setRating(4.2);
        request.setItemId(item.getId());

        mockMvc.perform(
                        post("/reviews/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());

        List<Review> reviews = reviewRepository.findByItemId(item.getId());
        assertFalse(reviews.isEmpty());
        assertEquals(4.1, reviews.get(0).getRating());

    }

    @Test
    void deleteItemShouldReturn200() throws Exception {
        int id = 4;
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/delete/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getItemWithInvalidIdShouldReturn404() throws Exception {
        mockMvc.perform(get("/items/getById/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteNonExistingItemShouldReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/items/delete/{id}", 9999))
                .andExpect(status().isNotFound());
    }

}