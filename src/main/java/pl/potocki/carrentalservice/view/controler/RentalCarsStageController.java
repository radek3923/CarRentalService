package pl.potocki.carrentalservice.view.controler;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import pl.potocki.carrentalservice.carRental.model.CarRental;
import pl.potocki.carrentalservice.carRental.service.CarRentalService;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RentalCarsStageController implements Initializable {

    private final CarRentalService carRentalService;
    @FXML
    private TableView<CarRental> carDataTableView = new TableView<>();
    @FXML
    private TableColumn<CarRental, Long> carRentalIdColumn;
    @FXML
    private TableColumn<CarRental, String> carMakeColumn;
    @FXML
    private TableColumn<CarRental, String> carModelColumn;
    @FXML
    private TableColumn<CarRental, String> carYearColumn;
    @FXML
    private TableColumn<CarRental, String> carDescriptionColumn;

    @FXML
    private TableColumn<CarRental, LocalDate> carRentalDateColumn;

    @FXML
    private TableColumn<CarRental, LocalDate> carReturnDateColumn;

    @FXML
    private TableColumn<CarRental, BigDecimal> carPriceColumn;
    @FXML
    private ScrollBar scroll;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCarRentalHistory();
        setScrollBar(carDataTableView);
    }

    public void showCarRentalHistory() {
        List<CarRental> carRentalList = carRentalService.findAll();
        ObservableList<CarRental> data = FXCollections.observableList(carRentalList);

        setColumnForCarTableView(carDataTableView);

        wrapEachColumnsFromCarTableView();

        carDataTableView.setItems(data);

        scroll.setMax(data.size());
    }

    private void setColumnForCarTableView(TableView<CarRental> tableView) {
        carRentalIdColumn.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        carMakeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getCarMake()));
        carModelColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getCarModel()));
        carYearColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(Integer.toString(cellData.getValue().getCar().getYear())));
        carDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCar().getDescription()));
        carRentalDateColumn.setCellValueFactory(new PropertyValueFactory<>("rentalDate"));
        carReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        carPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableView.setFixedCellSize(150);
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

    public void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getItems().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            tableView.scrollTo(t1.intValue());
        });
    }
}
