package ro.umfst.oop.cardealership;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class DealershipController {

    // Two separate lists for the two ListViews
    private ObservableList<Vehicle> carInventory = FXCollections.observableArrayList();
    private ObservableList<Vehicle> motoInventory = FXCollections.observableArrayList();

    private CarSalesman activeSalesman = new CarSalesman("Jim Halpert", 101);

    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField priceField;
    @FXML private CheckBox isMotoCheckBox;
    @FXML private Label statusLabel;

    // Changed from one listView to two
    @FXML private ListView<Vehicle> carListView;
    @FXML private ListView<Vehicle> motoListView;

    @FXML private TextArea detailsArea;

    @FXML
    public void initialize() {
        // Link the data lists to their respective UI elements
        carListView.setItems(carInventory);
        motoListView.setItems(motoInventory);

        // Preload Data
        try {
            carInventory.add(new Car("Toyota", "Corolla", 22500.00));
            carInventory.add(new Car("Ford", "Mustang", 35000.00, 2));

            motoInventory.add(new Motorcycle("Kawasaki", "Ninja", 15000.00, false));
            motoInventory.add(new Motorcycle("Ural", "Gear Up", 18500.00, true));

        } catch (InvalidPriceException e) {
            System.err.println("Error preloading data: " + e.getMessage());
        }

        statusLabel.setText("Total Vehicles: " + Vehicle.getTotalVehicles());
    }

    @FXML
    void onAddVehiclePress() {
        try {
            String make = makeField.getText();
            String model = modelField.getText();

            if (make.isEmpty() || model.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Make and Model cannot be empty.");
                alert.show();
                return;
            }

            double price = Double.parseDouble(priceField.getText());
            Vehicle newVehicle;

            if (isMotoCheckBox.isSelected()) {
                newVehicle = new Motorcycle(make, model, price, false);
                motoInventory.add(newVehicle);
            } else {
                newVehicle = new Car(make, model, price);
                carInventory.add(newVehicle);
            }

            statusLabel.setText("Total Vehicles: " + Vehicle.getTotalVehicles());
            makeField.clear(); modelField.clear(); priceField.clear();

        } catch (InvalidPriceException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Custom Error: " + ex.getMessage());
            alert.show();
        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid numeric price.");
            alert.show();
        }
    }

    @FXML
    void onCarListSelection(MouseEvent event) {
        // Deselect the motorcycle list so only one item is selected globally
        motoListView.getSelectionModel().clearSelection();

        Vehicle selected = carListView.getSelectionModel().getSelectedItem();
        displayDetails(selected);
    }

    // Logic for when user clicks on the MOTORCYCLE list
    @FXML
    void onMotoListSelection(MouseEvent event) {
        // Deselect the car list
        carListView.getSelectionModel().clearSelection();

        Vehicle selected = motoListView.getSelectionModel().getSelectedItem();
        displayDetails(selected);
    }

    // Helper method to show info in the text area
    private void displayDetails(Vehicle selected) {
        if (selected != null) {
            String pitch = activeSalesman.makeSalesPitch(selected, 10.0);
            String info = "Stock ID: " + selected.getStockLabel() + "\n" +
                    "Full Info: " + selected.toString() + "\n" +
                    "Salesman: " + pitch;
            detailsArea.setText(info);
        } else {
            detailsArea.clear();
        }
    }

    @FXML
    void onSellVehiclePress() {
        // Check which list has a selection
        Vehicle selectedCar = carListView.getSelectionModel().getSelectedItem();
        Vehicle selectedMoto = motoListView.getSelectionModel().getSelectedItem();

        if (selectedCar == null && selectedMoto == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a vehicle to sell first.");
            alert.show();
            return;
        }

        // Remove from the correct list
        if (selectedCar != null) {
            carInventory.remove(selectedCar);
        } else {
            motoInventory.remove(selectedMoto);
        }

        // Update totals and clear UI
        Vehicle.decrementTotal();
        statusLabel.setText("Total Vehicles: " + Vehicle.getTotalVehicles());
        detailsArea.clear();

        // Clear selections
        carListView.getSelectionModel().clearSelection();
        motoListView.getSelectionModel().clearSelection();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vehicle sold successfully!");
        alert.show();
    }
}