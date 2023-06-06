package pl.potocki.carrentalservice.model.carApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarDataDto {
    @JsonProperty("data")
    private List<CarMakeDto> carMakes;
}
