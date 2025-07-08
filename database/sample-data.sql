-- Sample data for Catering Management System
-- Run this after init.sql to populate the database with sample data

-- Insert sample customers
INSERT INTO customers (name, phone_number, email, address, customer_type) VALUES
('Rajesh Kumar', '+91-9876543210', 'rajesh.kumar@email.com', '123 MG Road, Mumbai', 'REGULAR'),
('Priya Sharma', '+91-9876543211', 'priya.sharma@email.com', '456 Park Street, Delhi', 'DIRECT'),
('Amit Patel', '+91-9876543212', 'amit.patel@email.com', '789 Brigade Road, Bangalore', 'REGULAR'),
('Sunita Gupta', '+91-9876543213', 'sunita.gupta@email.com', '321 Commercial Street, Pune', 'DIRECT'),
('Vikram Singh', '+91-9876543214', 'vikram.singh@email.com', '654 Connaught Place, Delhi', 'REGULAR');

-- Insert sample employees
INSERT INTO employees (name, role, phone_number, address, daily_rate, is_active) VALUES
('Ravi Kumar', 'COOK', '+91-9876543220', 'Sector 15, Noida', 800.00, TRUE),
('Meera Devi', 'COOK', '+91-9876543221', 'Lajpat Nagar, Delhi', 750.00, TRUE),
('Suresh Yadav', 'WAITER', '+91-9876543222', 'Karol Bagh, Delhi', 600.00, TRUE),
('Anita Kumari', 'FEMALE_WORKER', '+91-9876543223', 'Dwarka, Delhi', 550.00, TRUE),
('Ramesh Chand', 'CLEANER', '+91-9876543224', 'Rohini, Delhi', 500.00, TRUE),
('Deepak Sharma', 'TRANSPORT_DRIVER', '+91-9876543225', 'Gurgaon, Haryana', 1000.00, TRUE),
('Mohan Lal', 'BOY', '+91-9876543226', 'Faridabad, Haryana', 450.00, TRUE),
('Sanjay Kumar', 'SUPERVISOR', '+91-9876543227', 'Greater Noida', 1200.00, TRUE);

-- Insert sample resources
INSERT INTO resources (name, resource_type, unit_of_measure, unit_cost, available_quantity, minimum_stock, is_active, description) VALUES
-- Ingredients
('Basmati Rice', 'INGREDIENT', 'kg', 120.00, 50, 10, TRUE, 'Premium quality basmati rice'),
('Chicken', 'INGREDIENT', 'kg', 250.00, 25, 5, TRUE, 'Fresh chicken for non-veg dishes'),
('Paneer', 'INGREDIENT', 'kg', 300.00, 15, 3, TRUE, 'Fresh cottage cheese'),
('Dal (Mixed)', 'INGREDIENT', 'kg', 80.00, 30, 8, TRUE, 'Mixed lentils'),

-- Vegetables
('Onions', 'VEGETABLE', 'kg', 40.00, 20, 5, TRUE, 'Fresh onions'),
('Tomatoes', 'VEGETABLE', 'kg', 50.00, 15, 3, TRUE, 'Fresh tomatoes'),
('Potatoes', 'VEGETABLE', 'kg', 30.00, 25, 5, TRUE, 'Fresh potatoes'),
('Green Vegetables', 'VEGETABLE', 'kg', 60.00, 10, 2, TRUE, 'Mixed green vegetables'),

-- Kirana items
('Cooking Oil', 'KIRANA', 'liter', 150.00, 20, 5, TRUE, 'Refined cooking oil'),
('Spices Mix', 'KIRANA', 'kg', 200.00, 8, 2, TRUE, 'Mixed spices for cooking'),
('Salt', 'KIRANA', 'kg', 25.00, 10, 2, TRUE, 'Iodized salt'),
('Sugar', 'KIRANA', 'kg', 45.00, 15, 3, TRUE, 'White sugar'),

-- Equipment
('Large Cooking Pot', 'EQUIPMENT', 'piece', 2500.00, 8, 2, TRUE, 'Large aluminum cooking pot'),
('Gas Stove (4 Burner)', 'EQUIPMENT', 'piece', 8000.00, 4, 1, TRUE, 'Commercial 4-burner gas stove'),
('Pressure Cooker', 'EQUIPMENT', 'piece', 3000.00, 6, 2, TRUE, 'Large pressure cooker'),

