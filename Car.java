
public class Car {
    private int carId;
    private String carName;
    private String brand;
    private double pricePerDay;
    private boolean available;

    public Car(int carId, String carName, String brand,
            double pricePerDay, boolean available) {
        this.carId = carId;
        this.carName = carName;
        this.brand = brand;
        this.pricePerDay = pricePerDay;
        this.available = available;
    }

    public int getCarId() {
        return carId;
    }

    public String getCarName() {
        return carName;
    }

    public String getBrand() {
        return brand;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void displayDetails() {
        System.out.println("Car ID: " + carId);
        System.out.println("Car: " + brand + " " + carName);
        System.out.println("Price per day: Rs. " + pricePerDay);
        System.out.println("Available: " + (available ? "Yes" : "No"));
    }
}
