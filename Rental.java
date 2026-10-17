
public class Rental {
    private int rentalId;
    private int customerId;
    private int carId;
    private String rentalDate;
    private String returnDate;
    private double totalAmount;
    private String rentalStatus;

    public Rental(int rentalId, int customerId, int carId,
            String rentalDate, String returnDate,
            double totalAmount, String rentalStatus) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.carId = carId;
        this.rentalDate = rentalDate;
        this.returnDate = returnDate;
        this.totalAmount = totalAmount;
        this.rentalStatus = rentalStatus;
    }

    public int getRentalId() {
        return rentalId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCarId() {
        return carId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void displayDetails() {
        System.out.println("Rental ID: " + rentalId);
        System.out.println("Customer ID: " + customerId);
        System.out.println("Car ID: " + carId);
        System.out.println("Rental Date: " + rentalDate);
        System.out.println("Return Date: " + returnDate);
        System.out.println("Total Amount: Rs. " + totalAmount);
        System.out.println("Status: " + rentalStatus);
    }
}
