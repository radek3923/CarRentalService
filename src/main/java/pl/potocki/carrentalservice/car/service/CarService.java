package pl.potocki.carrentalservice.car.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
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
    private final String carApiUrl = "https://carapi.app";
    private final String imaginStudioUrl = "https://cdn.imagin.studio/getImage?&customer=plpretius";
    private final String YEAR = "2020";

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = objectMapper.readValue(new URL(carApiUrl + "/api/makes?year=" + YEAR), CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarMakeDto>>() {
        });
    }

    @SneakyThrows
    public List<CarModelDto> getAllCarModels(String carMake) {
        URL url = new URL(carApiUrl + "/api/models?year=" + YEAR + "&make=" + carMake);
        CarDataDto carDataDto = objectMapper.readValue(url, CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarModelDto>>() {
        });
    }

    @SneakyThrows
    public List<CarTrimDto> getAllCarTrims(String carMake, String carModel) {
        URL url = new URL(carApiUrl + "/api/trims?verbose=yes&year=" + YEAR + "&make=" + carMake + "&model=" + carModel);
        CarDataDto carDataDto = objectMapper.readValue(url, CarDataDto.class);
        return objectMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarTrimDto>>() {
        });
    }

    @SneakyThrows
    public ImageView getCarImage(String carMake, String carModel) {
//        URL url = new URL(imaginStudioUrl + "&make=" + carMake + "&model=" + carModel);
//        String url = imaginStudioUrl + "&make=" + carMake + "&model=" + carModel;
        String url = "https://apartamentyzakopane.pl/blog/wp-content/uploads/2020/11/20120821_IMG_7221.jpg";
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);


        return imageView;
    }
}
