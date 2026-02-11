CREATE DATABASE IF NOT EXISTS properties_db;
USE properties_db;

CREATE TABLE properties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    size INT NOT NULL,
    description TEXT
);
