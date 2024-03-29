package pl.potocki.carrentalservice.car.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CarMakeDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
}
