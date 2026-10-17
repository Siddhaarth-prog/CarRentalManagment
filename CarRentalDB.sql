SHOW DATABASES;

USE CarRentalDB;

CREATE TABLE Cars (
    car_id INT PRIMARY KEY AUTO_INCREMENT,
    car_name VARCHAR(100),
    brand VARCHAR(50),
    price_per_day DECIMAL(10,2),
    available BOOLEAN DEFAULT TRUE
);
SHOW TABLES;
DESCRIBE Cars;
ALTER TABLE Cars
ADD registration_no VARCHAR(20) UNIQUE,
ADD category VARCHAR(30),
ADD fuel_type VARCHAR(20),
ADD transmission VARCHAR(20),
ADD model_year INT;
CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(100),
    license_no VARCHAR(30) UNIQUE,
    address VARCHAR(200)
);
INSERT INTO Cars
(car_name, brand, price_per_day, available,
 registration_no, category, fuel_type, transmission, model_year)
VALUES
('Swift', 'Maruti Suzuki', 1800, TRUE, 'TN01AB1001', 'Hatchback', 'Petrol', 'Manual', 2023),
('Baleno', 'Maruti Suzuki', 2200, TRUE, 'TN01AB1002', 'Hatchback', 'Petrol', 'Automatic', 2024),
('i20', 'Hyundai', 2500, TRUE, 'TN01AB1003', 'Hatchback', 'Petrol', 'Automatic', 2023),
('Creta', 'Hyundai', 3500, TRUE, 'TN01AB1004', 'SUV', 'Diesel', 'Automatic', 2024),
('Venue', 'Hyundai', 3000, TRUE, 'TN01AB1005', 'SUV', 'Petrol', 'Manual', 2023),
('Nexon', 'Tata', 2800, TRUE, 'TN01AB1006', 'SUV', 'Petrol', 'Manual', 2024),
('Nexon EV', 'Tata', 3200, TRUE, 'TN01AB1007', 'SUV', 'Electric', 'Automatic', 2024),
('Honda City', 'Honda', 3200, TRUE, 'TN01AB1008', 'Sedan', 'Petrol', 'Automatic', 2023),
('Verna', 'Hyundai', 3000, TRUE, 'TN01AB1009', 'Sedan', 'Petrol', 'Automatic', 2024),
('Innova Crysta', 'Toyota', 4500, TRUE, 'TN01AB1010', 'MUV', 'Diesel', 'Manual', 2023),
('Fortuner', 'Toyota', 7000, TRUE, 'TN01AB1011', 'SUV', 'Diesel', 'Automatic', 2024),
('Thar', 'Mahindra', 5000, TRUE, 'TN01AB1012', 'SUV', 'Diesel', 'Manual', 2024),
('XUV700', 'Mahindra', 4800, TRUE, 'TN01AB1013', 'SUV', 'Petrol', 'Automatic', 2024),
('Kia Seltos', 'Kia', 3500, TRUE, 'TN01AB1014', 'SUV', 'Petrol', 'Automatic', 2024),
('BMW 3 Series', 'BMW', 10000, TRUE, 'TN01AB1015', 'Luxury Sedan', 'Petrol', 'Automatic', 2023);
SELECT * FROM Cars;
INSERT INTO Customers
(customer_name, phone, email, license_no, address)
VALUES
('Arun Kumar', '9876543210', 'arun@gmail.com', 'DL100001', 'Chennai'),
('Priya Sharma', '9876543211', 'priya@gmail.com', 'DL100002', 'Tambaram'),
('Rahul Raj', '9876543212', 'rahul@gmail.com', 'DL100003', 'Velachery'),
('Sneha R', '9876543213', 'sneha@gmail.com', 'DL100004', 'Anna Nagar'),
('Karthik S', '9876543214', 'karthik@gmail.com', 'DL100005', 'Porur'),
('Divya M', '9876543215', 'divya@gmail.com', 'DL100006', 'Adyar'),
('Vignesh P', '9876543216', 'vignesh@gmail.com', 'DL100007', 'Chromepet'),
('Nithya K', '9876543217', 'nithya@gmail.com', 'DL100008', 'Guindy'),
('Santhosh V', '9876543218', 'santhosh@gmail.com', 'DL100009', 'Pallavaram'),
('Meera S', '9876543219', 'meera@gmail.com', 'DL100010', 'Porur'),
('Ajay Kumar', '9876543220', 'ajay@gmail.com', 'DL100011', 'Mylapore'),
('Harini R', '9876543221', 'harini@gmail.com', 'DL100012', 'T Nagar'),
('Praveen D', '9876543222', 'praveen@gmail.com', 'DL100013', 'Sholinganallur'),
('Keerthana V', '9876543223', 'keerthana@gmail.com', 'DL100014', 'Besant Nagar'),
('Manoj B', '9876543224', 'manoj@gmail.com', 'DL100015', 'Ambattur');
select * from customers;
CREATE TABLE Rentals (
    rental_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    car_id INT NOT NULL,
    rental_date DATE NOT NULL,
    return_date DATE NOT NULL,
    actual_return_date DATE,
    total_amount DECIMAL(10,2),
    rental_status VARCHAR(20) DEFAULT 'Booked',
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
);

