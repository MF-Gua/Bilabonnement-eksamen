Use bilabbonnementdb;

DROP TABLE IF EXISTS DamageReport;
CREATE TABLE DamageReport (
    damage_id INT (10) PRIMARY KEY AUTO_INCREMENT NOT NULL UNIQUE,
    damage_date DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    repair_cost DOUBLE NOT NULL,
    FOREIGN KEY (vin_id) REFERENCES Vehicles (vin_id) VARCHAR(30)
    FOREIGN KEY (employee_id) REFERENCES Employess (employee_id) int(10) NOT NULL,
    FOREIGN KEY (lease_id) REFERENCES LeaseContract (lease_id) int(10) NOT NULL,
    km_slut int(10) NOT NULL
);