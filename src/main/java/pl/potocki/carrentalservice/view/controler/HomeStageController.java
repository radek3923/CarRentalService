package pl.potocki.carrentalservice.view.controler;

import com.sun.javafx.menu.MenuBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.control.NotificationPane;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PlusMinusSlider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;
import pl.potocki.carrentalservice.car.model.Car;
import pl.potocki.carrentalservice.car.model.CarImage;
import pl.potocki.carrentalservice.car.model.dto.CarMakeDto;
import pl.potocki.carrentalservice.car.model.dto.CarModelDto;
import pl.potocki.carrentalservice.car.service.CarService;
import pl.potocki.carrentalservice.carRental.model.CarRental;
import pl.potocki.carrentalservice.carRental.service.CarRentalService;

import javax.management.Notification;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class HomeStageController {

    private final CarService carService;
    private final CarRentalService carRentalService;
    private final String defaultMinPrice = "100";
    private final String defaultMaxPrice = "1000";
    private final int perDayParameter = 200;

    private Task<List<Car>> searchTask;

    @Value("classpath:/stages/RentalCarsStage.fxml")
    private Resource rentalCarsStageResource;

    @Value("classpath:/stages/CarDetailsStage.fxml")
    private Resource carDetailsStageResource;

    @Value("${spring.application.ui.rentalCarsStage.title}")
    private String rentalCarsStageTitle;

    @Value("${spring.application.ui.carDetailsStage.title}")
    private String carDetailsStageTitle;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button seeRentalCarsButton;
    @FXML
    private Button rentCarButton;
    @FXML
    private Button seeCarDetailsButton;
    @FXML
    private TextField priceRangeFromTextField;
    @FXML
    private TextField priceRangeToTextField;
    @FXML
    private Label infoLabel;
    @FXML
    private Label rentalPriceLabel;
    @FXML
    private ScrollBar scroll;

    @FXML
    private DatePicker rentalCarDatePicker;
    @FXML
    private DatePicker returnCarDatePicker;
    @FXML
    private ComboBox<String> carMakesComboBox;
    @FXML
    private ComboBox<String> carModelsComboBox;

    @FXML
    private TableView<CarImage> carImagesTableView;
    @FXML
    private TableColumn<CarImage, ImageView> carImagesColumn;

    @FXML
    private TableView<Car> carDataTableView = new TableView<>();
    @FXML
    private TableColumn<Car, String> carMakeColumn;
    @FXML
    private TableColumn<Car, String> carModelColumn;
    @FXML
    private TableColumn<Car, String> carYearColumn;
    @FXML
    private TableColumn<Car, String> carDescriptionColumn;
    @FXML
    private TableColumn<Car, String> carPriceColumn;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private NotificationPane notificationPane;
    @FXML
    private PlusMinusSlider fromSlider;
    @FXML
    private PlusMinusSlider toSlider;

    @FXML
    public void initialize() {
        clearSearchingOptionsButtonAction();

        searchButton.setOnAction(
                actionEvent -> searchCarsButtonAction());

        clearButton.setOnAction(
                actionEvent -> clearSearchingOptionsButtonAction()
        );

        fromSlider.setOnValueChanged(event -> {
            int value = (int) (Integer.parseInt(priceRangeFromTextField.getText()) + fromSlider.getValue());
            if (value < 0) value = 0;
            priceRangeFromTextField.setText(Integer.toString(value));
        });

        toSlider.setOnValueChanged(event -> {
            int value = (int) (Integer.parseInt(priceRangeToTextField.getText()) + toSlider.getValue());
            if (value < 0) value = 0;
            priceRangeToTextField.setText(Integer.toString(value));
        });


        rentalCarDatePicker.setOnAction(
                actionEvent -> updateRentalPriceLabel(carDataTableView.getSelectionModel().getSelectedItem().getMsrp())
        );
        returnCarDatePicker.setOnAction(
                actionEvent -> updateRentalPriceLabel(carDataTableView.getSelectionModel().getSelectedItem().getMsrp())
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
                actionEvent -> {
                    try {
                        if (carDataTableView.getSelectionModel().getSelectedItem() != null) {
                            handleOpenCarDetailsStage(carDataTableView.getSelectionModel().getSelectedItem());
                        } else {
                            noCarSelected();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        carDataTableView.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        Car currentCar = carDataTableView.getSelectionModel().getSelectedItem();
                        updateRentalPriceLabel(currentCar.getMsrp());
                        rentCar(currentCar, new BigDecimal(rentalPriceLabel.getText()), rentalCarDatePicker.getValue(), returnCarDatePicker.getValue());

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

    private void noCarSelected() {
        Notifications.create()
                .title("No Car Selected")
                .text("Please select a car.")
                .owner(notificationPane)
                .position(Pos.CENTER)
                .hideAfter(Duration.seconds(2))
                .showWarning();
    }

    private void updateRentalPriceLabel(int msrp) {
        LocalDate dateFrom = rentalCarDatePicker.getValue();
        LocalDate dateTo = returnCarDatePicker.getValue();
        long differenceInDays = java.time.temporal.ChronoUnit.DAYS.between(dateFrom, dateTo);

        BigDecimal price = new BigDecimal(msrp / perDayParameter * differenceInDays);
        rentalPriceLabel.setText(String.valueOf(price));
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

    private void handleOpenCarDetailsStage(Car car) throws IOException {
        Stage newWindow = new Stage();
        FXMLLoader loader = new FXMLLoader(carDetailsStageResource.getURL());
        CarDetailsStageController controller = new CarDetailsStageController(carService);
        controller.setCar(car);
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        newWindow.setScene(scene);
        newWindow.setTitle(carDetailsStageTitle);
        newWindow.show();
    }

    public void rentCar(Car car, BigDecimal price, LocalDate rentalDate, LocalDate returnDate) {
        rentCarButton.setOnMouseClicked(
                mouseEvent -> {
                    if (carDataTableView.getSelectionModel().getSelectedItem() == null) {
                        noCarSelected();
                    }
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

        priceRangeFromTextField.setText(defaultMinPrice);
        priceRangeToTextField.setText(defaultMaxPrice);

        infoLabel.setText("");
        rentalPriceLabel.setText("0");

        priceRangeFromTextField.setText(defaultMinPrice);
        priceRangeToTextField.setText(defaultMaxPrice);

        rentalCarDatePicker.setValue(LocalDate.now());
        returnCarDatePicker.setValue(LocalDate.now().plusDays(1));
    }


    public void searchCarsButtonAction() {
        String carMake = carMakesComboBox.getSelectionModel().getSelectedItem();
        String carModel = carModelsComboBox.getSelectionModel().getSelectedItem();
        int priceFrom = Integer.parseInt(priceRangeFromTextField.getText());
        int priceTo = Integer.parseInt(priceRangeToTextField.getText());

        List<Car> cars = carService.getAllCarTrims(carMake, carModel, priceFrom, priceTo, perDayParameter);
        cars.forEach(car -> car.setDescription(StringUtils.upperCase(car.getDescription())));
        infoLabel.setText("Found " + cars.size() + " cars");

        ObservableList<Car> data = FXCollections.observableList(cars);
        setColumnForCarTableView(carDataTableView);
        wrapEachColumnsFromCarTableView();

        carDataTableView.setItems(data);
        getCarImagesTableView(cars);
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

    public void getCarImagesTableView(List<Car> cars) {
        getCarImages(cars);
    }

    public void setCarImagesTableView(List<CarImage> imagesFromCars) {
        ObservableList<CarImage> carImages = FXCollections.observableList(imagesFromCars);
        carImagesColumn.setCellValueFactory((new PropertyValueFactory<>("image")));
        carImagesTableView.setFixedCellSize(150);
        carImagesTableView.setItems(carImages);
    }

    public List<CarImage> getCarImages(List<Car> cars) {
        progressBar.setVisible(true);
        Task<List<CarImage>> task = new Task<List<CarImage>>() {
            @Override
            protected List<CarImage> call() {
                List<CarImage> carImages = new ArrayList<>();
                int totalProgress = cars.size();
                int currentProgress = 0;

                for (Car car : cars) {
                    ImageView imageView = new ImageView(carService.getCarImage(car.getCarMake(), car.getCarModel(), "", ""));
                    imageView.setFitHeight(180);
                    imageView.setFitWidth(300);
                    CarImage carImage = new CarImage(imageView);
                    carImages.add(carImage);

                    currentProgress++;
                    updateProgress(currentProgress, totalProgress);
                }
                return carImages;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(new EventHandler<>() {
            @Override
            public void handle(WorkerStateEvent event) {
                setCarImagesTableView(task.getValue());
                progressBar.setVisible(false);
            }
        });

        Thread thread = new Thread(task);
        thread.start();

        return new ArrayList<CarImage>();
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

    public void setColumnForCarTableView(TableView<Car> tableView) {
        carMakeColumn.setCellValueFactory(new PropertyValueFactory<>("carMake"));
        carModelColumn.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        carYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        carDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        carPriceColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(Integer.toString(cellData.getValue().getMsrp() / perDayParameter)));

        tableView.setFixedCellSize(150);
    }
}
