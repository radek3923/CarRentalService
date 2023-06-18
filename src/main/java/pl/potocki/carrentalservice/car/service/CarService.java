package pl.potocki.carrentalservice.car.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.car.mapper.CarMapper;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarDataDto;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.model.dto.CarTrimDto;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {
    private final CarMapper carMapper = new CarMapper();
    private final String carApiUrl = "https://carapi.app";
    private final String imaginStudioUrl = "https://cdn.imagin.studio/getImage?&customer=plpretius";
    private final String YEAR = "2020";

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = carMapper.readValue(new URL(carApiUrl + "/api/makes?year=" + YEAR), CarDataDto.class);
        return carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarMakeDto>>() {
        });
    }

    @SneakyThrows
    public List<CarModelDto> getAllCarModels(String carMake) {
        URL url = new URL(carApiUrl + "/api/models?year=" + YEAR + "&make=" + carMake);
        CarDataDto carDataDto = carMapper.readValue(url, CarDataDto.class);
        return carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarModelDto>>() {
        });
    }

    @SneakyThrows
    public List<Car> getAllCarTrims(String carMake, String carModel) {
        URL url = new URL(carApiUrl + "/api/trims?verbose=yes&year=" + YEAR + "&make=" + carMake + "&model=" + carModel);
        CarDataDto carDataDto = carMapper.readValue(url, CarDataDto.class);

        List<CarTrimDto> carTrimDtoList = carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarTrimDto>>() {
        });

        return carTrimDtoList.stream()
                .map(carMapper::toCar)
                .toList();
    }

    @SneakyThrows
    public Image getCarImage(String carMake, String carModel) {
        String imageUrl =  imaginStudioUrl + "&make=" + carMake + "&modelFamily=" + carModel;
        BufferedImage bufferedImage = downloadImageFromURL(imageUrl);
        return convertBufferedImageToImage(bufferedImage);
    }

    private BufferedImage downloadImageFromURL(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }

    private Image convertBufferedImageToImage(BufferedImage bufferedImage) {
        return javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
