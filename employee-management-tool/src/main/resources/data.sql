

-- Insert the default admin user if it does not already exist
INSERT INTO user_schema.users (id, emp_Id, username, password, role, is_default_password_changed)
SELECT NEXTVAL('users_seq'), 559260, 'admin', '$2a$10$OEwN2DIBsxkJqPQpTeGxKOWf7Z6bCAAsQkQ6qc0teIXNmLms6J5.G', 'ADMIN', true
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');


INSERT INTO employee_schema.employee (id, name, emp_id, email, phone_number, address, married, extra_martial_affair, dream_wish, nature_behavior)
VALUES (NEXTVAL('employee_seq'), 'A Manchanda', 38, 'Ankit.Mda@aabc.com', '1234567890', '29 Starsse, Frankfurt am Main, Germany', true, true, 'Becoming a CEO of Ankit and Sons Computers Ltd Company', 'Friendly Jolly and very Manipulative');

-- Insert spouse for the employee
INSERT INTO employee_schema.spouse (id, name, age, gender, current_occupation, employee_id)
VALUES (NEXTVAL('spouse_seq'), 'A Sharma', 30, 'Female', 'Learning German', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Insert kids for the employee
INSERT INTO employee_schema.kids (id, name, age, gender, profession, employee_id)
VALUES (NEXTVAL('kids_seq'), 'ABC', 7, 'Male', 'Student', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Insert professional details for the employee
INSERT INTO employee_schema.professional_details (id, current_company, current_designation, current_salary, current_location, employee_id)
VALUES (NEXTVAL('professional_details_seq'), 'erg GMBH', 'Senior Java Developer', 95000.00, 'Fre, Germany', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Insert past employment for the employee (corrected this to past_employment)
INSERT INTO employee_schema.past_employment (id, company_name, designation, salary, employee_id)
VALUES (NEXTVAL('past_employment_seq'), 'abc GMBH', 'Senior Java Developer', 9556000.00, (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Employee 1
INSERT INTO employee_schema.employee (id, name, emp_id, email, phone_number, address, married, extra_martial_affair, dream_wish, nature_behavior)
VALUES (NEXTVAL('employee_seq'), 'John Doe', 101, 'john.doe@example.com', '1111111111', '123 Elm Street, New York, USA', true, false, 'Travel the world', 'Friendly and hardworking');

INSERT INTO employee_schema.spouse (id, name, age, gender, current_occupation, employee_id)
VALUES (NEXTVAL('spouse_seq'), 'Jane Doe', 29, 'Female', 'Software Engineer', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.kids (id, name, age, gender, profession, employee_id)
VALUES (NEXTVAL('kids_seq'), 'Jimmy Doe', 5, 'Male', 'Student', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.professional_details (id, current_company, current_designation, current_salary, current_location, employee_id)
VALUES (NEXTVAL('professional_details_seq'), 'TechCorp Inc.', 'Team Lead', 120000.00, 'New York, USA', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.past_employment (id, company_name, designation, salary, employee_id)
VALUES (NEXTVAL('past_employment_seq'), 'Innovate Solutions', 'Developer', 80000.00, (SELECT id FROM employee ORDER BY id DESC LIMIT 1));



-- Employee 2
INSERT INTO employee_schema.employee (id, name, emp_id, email, phone_number, address, married, extra_martial_affair, dream_wish, nature_behavior)
VALUES (NEXTVAL('employee_seq'), 'Alice Smith', 102, 'alice.smith@example.com', '2222222222', '456 Oak Avenue, London, UK', true, false, 'Own a bakery', 'Creative and diligent');

INSERT INTO employee_schema.spouse (id, name, age, gender, current_occupation, employee_id)
VALUES (NEXTVAL('spouse_seq'), 'Bob Smith', 35, 'Male', 'Marketing Manager', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.kids (id, name, age, gender, profession, employee_id)
VALUES (NEXTVAL('kids_seq'), 'Sally Smith', 8, 'Female', 'Student', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.professional_details (id, current_company, current_designation, current_salary, current_location, employee_id)
VALUES (NEXTVAL('professional_details_seq'), 'Global Market Solutions', 'Senior Designer', 95000.00, 'London, UK', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.past_employment (id, company_name, designation, salary, employee_id)
VALUES (NEXTVAL('past_employment_seq'), 'Creative Minds Ltd.', 'Junior Designer', 60000.00, (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Employee 3
INSERT INTO employee_schema.employee (id, name, emp_id, email, phone_number, address, married, extra_martial_affair, dream_wish, nature_behavior)
VALUES (NEXTVAL('employee_seq'), 'Michael Brown', 103, 'michael.brown@example.com', '3333333333', '789 Pine Lane, Sydney, Australia', false, false, 'Climb Mount Everest', 'Ambitious and adventurous');

INSERT INTO employee_schema.professional_details (id, current_company, current_designation, current_salary, current_location, employee_id)
VALUES (NEXTVAL('professional_details_seq'), 'Outback Adventures', 'Tour Guide', 50000.00, 'Sydney, Australia', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.past_employment (id, company_name, designation, salary, employee_id)
VALUES (NEXTVAL('past_employment_seq'), 'Wilderness Experts', 'Assistant Guide', 40000.00, (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

-- Employee 4
INSERT INTO employee_schema.employee (id, name, emp_id, email, phone_number, address, married, extra_martial_affair, dream_wish, nature_behavior)
VALUES (NEXTVAL('employee_seq'), 'Samantha Green', 104, 'samantha.green@example.com', '4444444444', '987 Willow Way, Toronto, Canada', true, true, 'Write a novel', 'Intellectual and calm');

INSERT INTO employee_schema.spouse (id, name, age, gender, current_occupation, employee_id)
VALUES (NEXTVAL('spouse_seq'), 'Peter Green', 34, 'Male', 'Freelance Writer', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.kids (id, name, age, gender, profession, employee_id)
VALUES (NEXTVAL('kids_seq'), 'Lilly Green', 6, 'Female', 'Student', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.professional_details (id, current_company, current_designation, current_salary, current_location, employee_id)
VALUES (NEXTVAL('professional_details_seq'), 'Literary House', 'Editor', 70000.00, 'Toronto, Canada', (SELECT id FROM employee ORDER BY id DESC LIMIT 1));

INSERT INTO employee_schema.past_employment (id, company_name, designation, salary, employee_id)
VALUES (NEXTVAL('past_employment_seq'), 'Content Creators Inc.', 'Content Writer', 55000.00, (SELECT id FROM employee ORDER BY id DESC LIMIT 1));