-- Bartan (Utensils)
('Serving Plates', 'BARTAN', 'piece', 50.00, 100, 20, TRUE, 'Steel serving plates'),
('Serving Bowls', 'BARTAN', 'piece', 75.00, 80, 15, TRUE, 'Steel serving bowls'),
('Glasses', 'BARTAN', 'piece', 25.00, 150, 30, TRUE, 'Water glasses'),
('Spoons', 'BARTAN', 'piece', 15.00, 200, 40, TRUE, 'Steel spoons'),

-- Other equipment
('Display Table', 'DISPLAY_TABLE', 'piece', 1500.00, 10, 2, TRUE, 'Folding display table'),
('Water Can (20L)', 'WATER_CAN', 'piece', 300.00, 15, 3, TRUE, '20 liter water container'),
('Gas Cylinder', 'GAS_CYLINDER', 'piece', 800.00, 12, 3, TRUE, 'Commercial gas cylinder');

-- Insert sample clients
INSERT INTO clients (name, business_name, client_type, phone_number, email, address, current_balance, credit_limit, is_active, notes) VALUES
('Elegant Decorators', 'Elegant Event Decorators Pvt Ltd', 'DECORATOR', '+91-9876543230', 'contact@elegantdecorators.com', 'Nehru Place, Delhi', 15000.00, 50000.00, TRUE, 'Regular decorator partner'),
('Royal Banquet Hall', 'Royal Banquet & Convention Center', 'HALL', '+91-9876543231', 'booking@royalbanquet.com', 'Connaught Place, Delhi', -5000.00, 100000.00, TRUE, 'Premium venue partner'),
('Sound & Light Co.', 'Professional Sound & Light Services', 'SERVICE_PROVIDER', '+91-9876543232', 'info@soundlight.com', 'Karol Bagh, Delhi', 8000.00, 30000.00, TRUE, 'Audio-visual equipment provider'),
('Fresh Vegetables Supplier', 'Daily Fresh Vegetables Supply', 'VENDOR', '+91-9876543233', 'orders@freshveggies.com', 'Azadpur Mandi, Delhi', 12000.00, 25000.00, TRUE, 'Regular vegetable supplier');

-- Insert sample orders
INSERT INTO orders (customer_id, event_date, event_venue, order_type, order_status, payment_status, total_amount, advance_amount, balance_amount, guest_count, notes) VALUES
(1, CURRENT_DATE, 'Royal Banquet Hall, CP', 'FULL_CATERING', 'CONFIRMED', 'ADVANCE', 45000.00, 15000.00, 30000.00, 150, 'Wedding reception - vegetarian menu'),
(2, CURRENT_DATE + INTERVAL '1 day', 'Community Center, Dwarka', 'HALF_CATERING', 'PENDING', 'ADVANCE', 25000.00, 8000.00, 17000.00, 80, 'Birthday party - mixed menu'),
(3, CURRENT_DATE + INTERVAL '2 days', 'Hotel Grand Plaza', 'FULL_CATERING', 'CONFIRMED', 'BALANCE', 60000.00, 20000.00, 40000.00, 200, 'Corporate event - premium menu'),
(4, CURRENT_DATE + INTERVAL '3 days', 'Residence, Greater Noida', 'HALF_CATERING', 'PENDING', 'ADVANCE', 18000.00, 5000.00, 13000.00, 60, 'House warming ceremony'),
(5, CURRENT_DATE - INTERVAL '1 day', 'Banquet Hall, Gurgaon', 'FULL_CATERING', 'COMPLETED', 'COMPLETE', 55000.00, 55000.00, 0.00, 180, 'Anniversary celebration - completed successfully');

-- Insert sample employee assignments
INSERT INTO employee_assignments (employee_id, order_id, assignment_date, payment_amount, payment_status, is_present) VALUES
-- For today's order (order_id = 1)
(1, 1, CURRENT_DATE, 800.00, 'PENDING', TRUE),  -- Ravi Kumar (Cook)
(2, 1, CURRENT_DATE, 750.00, 'PENDING', TRUE),  -- Meera Devi (Cook)
(3, 1, CURRENT_DATE, 600.00, 'PENDING', TRUE),  -- Suresh Yadav (Waiter)
(4, 1, CURRENT_DATE, 550.00, 'PENDING', TRUE),  -- Anita Kumari (Female Worker)
(5, 1, CURRENT_DATE, 500.00, 'PENDING', TRUE),  -- Ramesh Chand (Cleaner)
(8, 1, CURRENT_DATE, 1200.00, 'PENDING', TRUE), -- Sanjay Kumar (Supervisor)

