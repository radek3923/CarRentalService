package com.example.car_rental;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import com.example.car_rental.ChartApplication.*;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class StageInitalizer implements ApplicationListener<StageReadyEvent> {
    @Value("classpath:/chart.fxml")
    private Resource charResource;

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(charResource.getURL());
            Parent parent = fxmlLoader.load();

            Stage stage = event.getStage();
            stage.setScene(new Scene(parent, 800, 600) );
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}