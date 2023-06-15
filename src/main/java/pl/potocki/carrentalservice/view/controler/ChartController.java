package pl.potocki.carrentalservice.view.controler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.service.CarService;

import javax.swing.text.html.ImageView;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ChartController {

    private final CarService carRentalService;
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
//        System.out.println(dateFromDatePicker.getValue());
//        System.out.println(dateToDatePicker.getValue());


        for(CarMakeDto carMakeDto : carRentalService.getAllCarMakes()){
            System.out.println(carMakeDto.getName());
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
