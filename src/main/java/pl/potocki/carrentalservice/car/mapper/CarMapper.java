package pl.potocki.carrentalservice.car.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarTrimDto;

@Component
public class CarMapper extends ObjectMapper {
    public Car toCar(CarTrimDto carTrimDto) {
        return new Car(carTrimDto.getCarMakeModelDto().getCarMakeDto().getName(),
                carTrimDto.getCarMakeModelDto().getName(),
                carTrimDto.getYear(),
                carTrimDto.getDescription());
    }
}
