-- Create customers_info table
CREATE TABLE customers_info (
    citizen_id CHAR(13) PRIMARY KEY CHECK (citizen_id ~ '^[0-9]{13}$'),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    mobile CHAR(10) CHECK (mobile ~ '^[0-9]{10}$'),
    pin CHAR(6) CHECK (pin ~ '^[0-9]{6}$')
);

-- Create accounts table
CREATE TABLE accounts (
    account_number CHAR(7) PRIMARY KEY CHECK (account_number ~ '^[0-9]{7}$'),
    citizen_id CHAR(13) REFERENCES customers_info(citizen_id) ON DELETE CASCADE,
    account_type VARCHAR(20),
    balance NUMERIC CHECK (balance >= 0)
);

-- Insert mock customer
INSERT INTO customers_info (citizen_id, first_name, last_name, mobile, pin)
VALUES ('1234567890000', 'John', 'Doe', '0812345678', '123456');

-- Insert mock account for that customer
INSERT INTO accounts (account_number, citizen_id, account_type, balance)
VALUES ('1000001', '1234567890000', 'saving', 10000.50);
