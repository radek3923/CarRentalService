package pl.potocki.carrentalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.repository.CarRentalRepository;
import pl.potocki.carrentalservice.model.carApi.CarDataDto;
import pl.potocki.carrentalservice.model.carApi.CarMakeDto;

import java.net.URL;
import java.util.List;

@Service
public class CarRentalService {
    private final CarRentalRepository carRentalRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public CarRentalService(CarRentalRepository carRentalRepository, ObjectMapper objectMapper) {
        this.carRentalRepository = carRentalRepository;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = objectMapper.readValue(new URL("https://carapi.app/api/makes"), CarDataDto.class);
        return carDataDto.getCarMakes();
    }
}
