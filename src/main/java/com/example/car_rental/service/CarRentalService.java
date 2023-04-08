package com.example.car_rental.service;

import com.example.car_rental.dao.CarRentalRepo;
import com.example.car_rental.model.CarModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class CarRentalService {
    private CarRentalRepo carRentalRepo;
//    ObjectMapper


    @Autowired
    public CarRentalService(CarRentalRepo carRentalRepo) {
        this.carRentalRepo = carRentalRepo;
    }

    public List<CarModel> getAllCarModels(){

        return new ArrayList<>();
    }
}
