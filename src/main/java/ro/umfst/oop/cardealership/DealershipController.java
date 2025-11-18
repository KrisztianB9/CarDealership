package ro.umfst.oop.cardealership;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class DealershipController {

    private ObservableList<Vehicle> vehicleInventory = FXCollections.observableArrayList();
    private CarSalesman activeSalesman = new CarSalesman("Jim Halpert", 101);

    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField priceField;
    @FXML private CheckBox isMotoCheckBox;
    @FXML private Label statusLabel;
    @FXML private ListView<Vehicle> listView;
    @FXML private TextArea detailsArea;

    @FXML
    public void initialize() {
        listView.setItems(vehicleInventory);

        try {
            vehicleInventory.add(new Car("Ford", "Mondeo", 5500.00));
            vehicleInventory.add(new Car("Mini", "Cooper", 3000.00, 2));
            vehicleInventory.add(new Motorcycle("Kawasaki", "Ninja", 15000.00, false));
            vehicleInventory.add(new Motorcycle("Ural", "Gear Up", 18500.00, true));

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
            } else {
                newVehicle = new Car(make, model, price);
            }

            vehicleInventory.add(newVehicle);
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
    void onListSelection(MouseEvent event) {
        Vehicle selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String pitch = activeSalesman.makeSalesPitch(selected, 5.0);
            String info = "Stock ID: " + selected.getStockLabel() + "\n" +
                    "Full Info: " + selected.toString() + "\n" +
                    "Salesman: " + pitch;
            detailsArea.setText(info);
        }
    }

    @FXML
    void onSellVehiclePress() {
        Vehicle selected = listView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a vehicle to sell first.");
            alert.show();
            return;
        }
        vehicleInventory.remove(selected);
        Vehicle.decrementTotal();
        statusLabel.setText("Total Vehicles: " + Vehicle.getTotalVehicles());
        detailsArea.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vehicle sold successfully!");
        alert.show();
    }
}