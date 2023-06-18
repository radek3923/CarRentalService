package pl.potocki.carrentalservice.view.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.CarImage;
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
    public Button seeRentalCarsButton;
    @FXML
    public Button rentCarButton;
    @FXML
    public TextField priceRangeFromTextField;
    @FXML
    public TextField priceRangeToTextField;
    @FXML
    public Slider priceRangeSlider;
    @FXML
    protected ScrollBar scroll;

    @FXML
    public DatePicker dateFromDatePicker;
    @FXML
    public DatePicker dateToDatePicker;
    @FXML
    public ComboBox<String> carMakesComboBox;
    @FXML
    public ComboBox<String> carModelsComboBox;

    @FXML
    public TableView<CarImage> carImagesTableView;
    @FXML
    public TableColumn<CarImage, ImageView> carImagesColumn;

    @FXML
    public TableView<Car> carDataTableView = new TableView<>();
    @FXML
    public TableColumn<?, String> carMakeColumn;
    @FXML
    public TableColumn<?, String> carModelColumn;
    @FXML
    public TableColumn<?, String> carYearColumn;
    @FXML
    public TableColumn<?, String> carDescriptionColumn;

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

        carDataTableView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        Car currentCar = carDataTableView.getSelectionModel().getSelectedItem();
                        rentCar(currentCar, dateFromDatePicker.getValue(), dateToDatePicker.getValue());

                        int selectedIndex = carDataTableView.getSelectionModel().getSelectedIndex();
                        carImagesTableView.getSelectionModel().clearAndSelect(selectedIndex);
                    }
                });

        carImagesTableView.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    carDataTableView.getSelectionModel().clearAndSelect(newValue.intValue());
                });


        setPriceRangeSlider();
        setDefaultCarMakes();
        setScrollBar(carDataTableView);

        carImagesTableView.addEventFilter(ScrollEvent.ANY, Event::consume);
        carDataTableView.addEventFilter(ScrollEvent.ANY, Event::consume);
    }

    public void rentCar(Car currentCar, LocalDate dateFromDatePicker, LocalDate dateToDatePicker) {
        rentCarButton.setOnMouseClicked(
                mouseEvent -> {
                    System.out.println(currentCar);
                    System.out.println(dateFromDatePicker);
                    System.out.println(dateToDatePicker);
                });
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

    public void clearSearchingOptionsButtonAction() {
        carMakesComboBox.getSelectionModel().select(0);
        carModelsComboBox.getSelectionModel().select(0);
        //TODO add more default searching options in future
    }


    public void searchCarsButtonAction() {
        String carMake = carMakesComboBox.getSelectionModel().getSelectedItem();
        String carModel = carModelsComboBox.getSelectionModel().getSelectedItem();
        List<Car> cars = carService.getAllCarTrims(carMake, carModel);

        for (Car car : cars) {
            System.out.println(car.toString());
        }

        ObservableList<Car> data = FXCollections.observableList(cars);
        setColumnForCarTableView(carDataTableView);
        wrapEachColumnsFromCarTableView();

        carDataTableView.setItems(data);
        setCarImagesTableView(cars);
        scroll.setMax(data.size());
    }

    public void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getItems().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            tableView.scrollTo(t1.intValue());
            carImagesTableView.scrollTo(t1.intValue());
        });
    }

    private void setCarImagesTableView(List<Car> cars) {
        List<CarImage> imagesFromCars = getCarImages(cars);
        ObservableList<CarImage> carImages = FXCollections.observableList(imagesFromCars);

        carImagesColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
        carImagesTableView.setFixedCellSize(150);
        carImagesTableView.setItems(carImages);
    }

    private List<CarImage> getCarImages(List<Car> cars) {
        return cars.stream()
                .map(c -> {
                    ImageView imageView = new ImageView(carService.getCarImage(c.getCarMake(), c.getCarModel()));
                    imageView.setFitHeight(180);
                    imageView.maxHeight(180);
                    imageView.setFitWidth(300);
                    return new CarImage(imageView);
                })
                .toList();
    }

    private void wrapEachColumnsFromCarTableView() {
        wrapTextForTableColumn(carMakeColumn);
        wrapTextForTableColumn(carModelColumn);
        wrapTextForTableColumn(carDescriptionColumn);
    }

    public static <T> void wrapTextForTableColumn(TableColumn<T, String> tableColumn) {
        tableColumn.setCellFactory(tc -> {
            TableCell<T, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void setColumnForCarTableView(TableView<?> tableView) {
        carMakeColumn.setCellValueFactory((new PropertyValueFactory<>("carMake")));
        carModelColumn.setCellValueFactory((new PropertyValueFactory<>("carModel")));
        carYearColumn.setCellValueFactory((new PropertyValueFactory<>("year")));
        carDescriptionColumn.setCellValueFactory((new PropertyValueFactory<>("description")));
        tableView.setFixedCellSize(150);
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