INSERT INTO Rentals
(customer_id, car_id, rental_date, return_date,
 actual_return_date, total_amount, rental_status)
VALUES
(1, 1, '2026-10-17', '2026-10-20', NULL, 5400, 'Booked'),
(2, 3, '2026-10-17', '2026-10-19', NULL, 5000, 'Booked'),
(3, 5, '2026-10-16', '2026-10-18', NULL, 6000, 'Ongoing'),
(4, 2, '2026-10-12', '2026-10-14', '2026-10-14', 4400, 'Completed'),
(5, 4, '2026-10-13', '2026-10-15', '2026-10-15', 7000, 'Completed'),
(6, 6, '2026-10-18', '2026-10-21', NULL, 8400, 'Booked'),
(7, 7, '2026-10-14', '2026-10-16', '2026-10-16', 6400, 'Completed'),
(8, 8, '2026-10-19', '2026-10-22', NULL, 9600, 'Booked'),
(9, 9, '2026-10-15', '2026-10-17', NULL, 6000, 'Ongoing'),
(10, 10, '2026-10-11', '2026-10-13', '2026-10-13', 9000, 'Completed'),
(11, 11, '2026-10-20', '2026-10-23', NULL, 21000, 'Booked'),
(12, 12, '2026-10-16', '2026-10-18', NULL, 10000, 'Ongoing'),
(13, 13, '2026-10-21', '2026-10-24', NULL, 14400, 'Booked'),
(14, 14, '2026-10-12', '2026-10-14', '2026-10-14', 7000, 'Completed'),
(15, 15, '2026-10-22', '2026-10-25', NULL, 30000, 'Booked');
select * from Rentals;
CREATE TABLE Payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    rental_id INT NOT NULL,
    payment_date DATE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(30),
    payment_status VARCHAR(20) DEFAULT 'Pending',
    FOREIGN KEY (rental_id) REFERENCES Rentals(rental_id)
);
INSERT INTO Payments
(rental_id, payment_date, amount, payment_method, payment_status)
VALUES
(1, '2026-10-17', 5400, 'UPI', 'Paid'),
(2, '2026-10-17', 5000, 'Card', 'Paid'),
(3, '2026-10-16', 6000, 'UPI', 'Paid'),
(4, '2026-10-12', 4400, 'Cash', 'Paid'),
(5, '2026-10-13', 7000, 'Card', 'Paid'),
(6, NULL, 8400, 'UPI', 'Pending'),
(7, '2026-10-14', 6400, 'Cash', 'Paid'),
(8, NULL, 9600, 'Card', 'Pending'),
(9, '2026-10-15', 6000, 'UPI', 'Paid'),
(10, '2026-10-11', 9000, 'Card', 'Paid'),
(11, NULL, 21000, 'UPI', 'Pending'),
(12, '2026-10-16', 10000, 'Card', 'Paid'),
(13, NULL, 14400, 'UPI', 'Pending'),
(14, '2026-10-12', 7000, 'Cash', 'Paid'),
(15, NULL, 30000, 'Card', 'Pending');
select * from Payments;

USE CarRentalDB;

