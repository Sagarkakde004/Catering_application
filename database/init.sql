-- Catering Management System Database Initialization Script
-- PostgreSQL Database Setup

-- Create database (run this as postgres superuser)
-- CREATE DATABASE catering_db;
-- CREATE USER catering_user WITH PASSWORD 'catering_pass';
-- GRANT ALL PRIVILEGES ON DATABASE catering_db TO catering_user;

-- Connect to catering_db and run the following:

-- Enable UUID extension (optional, for future use)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create sequences for auto-increment IDs
CREATE SEQUENCE IF NOT EXISTS customers_id_seq;
CREATE SEQUENCE IF NOT EXISTS orders_id_seq;
CREATE SEQUENCE IF NOT EXISTS employees_id_seq;
CREATE SEQUENCE IF NOT EXISTS employee_assignments_id_seq;
CREATE SEQUENCE IF NOT EXISTS resources_id_seq;
CREATE SEQUENCE IF NOT EXISTS resource_assignments_id_seq;
CREATE SEQUENCE IF NOT EXISTS tasks_id_seq;
CREATE SEQUENCE IF NOT EXISTS clients_id_seq;

-- Create ENUM types
CREATE TYPE customer_type AS ENUM ('DIRECT', 'REGULAR');
CREATE TYPE order_type AS ENUM ('FULL_CATERING', 'HALF_CATERING');
CREATE TYPE order_status AS ENUM ('PENDING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE payment_status AS ENUM ('ADVANCE', 'BALANCE', 'COMPLETE');
CREATE TYPE employee_role AS ENUM ('COOK', 'WAITER', 'FEMALE_WORKER', 'CLEANER', 'TRANSPORT_DRIVER', 'BOY', 'SUPERVISOR', 'MANAGER');
CREATE TYPE assignment_payment_status AS ENUM ('PENDING', 'PAID');
CREATE TYPE resource_type AS ENUM ('INGREDIENT', 'VEGETABLE', 'KIRANA', 'EQUIPMENT', 'BARTAN', 'DISPLAY_TABLE', 'WATER_CAN', 'GAS_CYLINDER', 'OTHER');
CREATE TYPE task_status AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED');
CREATE TYPE priority_level AS ENUM ('LOW', 'MEDIUM', 'HIGH', 'URGENT');
CREATE TYPE client_type AS ENUM ('DECORATOR', 'SERVICE_PROVIDER', 'HALL', 'VENDOR', 'OTHER');

-- Create tables

-- Customers table
CREATE TABLE customers (
    id BIGINT PRIMARY KEY DEFAULT nextval('customers_id_seq'),
    name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    customer_type customer_type DEFAULT 'DIRECT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id BIGINT PRIMARY KEY DEFAULT nextval('orders_id_seq'),
    customer_id BIGINT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    event_date DATE NOT NULL,
    event_venue VARCHAR(500) NOT NULL,
    order_type order_type NOT NULL,
    order_status order_status DEFAULT 'PENDING',
    payment_status payment_status DEFAULT 'ADVANCE',
    total_amount DECIMAL(10,2),
    advance_amount DECIMAL(10,2),
    balance_amount DECIMAL(10,2),
    guest_count INTEGER,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employees table
CREATE TABLE employees (
    id BIGINT PRIMARY KEY DEFAULT nextval('employees_id_seq'),
    name VARCHAR(255) NOT NULL,
    role employee_role NOT NULL,
    phone_number VARCHAR(20),
    address TEXT,
    daily_rate DECIMAL(8,2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Employee assignments table
CREATE TABLE employee_assignments (
    id BIGINT PRIMARY KEY DEFAULT nextval('employee_assignments_id_seq'),
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    assignment_date DATE NOT NULL,
    payment_amount DECIMAL(8,2),
    payment_status assignment_payment_status DEFAULT 'PENDING',
    is_present BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Resources table
CREATE TABLE resources (
    id BIGINT PRIMARY KEY DEFAULT nextval('resources_id_seq'),
    name VARCHAR(255) NOT NULL,
    resource_type resource_type NOT NULL,
    unit_of_measure VARCHAR(50),
    unit_cost DECIMAL(8,2),
    available_quantity INTEGER DEFAULT 0,
    minimum_stock INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Resource assignments table
CREATE TABLE resource_assignments (
    id BIGINT PRIMARY KEY DEFAULT nextval('resource_assignments_id_seq'),
    resource_id BIGINT NOT NULL REFERENCES resources(id) ON DELETE CASCADE,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    quantity_assigned INTEGER NOT NULL,
    cost_per_unit DECIMAL(8,2),
    total_cost DECIMAL(10,2),
    is_returned BOOLEAN DEFAULT FALSE,
    quantity_returned INTEGER DEFAULT 0,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tasks table
CREATE TABLE tasks (
    id BIGINT PRIMARY KEY DEFAULT nextval('tasks_id_seq'),
    title VARCHAR(255) NOT NULL,
    description TEXT,
    task_date DATE NOT NULL,
    task_status task_status DEFAULT 'PENDING',
    priority_level priority_level DEFAULT 'MEDIUM',
    order_id BIGINT REFERENCES orders(id) ON DELETE SET NULL,
    assigned_employee_id BIGINT REFERENCES employees(id) ON DELETE SET NULL,
    completed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clients table
CREATE TABLE clients (
    id BIGINT PRIMARY KEY DEFAULT nextval('clients_id_seq'),
    name VARCHAR(255) NOT NULL,
    business_name VARCHAR(255),
    client_type client_type,
    phone_number VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    current_balance DECIMAL(10,2) DEFAULT 0,
    credit_limit DECIMAL(10,2) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_customers_phone ON customers(phone_number);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_type ON customers(customer_type);

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_event_date ON orders(event_date);
CREATE INDEX idx_orders_status ON orders(order_status);
CREATE INDEX idx_orders_payment_status ON orders(payment_status);
CREATE INDEX idx_orders_type ON orders(order_type);

CREATE INDEX idx_employees_role ON employees(role);
CREATE INDEX idx_employees_active ON employees(is_active);

CREATE INDEX idx_employee_assignments_employee ON employee_assignments(employee_id);
CREATE INDEX idx_employee_assignments_order ON employee_assignments(order_id);
CREATE INDEX idx_employee_assignments_date ON employee_assignments(assignment_date);
CREATE INDEX idx_employee_assignments_payment_status ON employee_assignments(payment_status);

CREATE INDEX idx_resources_type ON resources(resource_type);
CREATE INDEX idx_resources_active ON resources(is_active);
CREATE INDEX idx_resources_low_stock ON resources(available_quantity, minimum_stock);

CREATE INDEX idx_resource_assignments_resource ON resource_assignments(resource_id);
CREATE INDEX idx_resource_assignments_order ON resource_assignments(order_id);
CREATE INDEX idx_resource_assignments_returned ON resource_assignments(is_returned);

CREATE INDEX idx_tasks_status ON tasks(task_status);
CREATE INDEX idx_tasks_date ON tasks(task_date);
CREATE INDEX idx_tasks_priority ON tasks(priority_level);
CREATE INDEX idx_tasks_order ON tasks(order_id);
CREATE INDEX idx_tasks_employee ON tasks(assigned_employee_id);

CREATE INDEX idx_clients_type ON clients(client_type);
CREATE INDEX idx_clients_active ON clients(is_active);
CREATE INDEX idx_clients_balance ON clients(current_balance);

-- Create triggers for updated_at timestamps
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_customers_updated_at BEFORE UPDATE ON customers FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_employees_updated_at BEFORE UPDATE ON employees FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_employee_assignments_updated_at BEFORE UPDATE ON employee_assignments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_resources_updated_at BEFORE UPDATE ON resources FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_resource_assignments_updated_at BEFORE UPDATE ON resource_assignments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_tasks_updated_at BEFORE UPDATE ON tasks FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_clients_updated_at BEFORE UPDATE ON clients FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Grant permissions to catering_user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO catering_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO catering_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO catering_user;

