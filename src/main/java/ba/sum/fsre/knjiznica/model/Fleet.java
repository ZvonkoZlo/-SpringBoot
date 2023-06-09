package ba.sum.fsre.knjiznica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fleet")
public class Fleet {
    public String getMarkaIBrend() {
        return this.brand + " " + this.model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 200, nullable = false)
    @NotBlank(message = "Molimo unesite marku vozila.")
    String brand;

    @Column(length = 200, nullable = false)
    @NotBlank(message = "Molimo unesite model vozila.")
    String model;

    @Column(nullable = false)
    @NotBlank(message = "Molimo postavite sliku")
    String image;

    @Column(length = 200, nullable = false)
    @NotBlank(message = "Molimo unesite opis vozila.")
    String description;

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    Float price;

    @OneToMany(mappedBy = "fleet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();
}
