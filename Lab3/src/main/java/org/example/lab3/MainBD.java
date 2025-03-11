package org.example.lab3;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainBD {
    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config " + e);
        }

        CarRepository carRepo = new CarsDBRepository(props);
        carRepo.add(new Car("Tesla", "Model S", 2019));
        System.out.println("Toate masinile din db");
        int id = 0;
        for (Car car : carRepo.findAll()) {
            System.out.println(car);
            id = car.getId();
        }
        String manufacturer = "Tesla";
        System.out.println("Masinile produse de " + manufacturer);
        for (Car car : carRepo.findByManufacturer(manufacturer))
            System.out.println(car);
        carRepo.delete(id);
    }
}
