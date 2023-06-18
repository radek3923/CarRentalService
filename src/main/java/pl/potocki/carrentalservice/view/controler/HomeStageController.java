package pl.potocki.carrentalservice.view.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.CarImage;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.service.CarService;
import pl.potocki.carrentalservice.carRental.model.CarRental;
import pl.potocki.carrentalservice.carRental.service.CarRentalService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class HomeStageController {

    private final CarService carService;
    private final CarRentalService carRentalService;
    private final String defaultPrice = "100";
    private final String maxPrice = "1000";


    @Value("classpath:/stages/RentalCarsStage.fxml")
    private Resource rentalCarsStageResource;

    @Value("${spring.application.ui.rentalCarsStage.title}")
    private String rentalCarsStageTitle;
    @FXML
    public Button searchButton;
    @FXML
    public Button clearButton;
    @FXML
    public Button seeRentalCarsButton;
    @FXML
    public Button rentCarButton;
    @FXML
    public Button seeCarDetailsButton;
    @FXML
    public TextField priceRangeFromTextField;
    @FXML
    public TextField priceRangeToTextField;
    @FXML
    public Label infoLabel;
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

        searchButton.setOnAction(
                actionEvent -> searchCarsButtonAction());

        clearButton.setOnAction(
                actionEvent -> clearSearchingOptionsButtonAction()
        );

        seeRentalCarsButton.setOnAction(
                actionEvent -> {
                    try {
                        handleOpenRentalCarsStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        seeCarDetailsButton.setOnAction(
                actionEvent -> System.out.println(carService.getAvailablePaintCombinations(carMakesComboBox.getValue(), carModelsComboBox.getValue()))
        );

        carDataTableView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        Car currentCar = carDataTableView.getSelectionModel().getSelectedItem();
                        rentCar(currentCar, new BigDecimal(100), dateFromDatePicker.getValue(), dateToDatePicker.getValue());

                        int selectedIndex = carDataTableView.getSelectionModel().getSelectedIndex();
                        carImagesTableView.getSelectionModel().clearAndSelect(selectedIndex);
                    }
                });

        carImagesTableView.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    carDataTableView.getSelectionModel().clearAndSelect(newValue.intValue());
                });

        setDefaultCarMakes();
        setScrollBar(carDataTableView);

        carImagesTableView.addEventFilter(ScrollEvent.ANY, Event::consume);
        carDataTableView.addEventFilter(ScrollEvent.ANY, Event::consume);
    }

    private void handleOpenRentalCarsStage() throws IOException {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(rentalCarsStageResource.getURL());
        loader.setController(new RentalCarsStageController(carRentalService));
        Scene scene = new Scene(loader.load());
        newWindow.setScene(scene);
        newWindow.setTitle(rentalCarsStageTitle);
        newWindow.show();
    }

    public void rentCar(Car car, BigDecimal price, LocalDate rentalDate, LocalDate returnDate) {
        rentCarButton.setOnMouseClicked(
                mouseEvent -> {
                    System.out.println(car);
                    System.out.println(rentalDate);
                    System.out.println(returnDate);
                    CarRental carRental = CarRental.builder()
                            .car(car)
                            .price(price)
                            .rentalDate(rentalDate)
                            .returnDate(returnDate)
                            .build();

                    infoLabel.setText(carRentalService.addCarRental(carRental));
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
        List<String> carPaintIds = carService.getAvailablePaintCombinations(carMake, carModel);

        ObservableList<Car> data = FXCollections.observableList(cars);
        setColumnForCarTableView(carDataTableView);
        wrapEachColumnsFromCarTableView();

        carDataTableView.setItems(data);
        setCarImagesTableView(cars, carPaintIds);
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

    public void setCarImagesTableView(List<Car> cars, List<String> carPaintIds) {
        List<CarImage> imagesFromCars = getCarImages(cars, carPaintIds);
        ObservableList<CarImage> carImages = FXCollections.observableList(imagesFromCars);

        carImagesColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
        carImagesTableView.setFixedCellSize(150);
        carImagesTableView.setItems(carImages);
    }


    public List<CarImage> getCarImages(List<Car> cars, List<String> carPaintIds) {
        Random rand = new Random();
        int size = carPaintIds.size();

        return cars.stream()
                .map(c -> {
                    String paintId = carPaintIds.isEmpty() ? "" : carPaintIds.get(rand.nextInt(size));
                    ImageView imageView = new ImageView(carService.getCarImage(c.getCarMake(), c.getCarModel(), paintId));
                    imageView.setFitHeight(180);
                    imageView.maxHeight(180);
                    imageView.setFitWidth(300);
                    return new CarImage(imageView);
                })
                .toList();
    }

    public void wrapEachColumnsFromCarTableView() {
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

    public void setColumnForCarTableView(TableView<?> tableView) {
        carMakeColumn.setCellValueFactory((new PropertyValueFactory<>("carMake")));
        carModelColumn.setCellValueFactory((new PropertyValueFactory<>("carModel")));
        carYearColumn.setCellValueFactory((new PropertyValueFactory<>("year")));
        carDescriptionColumn.setCellValueFactory((new PropertyValueFactory<>("description")));
        tableView.setFixedCellSize(150);
    }
}
