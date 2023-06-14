package pl.potocki.carrentalservice.carRental.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.carRental.model.CarRental;
import pl.potocki.carrentalservice.carRental.repository.CarRentalRepository;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CarRentalService {

    private final CarRentalRepository carRentalRepository;

    public Optional<CarRental> findCarRentalById(Long id){
        return carRentalRepository.findById(id);
    }
}
