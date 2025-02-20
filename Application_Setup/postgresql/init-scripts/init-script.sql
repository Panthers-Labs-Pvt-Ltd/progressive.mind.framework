-- roles

-- consider this as superuser (without really being the superuser) for chimera overall
create role chimera
WITH
LOGIN
CREATEDB
BYPASSRLS
REPLICATION
-- connection limit 10
password 'chimera123';

-- /* Business Value Service */

-- what is the right name, the role is for business_value_service
create role owner_business_value
WITH
LOGIN
-- connection limit 10
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
-- allow_connections = true
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

-- tables

\connect chimera_db;

CREATE TABLE vault_kv_store (
  parent_path TEXT COLLATE "C" NOT NULL,
  path        TEXT COLLATE "C",
  key         TEXT COLLATE "C",
  value       BYTEA,
  CONSTRAINT pkey PRIMARY KEY (path, key)
);

ALTER TABLE vault_kv_store OWNER TO owner_chimera_application;

CREATE INDEX parent_path_idx ON vault_kv_store (parent_path);

CREATE TABLE vault_ha_locks (
  ha_key                                      TEXT COLLATE "C" NOT NULL,
  ha_identity                                 TEXT COLLATE "C" NOT NULL,
  ha_value                                    TEXT COLLATE "C",
  valid_until                                 TIMESTAMP WITH TIME ZONE NOT NULL,
  CONSTRAINT ha_key PRIMARY KEY (ha_key)
);

ALTER TABLE vault_ha_locks OWNER TO owner_chimera_application;

--CREATE TABLE vault_kv_store_metadata (
--  path        TEXT COLLATE "C" NOT NULL,
--  created_time TIMESTAMP WITH TIME ZONE,
--  created_by  TEXT COLLATE "C",
--  updated_time TIMESTAMP WITH TIME ZONE,
--  updated_by  TEXT COLLATE "C",
--  CONSTRAINT pkey PRIMARY KEY (path)
--);
--
--CREATE TABLE vault_kv_store_version (
--  path        TEXT COLLATE "C" NOT NULL,
--  version     INTEGER,
--  CONSTRAINT pkey PRIMARY KEY (path, version)
--);
--
--CREATE TABLE vault_kv_store_version_metadata (
--  path        TEXT COLLATE "C" NOT NULL,
--  version     INTEGER,
--  created_time TIMESTAMP WITH TIME ZONE,
--  created_by  TEXT COLLATE "C",
--  updated_time TIMESTAMP WITH TIME ZONE,
--  updated_by  TEXT COLLATE "C",
--  CONSTRAINT pkey PRIMARY KEY (path, version)
--);
--
--CREATE TABLE vault_audit (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  path        TEXT COLLATE "C",
--  secret_id   TEXT COLLATE "C",
--  secret_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_path_idx ON vault_audit (path);
--
--CREATE TABLE vault_audit_device (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  device_id   TEXT COLLATE "C",
--  device_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_device_device_id_idx ON vault_audit_device (device_id);
--
--CREATE TABLE vault_audit_user (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  user_id     TEXT COLLATE "C",
--  user_name   TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_user_user_id_idx ON vault_audit_user (user_id);
--
--CREATE TABLE vault_audit_policy (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  policy_id   TEXT COLLATE "C",
--  policy_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_policy_policy_id_idx ON vault_audit_policy (policy_id);
--
--CREATE TABLE vault_audit_approle (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  approle_id  TEXT COLLATE "C",
--  approle_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_approle_approle_id_idx ON vault_audit_approle (approle_id);
--
--CREATE TABLE vault_audit_auth (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_auth_auth_method_idx ON vault_audit_auth (auth_method);
--
--CREATE TABLE vault_audit_token (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  token_id    TEXT COLLATE "C",
--  token_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_token_token_id_idx ON vault_audit_token (token_id);
--
--CREATE TABLE vault_audit_entity (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_entity_id_idx ON vault_audit_entity (entity_id);
--
--CREATE TABLE vault_audit_group (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_group_group_id_idx ON vault_audit_group (group_id);
--
--CREATE TABLE vault_audit_mount (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  mount_type  TEXT COLLATE "C",
--  mount_path  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_mount_mount_path_idx ON vault_audit_mount (mount_path);
--
--CREATE TABLE vault_audit_secret (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  path        TEXT COLLATE "C",
--  secret_id   TEXT COLLATE "C",
--  secret_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_secret_path_idx ON vault_audit_secret (path);
--
--CREATE TABLE vault_audit_policy_document (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  policy_id   TEXT COLLATE "C",
--  policy_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_policy_document_policy_id_idx ON vault_audit_policy_document (policy_id);
--
--CREATE TABLE vault_audit_entity_alias (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_alias_entity_id_idx ON vault_audit_entity_alias (entity_id);
--
--CREATE TABLE vault_audit_entity_alias_group (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_alias_group_entity_id_idx ON vault_audit_entity_alias_group (entity_id);
--
--CREATE TABLE vault_audit_entity_alias_group_member (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  member_id   TEXT COLLATE "C",
--  member_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_alias_group_member_entity_id_idx ON vault_audit_entity_alias_group_member (entity_id);
--
--CREATE TABLE vault_audit_entity_alias_group_member_entity (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  member_id   TEXT COLLATE "C",
--  member_name TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_alias_group_member_entity_entity_id_idx ON vault_audit_entity_alias_group_member_entity (entity_id);
--
--CREATE TABLE vault_audit_entity_alias_group_member_entity_alias (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  member_id   TEXT COLLATE "C",
--  member_name TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
--
--CREATE INDEX audit_entity_alias_group_member_entity_alias_entity_id_idx ON vault_audit_entity_alias_group_member_entity_alias (entity_id);
--
--CREATE TABLE vault_audit_entity_alias_group_member_entity_alias_group (
--  id          SERIAL PRIMARY KEY,
--  request_id  TEXT COLLATE "C",
--  event_time  TIMESTAMP WITH TIME ZONE,
--  audit_type  TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  member_id   TEXT COLLATE "C",
--  member_name TEXT COLLATE "C",
--  entity_id   TEXT COLLATE "C",
--  entity_name TEXT COLLATE "C",
--  alias_id    TEXT COLLATE "C",
--  alias_name  TEXT COLLATE "C",
--  group_id    TEXT COLLATE "C",
--  group_name  TEXT COLLATE "C",
--  request_data TEXT COLLATE "C",
--  response_data TEXT COLLATE "C",
--  error       TEXT COLLATE "C",
--  auth_method TEXT COLLATE "C",
--  auth_token  TEXT COLLATE "C",
--  remote_addr TEXT COLLATE "C",
--  user_agent  TEXT COLLATE "C",
--  error_code  TEXT COLLATE "C",
--  error_message TEXT COLLATE "C"
--);
