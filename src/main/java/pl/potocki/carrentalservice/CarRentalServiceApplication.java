package pl.potocki.carrentalservice;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.potocki.carrentalservice.view.application.ChartApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"pl.potocki.carrentalservice"})
public class CarRentalServiceApplication {

    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }

}
