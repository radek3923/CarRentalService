package pl.potocki.carrentalservice.car.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.car.model.dto.CarDataDto;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.model.dto.CarTrimDto;

import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String API_URL = "https://carapi.app";
    private final String YEAR = "2020";

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = objectMapper.readValue(new URL(API_URL + "/api/makes"), CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarMakeDto>>() {});
    }

    @SneakyThrows
    public List<CarModelDto> getAllCarModels(String carMake) {
        URL url = new URL(API_URL + "/api/models?year=" + YEAR + "&make=" + carMake);
        CarDataDto carDataDto = objectMapper.readValue(url, CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarModelDto>>() {});
    }

    @SneakyThrows
    public List<CarTrimDto> getAllCarTrims(String carMake, String carModel) {
        URL url = new URL(API_URL + "/api/trims?verbose=yes&year=" + YEAR + "&make=" + carMake + "&model=" + carModel);
        CarDataDto carDataDto = objectMapper.readValue(url, CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarTrimDto>>() {});
    }
}
