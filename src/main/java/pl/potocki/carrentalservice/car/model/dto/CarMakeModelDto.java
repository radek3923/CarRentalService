package pl.potocki.carrentalservice.car.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CarMakeModelDto {
    @JsonProperty("make_id")
    private int makeId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("make")
    private CarMakeDto carMakeDto;
}
