-- ============================================
-- CPU Management System - Database Initialization
-- ============================================

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Set encoding
SET client_encoding TO 'UTF8';

-- ============================================
-- Users Table
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('ADMIN', 'USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on username for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- ============================================
-- Manufacturer Table
-- ============================================
CREATE TABLE IF NOT EXISTS manufacturer (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    country VARCHAR(50) NOT NULL,
    founded_year INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_manufacturer_name ON manufacturer(name);

-- ============================================
-- Technology Table
-- ============================================
CREATE TABLE IF NOT EXISTS technology (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    release_year INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_technology_name ON technology(name);

-- ============================================
-- CPU Specification Table
-- ============================================
CREATE TABLE IF NOT EXISTS cpu_specification (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    cache_l1kb INTEGER NOT NULL,
    cache_l2kb INTEGER NOT NULL,
    cache_l3mb INTEGER NOT NULL,
    tdp_watts INTEGER NOT NULL,
    socket_type VARCHAR(50) NOT NULL
);

-- ============================================
-- CPU Table
-- ============================================
CREATE TABLE IF NOT EXISTS cpu (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    model VARCHAR(100) NOT NULL UNIQUE,
    cores INTEGER NOT NULL,
    threads INTEGER NOT NULL,
    frequency_ghz DOUBLE PRECISION NOT NULL,
    manufacturer_id UUID NOT NULL REFERENCES manufacturer(id) ON DELETE CASCADE,
    specification_id UUID UNIQUE REFERENCES cpu_specification(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_cpu_model ON cpu(model);
CREATE INDEX IF NOT EXISTS idx_cpu_manufacturer_id ON cpu(manufacturer_id);

-- ============================================
-- CPU Technology Join Table
-- ============================================
CREATE TABLE IF NOT EXISTS cpu_technology (
    cpus_id UUID NOT NULL REFERENCES cpu(id) ON DELETE CASCADE,
    technologies_id UUID NOT NULL REFERENCES technology(id) ON DELETE CASCADE,
    PRIMARY KEY (cpus_id, technologies_id)
);

CREATE INDEX IF NOT EXISTS idx_cpu_technology_cpu ON cpu_technology(cpus_id);
CREATE INDEX IF NOT EXISTS idx_cpu_technology_tech ON cpu_technology(technologies_id);

-- ============================================
-- CPU Benchmark Table
-- ============================================
CREATE TABLE IF NOT EXISTS cpu_benchmark (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    single_core_score INTEGER NOT NULL,
    multi_core_score INTEGER NOT NULL,
    passmark_score INTEGER NOT NULL,
    cinebench_r23 DOUBLE PRECISION NOT NULL,
    test_date VARCHAR(50) NOT NULL,
    cpu_id UUID NOT NULL REFERENCES cpu(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_benchmark_cpu_id ON cpu_benchmark(cpu_id);

-- ============================================
-- SAMPLE DATA
-- ============================================

-- Insert Users
INSERT INTO users (id, username, email, password, role, created_at, updated_at) VALUES
('11111111-1111-1111-1111-111111111111', 'admin', 'admin@example.com', '$2a$10$YZ7T5eIzF.x3iq0eeH9FdO2E0M1lW0nK4cG5e8h6L0rQ5jV2e2Ty6', 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222', 'user1', 'user1@example.com', '$2a$10$YZ7T5eIzF.x3iq0eeH9FdO2E0M1lW0nK4cG5e8h6L0rQ5jV2e2Ty6', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333', 'user2', 'user2@example.com', '$2a$10$YZ7T5eIzF.x3iq0eeH9FdO2E0M1lW0nK4cG5e8h6L0rQ5jV2e2Ty6', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('44444444-4444-4444-4444-444444444444', 'john.doe', 'john.doe@example.com', '$2a$10$YZ7T5eIzF.x3iq0eeH9FdO2E0M1lW0nK4cG5e8h6L0rQ5jV2e2Ty6', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('55555555-5555-5555-5555-555555555555', 'jane.smith', 'jane.smith@example.com', '$2a$10$YZ7T5eIzF.x3iq0eeH9FdO2E0M1lW0nK4cG5e8h6L0rQ5jV2e2Ty6', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert Manufacturers
INSERT INTO manufacturer (id, name, country, founded_year) VALUES
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Intel', 'United States', 1968),
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'AMD', 'United States', 1969),
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'ARM Holdings', 'United Kingdom', 1990),
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'NVIDIA', 'United States', 1993),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'TSMC', 'Taiwan', 1987);

-- Insert Technologies
INSERT INTO technology (id, name, description, release_year) VALUES
('11aaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '5nm', '5 nanometer process technology with extreme ultraviolet (EUV) lithography', 2020),
('22bbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '7nm', '7 nanometer process technology', 2018),
('33cccccc-cccc-cccc-cccc-cccccccccccc', '10nm', '10 nanometer process technology', 2017),
('44dddddd-dddd-dddd-dddd-dddddddddddd', '14nm', '14 nanometer process technology', 2014),
('55eeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '3nm', '3 nanometer process technology with advanced gate design', 2022),
('66ffffff-ffff-ffff-ffff-ffffffffffff', 'AVX-512', 'Advanced Vector Extensions 512-bit instruction set', 2013),
('77aaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'DDR5', 'DDR5 memory technology support', 2021);

-- Insert CPU Specifications
INSERT INTO cpu_specification (id, cache_l1kb, cache_l2kb, cache_l3mb, tdp_watts, socket_type) VALUES
('fa000001-0000-0000-0000-000000000001', 640, 5120, 30, 125, 'LGA1700'),
('fa000002-0000-0000-0000-000000000002', 512, 4096, 32, 105, 'LGA1700'),
('fa000003-0000-0000-0000-000000000003', 768, 6144, 96, 162, 'AM5'),
('fa000004-0000-0000-0000-000000000004', 512, 4096, 64, 120, 'AM5'),
('fa000005-0000-0000-0000-000000000005', 256, 2048, 16, 28, 'ARM'),
('fa000006-0000-0000-0000-000000000006', 192, 1536, 12, 15, 'ARM');

-- Insert CPUs
INSERT INTO cpu (id, model, cores, threads, frequency_ghz, manufacturer_id, specification_id) VALUES
('fa111111-1111-1111-1111-111111111111', 'Intel Core i9-13900K', 24, 32, 3.0, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'fa000001-0000-0000-0000-000000000001'),
('fa222222-2222-2222-2222-222222222222', 'Intel Core i7-13700K', 16, 24, 3.4, 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'fa000002-0000-0000-0000-000000000002'),
('fa333333-3333-3333-3333-333333333333', 'AMD Ryzen 9 7950X', 16, 32, 4.5, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'fa000003-0000-0000-0000-000000000003'),
('fa444444-4444-4444-4444-444444444444', 'AMD Ryzen 7 7700X', 8, 16, 4.5, 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'fa000004-0000-0000-0000-000000000004'),
('fa555555-5555-5555-5555-555555555555', 'ARM Cortex-A78', 8, 8, 3.0, 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'fa000005-0000-0000-0000-000000000005'),
('fa666666-6666-6666-6666-666666666666', 'ARM Cortex-A77', 8, 8, 2.8, 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'fa000006-0000-0000-0000-000000000006');

-- Link CPUs to Technologies
INSERT INTO cpu_technology (cpus_id, technologies_id) VALUES
('fa111111-1111-1111-1111-111111111111', '33cccccc-cccc-cccc-cccc-cccccccccccc'),
('fa111111-1111-1111-1111-111111111111', '66ffffff-ffff-ffff-ffff-ffffffffffff'),
('fa111111-1111-1111-1111-111111111111', '77aaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('fa222222-2222-2222-2222-222222222222', '33cccccc-cccc-cccc-cccc-cccccccccccc'),
('fa222222-2222-2222-2222-222222222222', '66ffffff-ffff-ffff-ffff-ffffffffffff'),
('fa333333-3333-3333-3333-333333333333', '22bbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
('fa333333-3333-3333-3333-333333333333', '77aaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('fa444444-4444-4444-4444-444444444444', '22bbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
('fa555555-5555-5555-5555-555555555555', '11aaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
('fa666666-6666-6666-6666-666666666666', '22bbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb');

-- Insert CPU Benchmarks
INSERT INTO cpu_benchmark (id, single_core_score, multi_core_score, passmark_score, cinebench_r23, test_date, cpu_id) VALUES
('fb000001-0000-0000-0000-000000000001', 2450, 45600, 59800, 2150.5, '2024-01-15', 'fa111111-1111-1111-1111-111111111111'),
('fb000002-0000-0000-0000-000000000002', 2380, 28500, 44200, 1680.3, '2024-01-15', 'fa222222-2222-2222-2222-222222222222'),
('fb000003-0000-0000-0000-000000000003', 2420, 33200, 48500, 1920.7, '2024-01-16', 'fa333333-3333-3333-3333-333333333333'),
('fb000004-0000-0000-0000-000000000004', 2340, 18600, 32100, 1350.2, '2024-01-16', 'fa444444-4444-4444-4444-444444444444'),
('fb000005-0000-0000-0000-000000000005', 1800, 12400, 18200, 820.5, '2024-01-17', 'fa555555-5555-5555-5555-555555555555'),
('fb000006-0000-0000-0000-000000000006', 1750, 11200, 17100, 780.3, '2024-01-17', 'fa666666-6666-6666-6666-666666666666'),
('fb000007-0000-0000-0000-000000000007', 2460, 46200, 60500, 2180.8, '2024-02-01', 'fa111111-1111-1111-1111-111111111111'),
('fb000008-0000-0000-0000-000000000008', 2400, 34800, 50200, 2020.1, '2024-02-02', 'fa333333-3333-3333-3333-333333333333');
