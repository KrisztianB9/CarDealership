package ro.umfst.oop.cardealership;

public class CarSalesman extends Employee {
    public CarSalesman(String name, int id) {
        super(name, id);
    }

    public String makeSalesPitch(Vehicle v) {
        return "Hello! Would you like to buy this " + v.make + "?";
    }

    public String makeSalesPitch(Vehicle v, double discount) {
        double newPrice = v.getPrice() * (1 - (discount/100));
        return "Special offer! This " + v.make + " is yours for only $" + String.format("%.2f", newPrice);
    }
}
