package com.example.car_rental.service;

import com.example.car_rental.dao.CarRentalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarRentalService {
    private CarRentalRepo carRentalRepo;

    @Autowired
    public CarRentalService(CarRentalRepo carRentalRepo) {
        this.carRentalRepo = carRentalRepo;
    }
}
