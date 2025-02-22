FROM trinodb/trino:471

# Install only the required PostgreSQL connector
# RUN wget https://repo1.maven.org/maven2/io/trino/trino-postgresql/471/trino-postgresql-471.jar -P /usr/lib/trino/plugin/postgresql/

# Copy any additional configuration files if needed
COPY config /etc/trino

COPY catalog /etc/trino/catalog

# Expose the required ports
EXPOSE 10100
