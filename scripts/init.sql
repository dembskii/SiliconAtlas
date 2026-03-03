-- ============================================
-- CPU Management System - Database Initialization
-- ============================================

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Set encoding
SET client_encoding TO 'UTF8';


-- Example: Create initial data (optional)
INSERT INTO manufacturer (id, name, country, founding_year) VALUES 
('550e8400-e29b-41d4-a716-446655440000', 'Intel', 'USA', 1968),
('550e8400-e29b-41d4-a716-446655440001', 'AMD', 'USA', 1969);

GRANT ALL PRIVILEGES ON DATABASE cpu_management_db TO cpu_admin;
