package pl.potocki.carrentalservice.car.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarTrimDto;

@Component
public class CarMapper extends ObjectMapper {
    public Car toCar(CarTrimDto carTrimDto) {
        return Car.builder()
                .carMake(carTrimDto.getCarMakeModelDto().getCarMakeDto().getName())
                .carModel(carTrimDto.getCarMakeModelDto().getName())
                .year(carTrimDto.getYear())
                .description(carTrimDto.getDescription())
                .msrp(carTrimDto.getMsrp())
                .build();
    }
}
