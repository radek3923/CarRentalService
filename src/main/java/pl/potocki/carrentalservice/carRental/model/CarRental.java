package pl.potocki.carrentalservice.carRental.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CarRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "integer default 0")
    private int price;
}
