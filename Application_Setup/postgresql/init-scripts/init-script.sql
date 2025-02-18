-- roles

-- consider this as superuser (without really being the superuser) for chimera overall
create role chimera
WITH
LOGIN
CREATEDB
BYPASSRLS
REPLICATION
connection limit 10
password 'chimera123';

-- /* Business Value Service */

-- what is the right name, the role is for business_value_service
create role owner_business_value
WITH
LOGIN
connection limit 10
password 'owner_business_value';

-- /* tablespaces */

-- tablespace for business value
--create tablespace ts_business_value
--owner owner_business_value
--location '/var/lib/postgresql/data/ts_business_value';

-- database for data_management using default template1, and server locale
create database business_value
with
owner = owner_business_value
encoding = 'UTF8'
strategy = 'WAL_LOG'
--tablespace = ts_data_management
allow_connections = true
connection_limit = 10;

-- Grant privileges to superuser role, chimera
GRANT ALL PRIVILEGES ON DATABASE business_value TO chimera;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO chimera;
GRANT ALL PRIVILEGES ON SCHEMA public TO chimera;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO chimera;

-- Create roles
CREATE ROLE business_value_read_only  WITH LOGIN PASSWORD 'readonly_password';

GRANT CONNECT ON DATABASE business_value TO business_value_read_only;
GRANT USAGE ON SCHEMA public TO business_value_read_only;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO business_value_read_only;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO business_value_read_only;

-- /* Data Management*/
-- role for Data Management
create role owner_data_management
WITH
LOGIN
connection limit 10
password 'owner_data_management';

-- tablespace for data management
--create tablespace ts_data_management
--owner user_data_management
--location '/var/lib/postgresql/data/ts_data_management';

-- database for data_management using default template1, and server locale
create database data_management
with
owner = owner_data_management
encoding = 'UTF8'
strategy = 'WAL_LOG'
--tablespace = ts_data_management
allow_connections = true
connection_limit = 10;

-- Grant privileges to superuser role, chimera
GRANT ALL PRIVILEGES ON DATABASE data_management TO chimera;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO chimera;
GRANT ALL PRIVILEGES ON SCHEMA public TO chimera;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO chimera;

-- Create roles
CREATE ROLE data_management_read_only  WITH LOGIN PASSWORD 'readonly_password';

GRANT CONNECT ON DATABASE data_management TO data_management_read_only  ;
GRANT USAGE ON SCHEMA public TO data_management_read_only  ;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO data_management_read_only  ;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO data_management_read_only;


-- /* Chimera Application Management */
-- role for Chimera Application Management
create role owner_chimera_application
WITH
LOGIN
connection limit 100
password 'owner_chimera_application';

-- tablespace for Chimera Application management
--create tablespace ts_chimera_db
--owner owner_chimera_application
--location '/var/lib/postgresql/data/ts_data_management';

-- database for data_management using default template1, and server locale
create database chimera_db
with
owner = owner_chimera_application
encoding = 'UTF8'
strategy = 'WAL_LOG'
--tablespace = ts_data_management
allow_connections = true
connection_limit = 10;

-- Grant privileges to superuser role, chimera
GRANT ALL PRIVILEGES ON DATABASE chimera_db TO chimera;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO chimera;
GRANT ALL PRIVILEGES ON SCHEMA public TO chimera;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL PRIVILEGES ON TABLES TO chimera;

-- Create roles
CREATE ROLE chimera_db_read_only  WITH LOGIN PASSWORD 'readonly_password';

GRANT CONNECT ON DATABASE chimera_db TO chimera_db_read_only;
GRANT USAGE ON SCHEMA public TO chimera_db_read_only;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO chimera_db_read_only;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO chimera_db_read_only;

-- Define timeouts, replication, etc.
