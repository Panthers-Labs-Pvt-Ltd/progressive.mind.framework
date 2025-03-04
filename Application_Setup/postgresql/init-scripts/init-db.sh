#!/bin/bash
set -e

# Create directories for tablespaces
mkdir -p /var/lib/postgresql/tablespaces/chimera
mkdir -p /var/lib/postgresql/tablespaces/keycloak
mkdir -p /var/lib/postgresql/tablespaces/vault
mkdir -p /var/lib/postgresql/tablespaces/datahub

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    -- Create Chimera database and tablespace if integration is required
    \if :'CHIMERA_INTEGRATION'
    CREATE TABLESPACE chimera_space LOCATION '/var/lib/postgresql/tablespaces/chimera';
    CREATE DATABASE chimera_db TABLESPACE chimera_space;
    CREATE USER chimera_user WITH ENCRYPTED PASSWORD 'chimera_pass';
    GRANT ALL PRIVILEGES ON DATABASE chimera_db TO chimera_user;
    \endif

    -- Create KeyCloak database and tablespace if integration is required
    \if :'KEYCLOAK_INTEGRATION'
    CREATE TABLESPACE keycloak_space LOCATION '/var/lib/postgresql/tablespaces/keycloak';
    CREATE DATABASE keycloak_db TABLESPACE keycloak_space;
    CREATE USER keycloak_user WITH ENCRYPTED PASSWORD 'keycloak_pass';
    GRANT ALL PRIVILEGES ON DATABASE keycloak_db TO keycloak_user;
    \endif

    -- Create Vault database and tablespace if integration is required
    \if :'VAULT_INTEGRATION'
    CREATE TABLESPACE vault_space LOCATION '/var/lib/postgresql/tablespaces/vault';
    CREATE DATABASE vault_db TABLESPACE vault_space;
    CREATE USER vault_user WITH ENCRYPTED PASSWORD 'vault_pass';
    GRANT ALL PRIVILEGES ON DATABASE vault_db TO vault_user;
    \endif

    -- Create Datahub database and tablespace if integration is required
    \if :'DATAHUB_INTEGRATION'
    CREATE TABLESPACE datahub_space LOCATION '/var/lib/postgresql/tablespaces/datahub';
    CREATE DATABASE datahub_db TABLESPACE datahub_space;
    CREATE USER datahub_user WITH ENCRYPTED PASSWORD 'datahub_pass';
    GRANT ALL PRIVILEGES ON DATABASE datahub_db TO datahub_user;
    \endif
EOSQL
