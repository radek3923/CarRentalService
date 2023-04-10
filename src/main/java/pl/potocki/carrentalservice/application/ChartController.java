package pl.potocki.carrentalservice.application;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;
import java.nio.file.Paths;
import java.time.LocalDate;

@Component
public class ChartController {
    private final String defaultPrice = "100";
    private final String maxPrice = "1000";

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
    public Label backgroundLabel;

    @FXML
    public void initialize() {
        searchButton.setText("Search");
        priceRangeFromTextField.setText(defaultPrice);
        priceRangeToTextField.setText(maxPrice);

        dateFromDatePicker.setValue(LocalDate.now());
        dateToDatePicker.setValue(LocalDate.now().plusDays(1));

        setBackgroundImageView();
        setBackgroundLabel();

        searchButton.setOnAction(
                actionEvent -> searchCarsButtonAction());

        setPriceRangeSlider();
    }

    public void setBackgroundImageView(){
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        String backroundImagePath = currentPath + "\\mountains.jpg";

        Image backgroundImage = new Image(backroundImagePath, backgroundImageView.getFitWidth(),
                backgroundImageView.getFitHeight(), false, false);


        backgroundImageView.setImage(backgroundImage);
        backgroundImageView.setOpacity(0.7);

    }

    public void setPriceRangeSlider(){
        priceRangeSlider.setMin(0);
        priceRangeSlider.setMax(Integer.parseInt(maxPrice));
        priceRangeSlider.setValue(Integer.parseInt(defaultPrice));

        priceRangeFromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceRangeFromTextField.setText(oldValue);
            } else if (!newValue.isEmpty()) {
                double value = Double.parseDouble(newValue);
                priceRangeSlider.setValue(value);
            }
        });
//
        priceRangeToTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                priceRangeToTextField.setText(oldValue);
            } else if (!newValue.isEmpty()) {
                double value = Double.parseDouble(newValue);
                priceRangeSlider.setMax(value);
            }
        });

        priceRangeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            priceRangeFromTextField.setText(Double.toString(value));
            priceRangeToTextField.setText(Double.toString(priceRangeSlider.getMax()));
        });
    }


    public void searchCarsButtonAction(){
        System.out.println(dateFromDatePicker.getValue());
        System.out.println(dateToDatePicker.getValue());
    }

    public void setBackgroundLabel(){
        backgroundLabel.setText("Car Rental Service");
        backgroundLabel.setFont(new Font("Brush Script MT", 80));
        backgroundLabel.setTextFill(Color.web("#0076a3"));

        backgroundLabel.setAlignment(Pos.CENTER);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.GRAY);
        backgroundLabel.setEffect(dropShadow);

        Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        backgroundLabel.setEffect(reflection);

        backgroundLabel.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 10px;");
    }
}