CREATE TABLE Maintenance (
    maintenance_id INT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    service_date DATE NOT NULL,
    description VARCHAR(200),
    cost DECIMAL(10,2) DEFAULT 0,
    maintenance_status VARCHAR(30) DEFAULT 'Scheduled',
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
);

CREATE TABLE Insurance (
    insurance_id INT PRIMARY KEY AUTO_INCREMENT,
    car_id INT NOT NULL,
    insurance_provider VARCHAR(100) NOT NULL,
    policy_number VARCHAR(50) UNIQUE NOT NULL,
    start_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    premium DECIMAL(10,2) NOT NULL,
    insurance_status VARCHAR(30) DEFAULT 'Active',
    FOREIGN KEY (car_id) REFERENCES Cars(car_id)
);

SHOW TABLES;
INSERT INTO Maintenance
(car_id, service_date, description, cost, maintenance_status)
VALUES
(1, '2026-10-10', 'Engine oil change', 2500, 'Completed'),
(2, '2026-10-11', 'Brake pad replacement', 4500, 'Completed'),
(3, '2026-10-12', 'Tyre replacement', 8000, 'Completed'),
(4, '2026-10-14', 'General servicing', 3500, 'Completed'),
(5, '2026-10-15', 'AC repair', 5000, 'Completed'),
(6, '2026-10-16', 'Battery inspection', 1200, 'Scheduled'),
(7, '2026-10-17', 'Wheel alignment', 1800, 'Scheduled'),
(8, '2026-10-18', 'Engine inspection', 3000, 'Scheduled'),
(9, '2026-10-19', 'Air filter replacement', 900, 'Scheduled'),
(10, '2026-10-20', 'Brake inspection', 1500, 'Scheduled'),
(11, '2026-10-21', 'Oil and filter change', 2800, 'Scheduled'),
(12, '2026-10-22', 'Tyre inspection', 700, 'Scheduled'),
(13, '2026-10-23', 'General servicing', 4000, 'Scheduled'),
(14, '2026-10-24', 'Battery replacement', 6500, 'Scheduled'),
(15, '2026-10-25', 'AC inspection', 1000, 'Scheduled');
INSERT INTO Insurance
(car_id, insurance_provider, policy_number, start_date,
 expiry_date, premium, insurance_status)
VALUES
(1, 'ICICI Lombard', 'INS1001', '2026-01-01', '2027-01-01', 12000, 'Active'),
(2, 'HDFC ERGO', 'INS1002', '2026-02-01', '2027-02-01', 13500, 'Active'),
(3, 'Bajaj Allianz', 'INS1003', '2026-03-01', '2027-03-01', 11000, 'Active'),
(4, 'Tata AIG', 'INS1004', '2026-01-15', '2027-01-15', 14000, 'Active'),
(5, 'New India Assurance', 'INS1005', '2026-04-01', '2027-04-01', 12500, 'Active'),
(6, 'ICICI Lombard', 'INS1006', '2026-05-01', '2027-05-01', 15000, 'Active'),
(7, 'HDFC ERGO', 'INS1007', '2025-10-01', '2026-10-20', 10500, 'Active'),
(8, 'Bajaj Allianz', 'INS1008', '2026-06-01', '2027-06-01', 16000, 'Active'),
(9, 'Tata AIG', 'INS1009', '2026-07-01', '2027-07-01', 11500, 'Active'),
(10, 'New India Assurance', 'INS1010', '2026-08-01', '2027-08-01', 14500, 'Active'),
(11, 'ICICI Lombard', 'INS1011', '2026-09-01', '2027-09-01', 18000, 'Active'),
(12, 'HDFC ERGO', 'INS1012', '2026-01-10', '2027-01-10', 13000, 'Active'),
(13, 'Bajaj Allianz', 'INS1013', '2026-02-15', '2027-02-15', 17000, 'Active'),
(14, 'Tata AIG', 'INS1014', '2025-10-01', '2026-10-15', 10000, 'Expired'),
(15, 'New India Assurance', 'INS1015', '2026-09-15', '2027-09-15', 20000, 'Active');
SELECT * FROM Maintenance;

SELECT * FROM Insurance;


