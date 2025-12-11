Use bilabbonnementdb;

DROP TABLE IF EXISTS Vehicle;

CREATE TABLE Vehicle
(
    registration_no VARCHAR(30) PRIMARY KEY NOT NULL,
    vin             VARCHAR(30)             NOT NULL UNIQUE,
    brand           VARCHAR(10)             NOT NULL,
    model           VARCHAR(10)             NOT NULL,
    model_year      INT(10)                 NOT NULL

);