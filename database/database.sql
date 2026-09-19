-- ============================================
-- Sunrise Dental Clinic Database
-- CIS6003 Advanced Programming
-- ============================================

-- USERS TABLE
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- PATIENTS TABLE
CREATE TABLE patients (
    patient_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    date_of_birth DATE,
    gender VARCHAR(20)
);

-- DENTISTS TABLE
CREATE TABLE dentists (
    dentist_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    contact_number VARCHAR(20),
    availability VARCHAR(255)
);

-- TREATMENTS TABLE
CREATE TABLE treatments (
    treatment_id SERIAL PRIMARY KEY,
    treatment_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    treatment_cost NUMERIC(10, 2) NOT NULL CHECK (treatment_cost >= 0)
);

-- APPOINTMENTS TABLE
CREATE TABLE appointments (
    appointment_no VARCHAR(20) PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    dentist_id INTEGER NOT NULL,
    treatment_id INTEGER NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    notes VARCHAR(500),

    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(patient_id),

    CONSTRAINT fk_appointment_dentist
        FOREIGN KEY (dentist_id)
        REFERENCES dentists(dentist_id),

    CONSTRAINT fk_appointment_treatment
        FOREIGN KEY (treatment_id)
        REFERENCES treatments(treatment_id)
);

-- BILL TABLE
CREATE TABLE bills (
    bill_id SERIAL PRIMARY KEY,
    appointment_no VARCHAR(20) NOT NULL UNIQUE,
    bill_date DATE NOT NULL DEFAULT CURRENT_DATE,
    treatment_cost NUMERIC(10, 2) NOT NULL CHECK (treatment_cost >= 0),
    consultation_fee NUMERIC(10, 2) NOT NULL CHECK (consultation_fee >= 0),
    total_amount NUMERIC(10, 2) NOT NULL CHECK (total_amount >= 0),
    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    CONSTRAINT fk_bill_appointment
        FOREIGN KEY (appointment_no)
        REFERENCES appointments(appointment_no)
);

-- ============================================
-- Sunrise Dental Clinic - Initial Test Data
-- ============================================

-- STAFF USER
INSERT INTO users (username, password_hash, role, status)
VALUES ('admin', 'admin123', 'STAFF', 'ACTIVE');

-- DENTISTS
INSERT INTO dentists (full_name, specialization, contact_number, availability)
VALUES
('Dr. Nimal Perera', 'General Dentistry', '0712345678', 'Monday-Friday 09:00-17:00'),
('Dr. Sanduni Silva', 'Orthodontics', '0771234567', 'Monday-Friday 10:00-18:00');

-- TREATMENTS
INSERT INTO treatments (treatment_name, description, treatment_cost)
VALUES
('Dental Cleaning', 'Professional dental cleaning', 3500.00),
('Dental Filling', 'Tooth filling treatment', 5000.00),
('Tooth Extraction', 'Basic tooth extraction procedure', 7500.00),
('Root Canal', 'Root canal treatment', 15000.00),
('Dental Consultation', 'General dental consultation', 2000.00);

SELECT * FROM users;

SELECT * FROM dentists;

SELECT * FROM treatments;