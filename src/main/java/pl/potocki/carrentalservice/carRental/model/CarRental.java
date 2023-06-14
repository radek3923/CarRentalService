package pl.potocki.carrentalservice.carRental.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.potocki.carrentalservice.car.model.Car;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CarRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rental_id;

    @ManyToOne
    private Car car;

    private int price;

    private LocalDateTime rentalDate;

    private LocalDateTime returnDate;
}
