package pl.potocki.carrentalservice.view.controler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.service.CarService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private ComboBox<String> availableColorComboBox;
    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String carMake = car.getCarMake();
        String carModel = car.getCarModel();

        carInfoLabel.setText("Car details: " + carMake + ", " + carModel + ", " + car.getDescription());
        setAvailableColorCombinations(carMake, carModel);

        carImageView.setImage(getImage(carMake, carModel));

        availableColorComboBox.setOnAction(event -> {
            carImageView.setImage(getImage(carMake, carModel));
        });
    }

    public Image getImage(String carMake, String carModel){
        return carService.getCarImage(carMake, carModel, availableColorComboBox.getSelectionModel().getSelectedItem());
    }

    public void setAvailableColorCombinations(String carMake, String carModel){
        List<String> availableColorCombinatios = carService.getAvailablePaintCombinations(carMake, carModel);

        availableColorComboBox.getItems().clear();
        availableColorComboBox.getItems().addAll(availableColorCombinatios);
        availableColorComboBox.getSelectionModel().select(0);
    }
}
