package org.example.lab3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CarsDBRepository implements CarRepository {

    private static final Logger logger = LogManager.getLogger();
    private JdbcUtils dbUtils;

    public CarsDBRepository(Properties props) {
        logger.info("Initializing CarsDBRepository with properties: {} ", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public List<Car> findByManufacturer(String manufacturerN) {
        logger.traceEntry("Finding cars by manufacturer {}", manufacturerN);
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from Masini where manufacturer = ?")) {
            preparedStatement.setString(1, manufacturerN);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    int year = resultSet.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }
        catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB findByManufacturer " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public List<Car> findBetweenYears(int min, int max) {
        logger.traceEntry("Finding cars between years {} and {}", min, max);
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from Masini where year between ? and ?")) {
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    int year = resultSet.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }

        }
        catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB findBetweenYears " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void add(Car elem) {
        logger.traceEntry("Saving element {} ", elem);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into Masini (manufacturer, model, year) values (?, ?, ?)")) {
            preparedStatement.setString(1, elem.getManufacturer());
            preparedStatement.setString(2, elem.getModel());
            preparedStatement.setInt(3, elem.getYear());
            int result = preparedStatement.executeUpdate();
            logger.traceEntry("Saved {} instances", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB adding car " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Integer integer, Car elem) {
        logger.traceEntry("Updating element with id {} ", integer);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update Masini set manufacturer = ?, model = ?, year = ? where id = ?")) {
            preparedStatement.setInt(1, integer);
            preparedStatement.setString(2, elem.getManufacturer());
            preparedStatement.setString(3, elem.getModel());
            preparedStatement.setInt(4, elem.getYear());
            int result = preparedStatement.executeUpdate();
            logger.traceEntry("Updated {} instances", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB updating car " + e);
        }
        logger.traceExit();
    }

    @Override
    public Iterable<Car> findAll() {
        logger.traceEntry("Trying to retrieve all cars from db");
        Connection connection = dbUtils.getConnection();
        List<Car> cars = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection.prepareStatement("select * from Masini")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String manufacturer = resultSet.getString("manufacturer");
                    String model = resultSet.getString("model");
                    int year = resultSet.getInt("year");
                    Car car = new Car(manufacturer, model, year);
                    car.setId(id);
                    cars.add(car);
                }
            }
        }catch (Exception e){
            logger.error(e);
            System.out.println("Error DB findAll " + e);
        }
        logger.traceExit(cars);
        return cars;
    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("Deleting element with id {} ", integer);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from Masini where id = ?")) {
            preparedStatement.setInt(1, integer);
            int result = preparedStatement.executeUpdate();
            logger.traceEntry("Deleted {} instances", result);
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB deleting car " + e);
        }
        logger.traceExit();
    }
}
