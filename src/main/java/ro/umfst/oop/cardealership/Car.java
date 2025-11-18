package ro.umfst.oop.cardealership;

public class Car extends Vehicle {
    private int doors;

    public Car(String make, String model, double price, int doors) throws InvalidPriceException {
        super(make, model, price);
        this.doors = doors;
    }

    public Car(String make, String model, double price) throws InvalidPriceException {
        super(make, model, price);
        this.doors = 4; // Default
    }

    @Override
    public String getVehicleType() {
        return "Car";
    }

    @Override
    public String getStockLabel() {
        String prefix = (make != null && make.length() >= 2) ? make.substring(0, 4) : "XXXX";
        return "CAR-" + prefix.toUpperCase();
    }

}
