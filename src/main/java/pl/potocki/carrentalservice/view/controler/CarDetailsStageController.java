package pl.potocki.carrentalservice.view.controler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.service.CarService;

import java.net.URL;
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
    private ComboBox availableColorComboBox;
    @FXML
    private Button previousImageButton;
    @FXML
    private Button nextImageButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carInfoLabel.setText(car.toString());
    }
}
