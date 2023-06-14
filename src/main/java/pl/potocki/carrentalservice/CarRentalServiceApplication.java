package pl.potocki.carrentalservice;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import pl.potocki.carrentalservice.application.ChartApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"pl.potocki.carrentalservice"})
public class CarRentalServiceApplication {

    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }

}
