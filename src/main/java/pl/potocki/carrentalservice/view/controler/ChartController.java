package pl.potocki.carrentalservice.view.controler;

import ch.qos.logback.classic.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.model.dto.CarTrimDto;
import pl.potocki.carrentalservice.car.service.CarService;

import javax.swing.text.html.ImageView;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final CarService carService;
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
    public ComboBox<String> carMakesComboBox;

    @FXML
    public ComboBox<String> carModelsComboBox;

    @FXML
    public TableView<Car> carTableView;
    @FXML
    public TableColumn<?, ImageView> carColumn1;


//    @FXML
//    public TableColumn<> carColumn2;
//
//    @FXML
//    public TableColumn carColumn3;
//
//    @FXML
//    public TableColumn carColumn4;
//
//    @FXML
//    public ImageView backgroundImageView;
//    @FXML
//    public Label backgroundLabel;

    @FXML
    public void initialize() {
        searchButton.setText("Search");
        priceRangeFromTextField.setText(defaultPrice);
        priceRangeToTextField.setText(maxPrice);

        dateFromDatePicker.setValue(LocalDate.now());
        dateToDatePicker.setValue(LocalDate.now().plusDays(1));

//        setBackgroundImageView();
//        setBackgroundLabel();

        searchButton.setOnAction(
                actionEvent -> searchCarsButtonAction());

        setPriceRangeSlider();
        setDefaultCarMakes();
    }

//    public void setBackgroundImageView(){
//        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
//        String backroundImagePath = currentPath + "\\mountains.jpg";
//
//        Image backgroundImage = new Image(backroundImagePath, backgroundImageView.getFitWidth(),
//                backgroundImageView.getFitHeight(), false, false);
//
//
//        backgroundImageView.setImage(backgroundImage);
//        backgroundImageView.setOpacity(0.7);
//
//    }

    public void setPriceRangeSlider() {
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

    public void setDefaultCarMakes() {

        List<String> carMakes = carService.getAllCarMakes().stream()
                .map(CarMakeDto::getName)
                .collect(Collectors.toList());

        carMakes.add(0, "");
        carMakesComboBox.getItems().addAll(carMakes);
        carMakesComboBox.getSelectionModel().select(1);

        updateCarModels(carMakesComboBox.getSelectionModel().getSelectedItem());

        carMakesComboBox.setOnAction(event -> {
            String selectedCarMake = carMakesComboBox.getValue();
            log.info("User selected car make: {}", selectedCarMake);
            updateCarModels(selectedCarMake);
        });
    }

    public void updateCarModels(String carMake) {
        carModelsComboBox.getItems().clear();
        List<String> carModels = carService.getAllCarModels(carMake).stream()
                .map(CarModelDto::getName)
                .collect(Collectors.toList());
        carModels.add(0, "");

        carModelsComboBox.getItems().addAll(carModels);
        log.info("Added car models to comboBox: {}", carModels);

        carModelsComboBox.getSelectionModel().select(1);
    }


    public void searchCarsButtonAction() {
//        System.out.println(dateFromDatePicker.getValue());
//        System.out.println(dateToDatePicker.getValue());


        for (CarTrimDto carMakeDto : carService.getAllCarTrims("BMW", "")) {
            System.out.println(carMakeDto.toString());
        }
    }

//    public void setBackgroundLabel(){
//        backgroundLabel.setText("Car Rental Service");
//        backgroundLabel.setFont(new Font("Brush Script MT", 80));
//        backgroundLabel.setTextFill(Color.web("#0076a3"));
//
//        backgroundLabel.setAlignment(Pos.CENTER);
//
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setOffsetX(5);
//        dropShadow.setOffsetY(5);
//        dropShadow.setColor(Color.GRAY);
//        backgroundLabel.setEffect(dropShadow);
//
//        Reflection reflection = new Reflection();
//        reflection.setFraction(0.8);
//        backgroundLabel.setEffect(reflection);
//
//        backgroundLabel.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 10px;");
//    }
}
