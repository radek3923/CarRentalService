package pl.potocki.carrentalservice.car.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CarTrimDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("year")
    private int year;

    @JsonProperty("msrp")
    private int msrp;

    @JsonProperty("make_model")
    private CarMakeModelDto carMakeModelDto;
}
