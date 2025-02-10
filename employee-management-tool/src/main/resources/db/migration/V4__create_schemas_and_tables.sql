-- Ensure user_schema and employee_schema exist
CREATE SCHEMA IF NOT EXISTS user_schema;
CREATE SCHEMA IF NOT EXISTS employee_schema;

-- Create Sequences in employee_schema
CREATE SEQUENCE IF NOT EXISTS employee_schema.employee_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.spouse_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.kids_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.professional_details_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.past_employment_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.employee_leave_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.salary_structure_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS employee_schema.salary_release_seq START WITH 1 INCREMENT BY 1;

-- Create Sequence for user table
CREATE SEQUENCE IF NOT EXISTS user_schema.users_seq START WITH 1 INCREMENT BY 1;

-- Create Users Table in user_schema
CREATE TABLE IF NOT EXISTS user_schema.users (
    id BIGINT PRIMARY KEY,
    emp_Id BIGINT,
    username VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(255),
    is_default_password_changed BOOLEAN DEFAULT FALSE
);

-- Create Employee Tables in employee_schema
CREATE TABLE IF NOT EXISTS employee_schema.employee (
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

CREATE TABLE IF NOT EXISTS employee_schema.spouse (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    age INT,
    gender VARCHAR(10),
    current_occupation VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.kids (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    age INT,
    gender VARCHAR(10),
    profession VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.professional_details (
    id BIGINT PRIMARY KEY,
    current_company VARCHAR(255),
    current_designation VARCHAR(255),
    current_salary NUMERIC(15, 2),
    current_location VARCHAR(255),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.past_employment (
    id BIGINT PRIMARY KEY,
    company_name VARCHAR(255),
    designation VARCHAR(255),
    salary NUMERIC(15, 2),
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.employee_leave (
    id BIGINT PRIMARY KEY,
    total_leave_granted_for_year INT,
    leave_consumed_for_year INT,
    leave_balanced_for_year INT,
    financial_year INT,
    leave_carry_forward_for_year INT,
    allowed_leave_for_forwarding INT,
    employee_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.salary_structure (
    id BIGINT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    hra DECIMAL(10, 2),
    allowances DECIMAL(10, 2),
    deductions DECIMAL(10, 2),
    total_salary DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id)
);

CREATE TABLE IF NOT EXISTS employee_schema.salary_release (
    id BIGINT PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    salary_structure_id BIGINT NOT NULL,
    salary_month VARCHAR(7) NOT NULL,
    amount_released DECIMAL(10, 2),
    release_status VARCHAR(10),
    release_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employee_schema.employee(id),
    FOREIGN KEY (salary_structure_id) REFERENCES employee_schema.salary_structure(id)
);

-- Grant Permissions
GRANT CONNECT ON DATABASE user_db TO auth_service_user;
GRANT USAGE ON SCHEMA user_schema TO auth_service_user;
GRANT SELECT ON ALL TABLES IN SCHEMA user_schema TO auth_service_user;

GRANT CONNECT ON DATABASE user_db TO employee_service_user;
GRANT USAGE ON SCHEMA user_schema, employee_schema TO employee_service_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA user_schema, employee_schema TO employee_service_user;
