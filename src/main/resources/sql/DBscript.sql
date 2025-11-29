Use bilabbonnementdb;

DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer (
    customer_id INT(10) PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    driver_license_nr VARCHAR(20) NOT NULL UNIQUE,
    street VARCHAR(30) NOT NULL,
    house_nr VARCHAR(10) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    city VARCHAR(30) NOT NULL,
    country VARCHAR(20) NOT NULL

);

DROP TABLE IF EXISTS Vehicle;
CREATE TABLE Vehicle (
    vin_id VARCHAR(30) PRIMARY KEY NOT NULL UNIQUE,
    registration_nr VARCHAR(30) NOT NULL UNIQUE,
    brand VARCHAR(10) NOT NULL,
    model VARCHAR(10) NOT NULL,
    model_year int(10) NOT NULL
);

DROP TABLE IF EXISTS LeaseContract;
CREATE TABLE LeaseContract (
    lease_id INT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_price DOUBLE(10, 2) NOT NULL,
    customer_id INT(10),
    vin_id VARCHAR(30) NOT NULL,
        FOREIGN KEY (vin_id) REFERENCES Vehicle (vin_id)

);


DROP TABLE IF EXISTS DamageReport;
CREATE TABLE DamageReport (
    damage_id INT (10) PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    damage_date DATE
)