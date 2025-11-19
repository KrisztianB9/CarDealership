package ro.umfst.oop.cardealership;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class DealershipController {

    private ObservableList<Vehicle> carInventory = FXCollections.observableArrayList();
    private ObservableList<Vehicle> motoInventory = FXCollections.observableArrayList();

    private CarSalesman activeSalesman = new CarSalesman("Jim Halpert", 101);

    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField priceField;
    @FXML private CheckBox isMotoCheckBox;
    @FXML private CheckBox hasSidecarCheckBox;
    @FXML private Label statusLabel;

    @FXML private ListView<Vehicle> carListView;
    @FXML private ListView<Vehicle> motoListView;

    @FXML private TextArea detailsArea;

    @FXML
    public void initialize() {
        carListView.setItems(carInventory);
        motoListView.setItems(motoInventory);
        //ha nincs becsekkolva a motor, ne lehessen a masik checkboxot hasznalni
        hasSidecarCheckBox.disableProperty().bind(isMotoCheckBox.selectedProperty().not());
        //ha kicsekkolodik a motor, kicsekkolja a masik checkboxot is
        isMotoCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (!isNowSelected) {
                hasSidecarCheckBox.setSelected(false);
            }
        });

        DatabaseHandler.initializeDB();
        refreshListsFromDB();
    }

    private void refreshListsFromDB() {
        carInventory.clear();
        motoInventory.clear();
        Vehicle.totalVehicles = 0;

        List<Vehicle> dbVehicles = DatabaseHandler.getAllVehicles();

        for (Vehicle v : dbVehicles) {
            if (v instanceof Car) {
                carInventory.add(v);
            } else {
                motoInventory.add(v);
            }
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

            if (isMotoCheckBox.isSelected()) {
                int extra = hasSidecarCheckBox.isSelected() ? 1 : 0;
                DatabaseHandler.addVehicleRaw("Motorcycle", make, model, price, extra);
            } else {
                DatabaseHandler.addVehicleRaw("Car", make, model, price, 4);
            }

            refreshListsFromDB();

            makeField.clear(); modelField.clear(); priceField.clear();
            isMotoCheckBox.setSelected(false);

        } catch (NumberFormatException nfe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter a valid numeric price.");
            alert.show();
        }
    }

    @FXML
    void onCarListSelection(MouseEvent event) {
        motoListView.getSelectionModel().clearSelection();
        Vehicle selected = carListView.getSelectionModel().getSelectedItem();
        displayDetails(selected);
    }

    @FXML
    void onMotoListSelection(MouseEvent event) {
        carListView.getSelectionModel().clearSelection();
        Vehicle selected = motoListView.getSelectionModel().getSelectedItem();
        displayDetails(selected);
    }

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
        Vehicle selectedCar = carListView.getSelectionModel().getSelectedItem();
        Vehicle selectedMoto = motoListView.getSelectionModel().getSelectedItem();

        Vehicle toDelete = (selectedCar != null) ? selectedCar : selectedMoto;

        if (toDelete == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a vehicle to sell first.");
            alert.show();
            return;
        }

        DatabaseHandler.deleteVehicle(toDelete.make, toDelete.model);
        Vehicle.decrementTotal();

        refreshListsFromDB();
        detailsArea.clear();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vehicle sold and removed from Database!");
        alert.show();
    }
}