-- For yesterday's completed order (order_id = 5)
(1, 5, CURRENT_DATE - INTERVAL '1 day', 800.00, 'PAID', TRUE),
(3, 5, CURRENT_DATE - INTERVAL '1 day', 600.00, 'PAID', TRUE),
(4, 5, CURRENT_DATE - INTERVAL '1 day', 550.00, 'PAID', TRUE),
(6, 5, CURRENT_DATE - INTERVAL '1 day', 1000.00, 'PAID', TRUE); -- Deepak Sharma (Driver)

-- Insert sample resource assignments
INSERT INTO resource_assignments (resource_id, order_id, quantity_assigned, cost_per_unit, total_cost, is_returned, quantity_returned) VALUES
-- For today's order (order_id = 1)
(1, 1, 15, 120.00, 1800.00, FALSE, 0),  -- Basmati Rice
(4, 1, 8, 80.00, 640.00, FALSE, 0),     -- Dal (Mixed)
(5, 1, 10, 40.00, 400.00, FALSE, 0),    -- Onions
(6, 1, 8, 50.00, 400.00, FALSE, 0),     -- Tomatoes
(14, 1, 150, 50.00, 7500.00, FALSE, 0), -- Serving Plates
(15, 1, 150, 75.00, 11250.00, FALSE, 0), -- Serving Bowls
(18, 1, 5, 1500.00, 7500.00, FALSE, 0),  -- Display Tables

-- For completed order (order_id = 5) - returned items
(1, 5, 20, 120.00, 2400.00, TRUE, 18),   -- Basmati Rice (2kg used)
(14, 5, 180, 50.00, 9000.00, TRUE, 180), -- Serving Plates (all returned)
(15, 5, 180, 75.00, 13500.00, TRUE, 180); -- Serving Bowls (all returned)

-- Insert sample tasks
INSERT INTO tasks (title, description, task_date, task_status, priority_level, order_id, assigned_employee_id) VALUES
-- Today's tasks
('Prepare ingredients for wedding reception', 'Cut vegetables and prepare spices for Order #1', CURRENT_DATE, 'PENDING', 'HIGH', 1, 1),
('Setup serving area', 'Arrange tables and serving equipment at Royal Banquet Hall', CURRENT_DATE, 'PENDING', 'MEDIUM', 1, 8),
('Transport equipment to venue', 'Load and transport all required equipment to venue', CURRENT_DATE, 'PENDING', 'HIGH', 1, 6),

-- Tomorrow's tasks
('Market shopping for vegetables', 'Buy fresh vegetables for tomorrow''s event', CURRENT_DATE + INTERVAL '1 day', 'PENDING', 'MEDIUM', 2, NULL),
('Equipment maintenance', 'Check and clean all cooking equipment', CURRENT_DATE + INTERVAL '1 day', 'PENDING', 'LOW', NULL, 5),

-- Overdue task (yesterday)
('Follow up on payment', 'Collect pending payment from completed order', CURRENT_DATE - INTERVAL '1 day', 'PENDING', 'URGENT', 5, NULL),

-- Completed tasks
('Venue cleanup', 'Clean up after anniversary event', CURRENT_DATE - INTERVAL '1 day', 'COMPLETED', 'MEDIUM', 5, 5);

-- Update some resource quantities to show low stock
UPDATE resources SET available_quantity = 2 WHERE name = 'Green Vegetables';
UPDATE resources SET available_quantity = 1 WHERE name = 'Spices Mix';
UPDATE resources SET available_quantity = 25 WHERE name = 'Glasses';

-- Add some notes to orders
UPDATE orders SET notes = 'Customer prefers less spicy food. No onions in dal.' WHERE id = 1;
UPDATE orders SET notes = 'Include birthday cake arrangement. Mixed veg and non-veg items.' WHERE id = 2;

