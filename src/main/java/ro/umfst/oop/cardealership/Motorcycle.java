package ro.umfst.oop.cardealership;

public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle(String make, String model, double price, boolean hasSidecar) throws InvalidPriceException {
        super(make, model, price);
        this.hasSidecar = hasSidecar;
    }

    @Override
    public String getVehicleType() {

        return "Motorcycle";
    }

    @Override
    public String getStockLabel() {
        String prefix = (make != null && make.length() >= 2) ? make.substring(0, 4) : "XXXX";
        return "MOTO-" + prefix.toUpperCase();
    }
}
