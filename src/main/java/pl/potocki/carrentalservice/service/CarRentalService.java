package pl.potocki.carrentalservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.potocki.carrentalservice.dao.CarRentalDao;
import pl.potocki.carrentalservice.model.CarModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarRentalService {
    private CarRentalDao carRentalRepo;
    ObjectMapper mapper = new ObjectMapper();


    @Autowired
    public CarRentalService(CarRentalDao carRentalRepo) {
        this.carRentalRepo = carRentalRepo;
    }

    @SneakyThrows
    public List<CarModel> getAllCarModels() {
        URL urlToApi = new URL("https://carapi.app/api/models?year=2020");
        JsonNode jsonNode = mapper.readTree(urlToApi);

        System.out.println(jsonNode.get("data"));

        return new ArrayList<>();
    }
}
