package pl.potocki.carrentalservice.model.carApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarMakeDto {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
}
