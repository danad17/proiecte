package app.electronics.Items;

import app.electronics.Review.Review;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String description;
    @Column(nullable = false)
    public double price;
    @Column(nullable = false)
    public String image;
    @Column(nullable = false)
    public String type;
    @Column(nullable = false)
    public String subtype;
    @Column(nullable = false)
    public String category;
    @Column(nullable = false)
    public Long stockAvailable;
    @Column(nullable = false)
    public String brand;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

}
