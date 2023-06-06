package pl.potocki.carrentalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.potocki.carrentalservice.model.CarRental;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Long> {
}
