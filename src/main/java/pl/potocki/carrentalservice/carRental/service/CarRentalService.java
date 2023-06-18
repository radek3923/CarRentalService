package pl.potocki.carrentalservice.carRental.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.carRental.model.CarRental;
import pl.potocki.carrentalservice.carRental.repository.CarRentalRepository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class CarRentalService {

    private final CarRentalRepository carRentalRepository;

    public Optional<CarRental> findCarRentalById(Long id) {
        return carRentalRepository.findById(id);
    }

    public String addCarRental(CarRental carRental) {
        if (carRentalRepository.findAll()
                .stream()
                .map(CarRental::getCar)
                .noneMatch(e -> e.equals(carRental.getCar()))) {
            carRentalRepository.save(carRental);
            return "You have successfully rented" + carRental.getCar().getCarMake()
                    + ", " + carRental.getCar().getCarModel() + ", "
                    + carRental.getCar().getDescription();
        }
        return "This car is already rented";
    }

    public List<CarRental> findAll() {
        return carRentalRepository.findAll();
    }
}
