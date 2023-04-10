package pl.potocki.carrentalservice.application;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentalServiceApplication {

    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }

}
