package pl.potocki.carrentalservice.car.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class CarService {
    private final CarMapper carMapper = new CarMapper();
    private final String getCarDataApi = "https://carapi.app";
    private final String getCarImagesApiUrl = "https://cdn.imagin.studio/getImage?&customer=plpretius";
    private final String getCarPaintCombinationsApiUrl = "https://cdn.imagin.studio/getPaints?&customer=plpretius&target=car";
    private final String YEAR = "2020";

    @SneakyThrows
    public List<CarMakeDto> getAllCarMakes() {
        CarDataDto carDataDto = carMapper.readValue(new URL(getCarDataApi + "/api/makes?year=" + YEAR), CarDataDto.class);
        return carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarMakeDto>>() {
        });
    }

    @SneakyThrows
    public List<CarModelDto> getAllCarModels(String carMake) {
        URL url = new URL(getCarDataApi + "/api/models?year=" + YEAR + "&make=" + carMake);
        CarDataDto carDataDto = carMapper.readValue(url, CarDataDto.class);
        return carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarModelDto>>() {
        });
    }

    @SneakyThrows
    public List<Car> getAllCarTrims(String carMake, String carModel, int priceFrom, int priceTo, int perDayParameter) {
        URL url = new URL(getCarDataApi + "/api/trims?verbose=yes&year=" + YEAR + "&make=" + carMake + "&model=" + carModel);
        CarDataDto carDataDto = carMapper.readValue(url, CarDataDto.class);

        List<CarTrimDto> carTrimDtoList = carMapper.convertValue(carDataDto.getCarData(), new TypeReference<List<CarTrimDto>>() {
        });

        return carTrimDtoList.stream()
                .filter(c -> c.getMsrp()/perDayParameter > priceFrom && c.getMsrp()/perDayParameter < priceTo)
                .map(carMapper::toCar)
                .toList();
    }

    @SneakyThrows
    public Image getCarImage(String carMake, String carModel, String paintId) {
        String imageUrl = getCarImagesApiUrl + "&zoomType=fullscreen&make=" + carMake + "&modelFamily=" + carModel + "&paintId=" + paintId;
        BufferedImage bufferedImage = downloadImageFromURL(imageUrl);
        return convertBufferedImageToImage(bufferedImage);
    }

    @SneakyThrows
    public List<String> getAvailablePaintCombinations(String carMake, String carModel) {
        URL url = new URL(getCarPaintCombinationsApiUrl + "&make=" + carMake + "&modelFamily=" + carModel);
        List<String> availablePaintCombinations = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(url);

        JsonNode paintCombinationsNode = rootNode.path("paintData").path("paintCombinations");
        Iterator<String> fieldNames = paintCombinationsNode.fieldNames();

        while (fieldNames.hasNext()) {
            String paintCombinationKey = fieldNames.next();
            JsonNode paintCombinationNode = paintCombinationsNode.path(paintCombinationKey);
            boolean isAvailable = paintCombinationNode.path("mapped").elements().next().path("available").asBoolean();
            if (isAvailable) {
                availablePaintCombinations.add(paintCombinationKey);
            }
        }
        return availablePaintCombinations;
    }

    private BufferedImage downloadImageFromURL(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        return ImageIO.read(url);
    }

    private Image convertBufferedImageToImage(BufferedImage bufferedImage) {
        return javafx.embed.swing.SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
