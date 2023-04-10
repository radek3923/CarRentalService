package pl.potocki.carrentalservice.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class ChartController {

    @FXML
    public Button searchButton;
    @FXML
    public TextField priceRangeFromTextField;


}
