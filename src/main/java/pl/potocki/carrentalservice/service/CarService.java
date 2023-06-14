package pl.potocki.carrentalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.model.carApi.CarDataDto;
import pl.potocki.carrentalservice.model.carApi.CarMakeDto;

import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = objectMapper.readValue(new URL("https://carapi.app/api/makes"), CarDataDto.class);
        return carDataDto.getCarMakes();
    }
}
