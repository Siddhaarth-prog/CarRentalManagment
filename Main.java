
import java.sql.*;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try (Connection con = DBConnection.getConnection()) {

            int choice;

            do {
                System.out.println("\n===== CAR RENTAL MANAGEMENT SYSTEM =====");
                System.out.println("1. View Customers");
                System.out.println("2. Add Customer");
                System.out.println("3. View Available Cars");
                System.out.println("4. Book a Car");
                System.out.println("5. Return a Car");
                System.out.println("6. Billing and Payments");
                System.out.println("7. View Maintenance Records");
                System.out.println("8. Schedule Maintenance");
                System.out.println("9. Update Maintenance Status");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");

                try {
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number!");
                    choice = -1;
                    continue;
                }

                try {
                    switch (choice) {
                        case 1:
                            viewCustomers(con);
                            break;
                        case 2:
                            addCustomer(con, sc);
                            break;
                        case 3:
                            viewAvailableCars(con);
                            break;
                        case 4:
                            bookCar(con, sc);
                            break;
                        case 5:
                            returnCar(con, sc);
                            break;
                        case 6:
                            billingAndPayments(con, sc);
                            break;
                        case 7:
                            viewMaintenance(con);
                            break;
                        case 8:
                            scheduleMaintenance(con, sc);
                            break;
                        case 9:
                            updateMaintenanceStatus(con, sc);
                            break;
                        case 0:
                            System.out.println("Thank you for using Car Rental Management System!");
                            break;
                        default:
                            System.out.println("Invalid choice!");
                    }
                } catch (Exception e) {
                    System.out.println("Operation failed: " + e.getMessage());
                }

            } while (choice != 0);

        } catch (Exception e) {
            System.out.println("Database connection error: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    static void viewCustomers(Connection con) throws Exception {
        String query = "SELECT customer_id, customer_name, phone, email FROM Customers";

        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            System.out.println("\n----- CUSTOMER DETAILS -----");

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("email"));
                customer.displayDetails();
                System.out.println("--------------------");
            }
        }
    }

    static void addCustomer(Connection con, Scanner sc) throws Exception {
        System.out.println("Enter customer name:");
        String name = sc.nextLine();

        System.out.println("Enter phone number:");
        String phone = sc.nextLine();

        System.out.println("Enter email:");
        String email = sc.nextLine();

        System.out.println("Enter driving licence number:");
        String license = sc.nextLine();

        System.out.println("Enter address:");
        String address = sc.nextLine();

        String query = "INSERT INTO Customers " +
                "(customer_name, phone, email, license_no, address) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, license);
            ps.setString(5, address);

            int rows = ps.executeUpdate();

            System.out.println(rows > 0
                    ? "Customer added successfully!"
                    : "Failed to add customer.");
        }
    }

    static void viewAvailableCars(Connection con) throws Exception {
        String query = "SELECT car_id, car_name, brand, price_per_day " +
                "FROM Cars WHERE available = TRUE";

        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== AVAILABLE CARS =====");

            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("car_id"),
                        rs.getString("car_name"),
                        rs.getString("brand"),
                        rs.getDouble("price_per_day"),
                        true);

                car.displayDetails();
                System.out.println("--------------------");
            }
        }
    }

    static void bookCar(Connection con, Scanner sc) throws Exception {
        System.out.println("\n----- CAR BOOKING -----");
        viewCustomers(con);

        System.out.println("Enter customer ID:");
        int customerId = Integer.parseInt(sc.nextLine());

        viewAvailableCars(con);
        System.out.println("Enter car ID to book:");
        int carId = Integer.parseInt(sc.nextLine());

        System.out.println("Enter rental date (YYYY-MM-DD):");
        Date rentalDate = Date.valueOf(sc.nextLine());

        System.out.println("Enter return date (YYYY-MM-DD):");
        Date returnDate = Date.valueOf(sc.nextLine());

        if (!returnDate.after(rentalDate)) {
            System.out.println("Return date must be after rental date.");
            return;
        }

        con.setAutoCommit(false);

        try {
            String carQuery = "SELECT price_per_day, available FROM Cars " +
                    "WHERE car_id = ? FOR UPDATE";
            double price;

            try (PreparedStatement ps = con.prepareStatement(carQuery)) {
                ps.setInt(1, carId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || !rs.getBoolean("available")) {
                        System.out.println("Car not available!");
                        con.rollback();
                        return;
                    }
                    price = rs.getDouble("price_per_day");
                }
            }

            long days = ChronoUnit.DAYS.between(
                    rentalDate.toLocalDate(), returnDate.toLocalDate());
            double totalAmount = days * price;

            String bookingQuery = "INSERT INTO Rentals " +
                    "(customer_id, car_id, rental_date, return_date, total_amount, rental_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(bookingQuery)) {
                ps.setInt(1, customerId);
                ps.setInt(2, carId);
                ps.setDate(3, rentalDate);
                ps.setDate(4, returnDate);
                ps.setDouble(5, totalAmount);
                ps.setString(6, "Booked");
                ps.executeUpdate();
            }

            String updateCar = "UPDATE Cars SET available = FALSE WHERE car_id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateCar)) {
                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            System.out.println("Car booked successfully!");
            Rental rental = new Rental(
                    0,
                    customerId,
                    carId,
                    rentalDate.toString(),
                    returnDate.toString(),
                    totalAmount,
                    "Booked");
            rental.displayDetails();

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    static void returnCar(Connection con, Scanner sc) throws Exception {
        System.out.println("\n----- ACTIVE BOOKINGS -----");

        String query = "SELECT rental_id, customer_id, car_id, return_date " +
                "FROM Rentals WHERE rental_status IN ('Booked', 'Ongoing')";

        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(
                        "Rental ID: " + rs.getInt("rental_id")
                                + " | Customer ID: " + rs.getInt("customer_id")
                                + " | Car ID: " + rs.getInt("car_id")
                                + " | Due date: " + rs.getDate("return_date"));
            }
        }

        System.out.println("Enter rental ID to return:");
        int rentalId = Integer.parseInt(sc.nextLine());

        con.setAutoCommit(false);

        try {
            int carId;
            String checkQuery = "SELECT car_id FROM Rentals WHERE rental_id = ? " +
                    "AND rental_status IN ('Booked', 'Ongoing') FOR UPDATE";

            try (PreparedStatement ps = con.prepareStatement(checkQuery)) {
                ps.setInt(1, rentalId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("Invalid rental ID or car already returned.");
                        con.rollback();
                        return;
                    }
                    carId = rs.getInt("car_id");
                }
            }

            String updateRental = "UPDATE Rentals SET actual_return_date = CURRENT_DATE, " +
                    "rental_status = 'Completed' WHERE rental_id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateRental)) {
                ps.setInt(1, rentalId);
                ps.executeUpdate();
            }

            String updateCar = "UPDATE Cars SET available = TRUE WHERE car_id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateCar)) {
                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            con.commit();
            System.out.println("Car returned successfully!");
            System.out.println("Rental status: Completed");
            System.out.println("Car is now available for booking.");

        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    static void billingAndPayments(Connection con, Scanner sc) throws Exception {
        System.out.println("\n----- BILLING AND PAYMENTS -----");

        String query = "SELECT r.rental_id, c.customer_name, ca.brand, ca.car_name, " +
                "r.rental_date, r.return_date, r.total_amount, r.rental_status " +
                "FROM Rentals r " +
                "JOIN Customers c ON r.customer_id = c.customer_id " +
                "JOIN Cars ca ON r.car_id = ca.car_id " +
                "ORDER BY r.rental_id";

        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.println(
                        "Rental ID: " + rs.getInt("rental_id")
                                + " | Customer: " + rs.getString("customer_name")
                                + " | Car: " + rs.getString("brand") + " " + rs.getString("car_name")
                                + " | Dates: " + rs.getDate("rental_date") + " to " +
                                rs.getDate("return_date")
                                + " | Bill: Rs. " + rs.getDouble("total_amount")
                                + " | Status: " + rs.getString("rental_status"));
            }
        }

        System.out.println("\nEnter rental ID to record payment:");
        int rentalId = Integer.parseInt(sc.nextLine());

        double amount;
        String paymentStatus;

        String billQuery = "SELECT total_amount FROM Rentals WHERE rental_id = ?";
        try (PreparedStatement ps = con.prepareStatement(billQuery)) {
            ps.setInt(1, rentalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Invalid rental ID!");
                    return;
                }
                amount = rs.getDouble("total_amount");
            }
        }

        System.out.println("Bill amount: Rs. " + amount);
        System.out.println("Enter payment method (Cash/UPI/Card):");
        String method = sc.nextLine();

        System.out.println("Enter payment status (Paid/Pending):");
        paymentStatus = sc.nextLine();

        String paymentQuery = "INSERT INTO Payments " +
                "(rental_id, payment_date, amount, payment_method, payment_status) " +
                "VALUES (?, CURRENT_DATE, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(paymentQuery)) {
            ps.setInt(1, rentalId);
            ps.setDouble(2, amount);
            ps.setString(3, method);
            ps.setString(4, paymentStatus);

            if (ps.executeUpdate() > 0) {
                System.out.println("\nPayment record saved successfully!");
                System.out.println("Rental ID: " + rentalId);
                System.out.println("Amount: Rs. " + amount);
                System.out.println("Method: " + method);
                System.out.println("Status: " + paymentStatus);
            }
        }
    }

    static void viewMaintenance(Connection con) throws Exception {
        String query = "SELECT maintenance_id, car_id, service_date, " +
                "description, cost, maintenance_status FROM Maintenance " +
                "ORDER BY maintenance_id";

        try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            System.out.println("\n===== MAINTENANCE RECORDS =====");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Maintenance ID: " + rs.getInt("maintenance_id") +
                                " | Car ID: " + rs.getInt("car_id") +
                                " | Date: " + rs.getDate("service_date") +
                                " | Description: " + rs.getString("description") +
                                " | Cost: Rs. " + rs.getDouble("cost") +
                                " | Status: " + rs.getString("maintenance_status"));
            }

            if (!found) {
                System.out.println("No maintenance records found.");
            }
        }
    }

    static void scheduleMaintenance(Connection con, Scanner sc) throws Exception {
        System.out.println("\n===== SCHEDULE MAINTENANCE =====");

        viewAvailableCars(con);

        System.out.println("Enter car ID:");
        int carId = Integer.parseInt(sc.nextLine());

        System.out.println("Enter service date (YYYY-MM-DD):");
        Date serviceDate = Date.valueOf(sc.nextLine());

        System.out.println("Enter maintenance description:");
        String description = sc.nextLine();

        System.out.println("Enter maintenance cost:");
        double cost = Double.parseDouble(sc.nextLine());

        String query = "INSERT INTO Maintenance " +
                "(car_id, service_date, description, cost, maintenance_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, carId);
            ps.setDate(2, serviceDate);
            ps.setString(3, description);
            ps.setDouble(4, cost);
            ps.setString(5, "Scheduled");

            if (ps.executeUpdate() > 0) {
                System.out.println("Maintenance scheduled successfully!");
                String updateCar = "UPDATE Cars SET available = FALSE WHERE car_id = ?";
                try (PreparedStatement updatePs = con.prepareStatement(updateCar)) {
                    updatePs.setInt(1, carId);
                    updatePs.executeUpdate();
                }
            }
        }
    }

    static void updateMaintenanceStatus(Connection con, Scanner sc) throws Exception {
        viewMaintenance(con);

        System.out.println("\nEnter maintenance ID:");
        int maintenanceId = Integer.parseInt(sc.nextLine());

        System.out.println("Enter new status (Scheduled/In Progress/Completed):");
        String status = sc.nextLine().trim();

        if (!status.equalsIgnoreCase("Scheduled")
                && !status.equalsIgnoreCase("In Progress")
                && !status.equalsIgnoreCase("Completed")) {
            System.out.println("Invalid status!");
            return;
        }

        String query = "UPDATE Maintenance SET maintenance_status = ? " +
                "WHERE maintenance_id = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, maintenanceId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                System.out.println("Maintenance ID not found.");
                return;
            }
        }

        if (status.equalsIgnoreCase("Completed")) {
            String updateCar = "UPDATE Cars SET available = TRUE " +
                    "WHERE car_id = (" +
                    "SELECT car_id FROM Maintenance WHERE maintenance_id = ?) " +
                    "AND NOT EXISTS (" +
                    "SELECT 1 FROM Maintenance " +
                    "WHERE car_id = (" +
                    "SELECT car_id FROM Maintenance WHERE maintenance_id = ?) " +
                    "AND maintenance_status IN ('Scheduled', 'In Progress'))";

            try (PreparedStatement carPs = con.prepareStatement(updateCar)) {
                carPs.setInt(1, maintenanceId);
                carPs.setInt(2, maintenanceId);
                carPs.executeUpdate();
            }
        } else {
            String updateCar = "UPDATE Cars SET available = FALSE " +
                    "WHERE car_id = (" +
                    "SELECT car_id FROM Maintenance WHERE maintenance_id = ?)";

            try (PreparedStatement carPs = con.prepareStatement(updateCar)) {
                carPs.setInt(1, maintenanceId);
                carPs.executeUpdate();
            }
        }

        System.out.println("Maintenance status updated successfully!");
    }
}
