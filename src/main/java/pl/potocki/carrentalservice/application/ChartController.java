package pl.potocki.carrentalservice.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ChartController {

    @FXML
    public Button searchButton;
    @FXML
    public TextField priceRangeFromTextField;
    @FXML
    public TextField priceRangeToTextField;
    @FXML
    public Slider priceRangeSlider;

    @FXML
    public DatePicker dateFromDatePicker;
    @FXML
    public DatePicker dateToDatePicker;

    @FXML
    public ImageView backgroundImageView;

    @FXML
    public void initialize() {
        searchButton.setText("Search");
        priceRangeFromTextField.setText("0");
        priceRangeToTextField.setText("100");

        dateFromDatePicker.setValue(LocalDate.now());
        dateToDatePicker.setValue(LocalDate.now().plusDays(1));

        setBackgroundImageView();

        searchButton.setOnAction(
                actionEvent -> searchCarsButtonAction());
    }

    public void setBackgroundImageView(){
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String backroundImagePath = currentPath + "\\mountains.jpg";

        Image bacgroundImage = new Image(backroundImagePath, backgroundImageView.getFitWidth(),
                backgroundImageView.getFitHeight(), false, false);
        backgroundImageView.setImage(bacgroundImage);
    }

    public void searchCarsButtonAction(){
        System.out.println(dateFromDatePicker.getValue());
        System.out.println(dateToDatePicker.getValue());
    }
}
