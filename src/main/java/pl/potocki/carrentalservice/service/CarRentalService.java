package pl.potocki.carrentalservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.model.CarRental;
import pl.potocki.carrentalservice.repository.CarRentalRepository;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CarRentalService {

    private final CarRentalRepository carRentalRepository;

    public Optional<CarRental> findCarRentalById(Long id){
        return carRentalRepository.findById(id);
    }
}
