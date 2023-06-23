# Car Rental Service

This repository contains the source code for a Car Rental Service implemented in JavaFX. It includes controllers, services, mappers, models, and a main view represented by an XML file.

## Controllers

The application contains various controllers responsible for handling user interactions and managing the application logic. These controllers include:

- `HomeStageController`: This controller is associated with the main view and handles actions related to searching for rental cars, filtering results, and managing rental transactions.
- `CarDetailsStageController`: This controller is responsible for handling the car details view in the application, allowing for the display of car information, color selection, browsing car images, and handling interactions related to the previous and next image buttons.
- `RentalCarsStageController`: This controller is responsible for managing the rental cars stage in the application, displaying a table view of car rental history.

## Services

This project includes two services responsible for different functionalities:

- `CarService`: Responsible for retrieving car-related data and images. It communicates with external APIs to fetch car makes, models, trims, and paint combinations. It also provides methods for obtaining car images based on make, model, paint ID, and angle. Additionally, it offers functionality to filter and retrieve available car trims based on specified price ranges.

- `CarRentalService`: handles car rental operations within the application. It allows adding new car rental entries and ensures that a car can only be rented once. The service interacts with the `CarRentalRepository` to store and retrieve car rental data. It provides a method to fetch all existing car rental records.

## Models

The application includes the following models:

- `Car`: This model represents a car in the Car Rental Service. It is an entity class annotated with the `@Entity` annotation, indicating that it can be persisted in a database. The `Car` class has properties such as `id`, `carMake`, `carModel`, `year`, `description`, and `msrp`.

- `CarRental`: Represents a car rental entry. The class contains fields like rentalId, which serves as the primary key, and car, which represents the rented car. Other fields include price, rentalDate, and returnDate, representing the rental price, rental date, and return date respectively. The relationship between CarRental and Car is defined as ManyToOne using the @ManyToOne annotation, indicating that multiple car rental entries can be associated with a single car.

## Mappers

The `CarMapper` class is responsible for converting `CarTrimDto` objects to `Car` objects. It extends the `ObjectMapper` class from the Jackson library, which is used for JSON serialization and deserialization.

## Main View

The main view of the Car Rental Service is represented by an XML file called `HomeStage.fxml`. It is built using JavaFX components and defines the layout and structure of the user interface. The main view consists of the following elements:

- Date pickers for selecting rental and return dates.
- Text fields for specifying the price range of rental cars.
- Combo boxes for filtering cars by make and model.
- Buttons for searching, clearing filters, and managing rental transactions.
- Table views for displaying car data and car images.
- Labels for displaying information and rental prices.
- A progress bar indicating the loading status of the application.

## Usage

To run the Car Rental Service, ensure that you have JavaFX installed and configured properly. You can then launch the application using the main class.
