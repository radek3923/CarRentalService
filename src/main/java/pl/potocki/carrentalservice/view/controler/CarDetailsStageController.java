package pl.potocki.carrentalservice.view.controler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.PaintCombination;
import pl.potocki.carrentalservice.car.service.CarService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Controller
@Data
public class CarDetailsStageController implements Initializable {

    private final CarService carService;
    private Car car;

    @FXML
    private ImageView carImageView;
    @FXML
    private Label carInfoLabel;
    @FXML
    private ComboBox<PaintCombination> availableColorComboBox;
    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> availavbleAngles = Arrays.asList("01", "29", "23", "21", "09", "05");
        AtomicInteger currentIndex = new AtomicInteger(0);

        String carMake = car.getCarMake();
        String carModel = car.getCarModel();

        carInfoLabel.setText("Car details: " + carMake + ", " + carModel + ", " + car.getDescription());
        setAvailableColorCombinations(carMake, carModel);

        carImageView.setImage(getImage(carMake, carModel, getAngle(availavbleAngles, currentIndex.get())));

        availableColorComboBox.setOnAction(event -> {
            carImageView.setImage(getImage(carMake, carModel, getAngle(availavbleAngles, currentIndex.get())));
        });

        nextImageButton.setOnAction(event -> {
            currentIndex.getAndIncrement();
            carImageView.setImage(getImage(carMake, carModel, getAngle(availavbleAngles, currentIndex.get())));
        });

        previousImageButton.setOnAction(event -> {
            currentIndex.getAndDecrement();
            carImageView.setImage(getImage(carMake, carModel, getAngle(availavbleAngles, currentIndex.get())));
        });


        availableColorComboBox.setConverter(new StringConverter<PaintCombination>() {
            @Override
            public String toString(PaintCombination paintCombination) {
                return paintCombination != null ? paintCombination.getPaintDescription() : "";
            }

            @Override
            public PaintCombination fromString(String string) {
                return null;
            }
        });
    }

    public String getAngle(List<String> availableAngles, int index) {
        int size = availableAngles.size();
        int result = (index % size + size) % size;
        return availableAngles.get(result);
    }

    public Image getImage(String carMake, String carModel, String angle) {
        PaintCombination selectedPaintCombination = availableColorComboBox.getSelectionModel().getSelectedItem();
        return carService.getCarImage(carMake, carModel, selectedPaintCombination != null ? selectedPaintCombination.getId() : "", angle);
    }

    public void setAvailableColorCombinations(String carMake, String carModel) {
        List<PaintCombination> availableColorCombinatios = carService.getAvailablePaintCombinations(carMake, carModel);

        availableColorComboBox.getItems().clear();
        availableColorComboBox.getItems().addAll(availableColorCombinatios);
        availableColorComboBox.getSelectionModel().select(0);
    }
}
