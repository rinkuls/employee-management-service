-- Define sequences
CREATE SEQUENCE IF NOT EXISTS employee_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS spouse_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS kids_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS professional_details_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS past_employment_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS books_seq START WITH 1 INCREMENT BY 1;

-- Create tables without AUTO_INCREMENT, use sequence for id generation
CREATE TABLE IF NOT EXISTS employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    emp_id BIGINT,
    email VARCHAR(255),
    phone_number VARCHAR(20),
    address VARCHAR(255),
    married BOOLEAN,
    extra_martial_affair BOOLEAN,
    dream_wish VARCHAR(255),
    nature_behavior VARCHAR(255)

);

CREATE TABLE IF NOT EXISTS spouse (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    age INT,
    gender VARCHAR(10),
    current_occupation VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS kids (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    age INT,
    gender VARCHAR(10),
    profession VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS professional_details (
    id BIGINT PRIMARY KEY,
    current_company VARCHAR(255),
    current_designation VARCHAR(255),
    current_salary NUMERIC(15, 2),
    current_location VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE TABLE IF NOT EXISTS past_employment (
    id BIGINT PRIMARY KEY,
    company_name VARCHAR(255),
    designation VARCHAR(255),
    salary NUMERIC(15, 2),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);




-- Define sequence for Users table
CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 1;

-- Create Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    user_name VARCHAR(255),
    role VARCHAR(255),
    emp_id BIGINT
);
