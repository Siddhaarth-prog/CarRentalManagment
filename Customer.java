
public class Customer {
    private int customerId;
    private String customerName;
    private String phone;
    private String email;

    public Customer(int customerId, String customerName,
            String phone, String email) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void displayDetails() {
        System.out.println("Customer ID: " + customerId);
        System.out.println("Name: " + customerName);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
    }
}
