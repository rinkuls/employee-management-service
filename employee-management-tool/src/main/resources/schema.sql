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

CREATE SEQUENCE IF NOT EXISTS employee_leave_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS employee_leave (
    id BIGINT PRIMARY KEY,
    total_leave_granted_for_year INT,
    leave_consumed_for_year INT,
    leave_balanced_for_year INT,
    financial_year INT,
    leave_carry_forward_for_year INT,
    allowed_leave_for_forwarding INT,
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);
CREATE SEQUENCE IF NOT EXISTS salary_structure_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS SalaryStructure (
    id BIGINT PRIMARY KEY ,
    employee_id BIGINT NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    hra DECIMAL(10, 2),
    allowances DECIMAL(10, 2),
    deductions DECIMAL(10, 2),
    total_salary DECIMAL(10, 2) ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id)
);

CREATE SEQUENCE IF NOT EXISTS salary_release_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE IF NOT EXISTS SalaryRelease (
    id BIGINT PRIMARY KEY ,
    employee_id BIGINT NOT NULL,
    salary_structure_id BIGINT NOT NULL,
    salary_month VARCHAR(7) NOT NULL, -- For tracking monthly salary releases
    amount_released DECIMAL(10, 2),
    release_status VARCHAR(10),
    release_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee(id),
    FOREIGN KEY (salary_structure_id) REFERENCES SalaryStructure(id)
);



-- Add a trigger for auto-updating updated_at column
--CREATE OR REPLACE FUNCTION update_timestamp()
--RETURNS TRIGGER AS $$
--BEGIN
  --  NEW.updated_at = CURRENT_TIMESTAMP;
    --RETURN NEW;
--END;
--$$ LANGUAGE plpgsql;

--CREATE TRIGGER update_salary_structure_timestamp
--BEFORE UPDATE ON SalaryStructure
--FOR EACH ROW
--EXECUTE FUNCTION update_timestamp();