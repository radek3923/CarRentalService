package pl.potocki.carrentalservice.carRental.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import pl.potocki.carrentalservice.car.model.Car;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CarRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalId;

    @Cascade(CascadeType.ALL)
    @ManyToOne
    private Car car;

    private BigDecimal price;

    private LocalDate rentalDate;

    private LocalDate returnDate;
}
