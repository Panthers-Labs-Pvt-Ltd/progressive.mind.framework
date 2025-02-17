#!/bin/bash
set -e

# Create directories for tablespaces
mkdir -p /var/lib/postgresql/data/ts_business_value
mkdir -p /var/lib/postgresql/data/ts_data_management

# Execute the original entrypoint script from the postgres image
exec docker-entrypoint.sh "$@"
