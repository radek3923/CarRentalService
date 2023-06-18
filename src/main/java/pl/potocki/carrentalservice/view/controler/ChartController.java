package pl.potocki.carrentalservice.view.controler;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.service.CarService;

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
    public Button clearButton;
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
    public TableColumn<?, ImageView> carImagesColumn;
    @FXML
    public TableColumn<?, String> carMakeColumn;
    @FXML
    public TableColumn<?, String> carModelColumn;
    @FXML
    public TableColumn<?, String> CarDescriptionColumn;


    @FXML
    ImageView carViewTest;

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

        clearButton.setOnAction(
                actionEvent -> clearSearchingOptionsButtonAction()
        );

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

        carViewTest.setImage(carService.getCarImage(carMakesComboBox.getSelectionModel().getSelectedItem(), carModelsComboBox.getSelectionModel().getSelectedItem()));
    }

    public void clearSearchingOptionsButtonAction() {
        carMakesComboBox.getSelectionModel().select(0);
        carModelsComboBox.getSelectionModel().select(0);
        //TODO add more default searching options in future
    }


    public void searchCarsButtonAction() {
        String carMake = carMakesComboBox.getSelectionModel().getSelectedItem();
        String carModel = carModelsComboBox.getSelectionModel().getSelectedItem();
        List<Car> cars = carService.getAllCarTrims(carMake, carModel);

        for(Car car: cars){
            System.out.println(car.toString());
        }
//        ObservableList<Movie> data = FXCollections.observableList(movies);
    }

//    public static List<ImagePoster> getImagePosters(List<? extends Movie> favouriteMovies) {
//        return favouriteMovies.stream()
//                .map(Movie::getPoster)
//                .map(s -> {
//                    ImageView imageView = new ImageView(s);
//                    imageView.setFitHeight(150);
//                    imageView.maxHeight(150);
//                    imageView.setFitWidth(180);
//                    return new ImagePoster(imageView);
//                })
//                .collect(Collectors.toList());
//    }

//    public void setTableViewForImagePoster() {
//        carImagesColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
//        carImagesColumn.setFixedCellSize(155);
//        carImagesColumn.set
//    }

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
