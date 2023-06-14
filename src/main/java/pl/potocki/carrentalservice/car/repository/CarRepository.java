package pl.potocki.carrentalservice.car.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.potocki.carrentalservice.car.model.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
