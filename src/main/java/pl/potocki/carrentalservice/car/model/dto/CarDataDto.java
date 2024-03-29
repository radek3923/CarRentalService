package pl.potocki.carrentalservice.car.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDataDto {
    @JsonProperty("data")
    private List<?> carData;
}
