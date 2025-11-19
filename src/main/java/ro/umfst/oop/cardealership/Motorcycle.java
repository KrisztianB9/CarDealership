package ro.umfst.oop.cardealership;

public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle(String make, String model, double price, boolean hasSidecar) throws InvalidPriceException {
        super(make, model, price);
        this.hasSidecar = hasSidecar;
    }

    public int getHasSidecar() {
        if (hasSidecar) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getVehicleType() {

        return "Motorcycle";
    }

    @Override
    public String getStockLabel() {
        String prefix = (make != null && make.length() >= 2) ? make.substring(0, 3) : "XXX";
        return "MOTO-" + prefix.toUpperCase();
    }
}
