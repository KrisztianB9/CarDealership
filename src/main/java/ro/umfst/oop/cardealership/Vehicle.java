package ro.umfst.oop.cardealership;

public abstract class Vehicle implements Stockable {
    protected String make;
    protected String model;
    protected double price;

    public static int totalVehicles = 0;

    public Vehicle(String make, String model, double price) throws InvalidPriceException {
        if (price < 0) {
            throw new InvalidPriceException("Price cannot be negative!");
        }
        this.make = make;
        this.model = model;
        this.price = price;

        totalVehicles++;
    }

    public static int getTotalVehicles() {

        return totalVehicles;
    }

    public static void decrementTotal() {
        if (totalVehicles > 0) {
            totalVehicles--;
        }
    }

    public abstract String getVehicleType();

    public double getPrice() {
        return price; }

    @Override
    public String toString() {

        return getVehicleType() + ": " + make + " " + model + " ($" + price + ")";
    }
}
