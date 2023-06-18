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

    public Optional<CarRental> findCarRentalById(Long id){
        return carRentalRepository.findById(id);
    }

    public CarRental addCarRental(CarRental carRental){
        log.info("Adding car rental: {}", carRental);
        return carRentalRepository.save(carRental);
    }

    public List<CarRental> findAll() {
        return carRentalRepository.findAll();
    }
}
