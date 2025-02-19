# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/metrics-collector-daemon-1.0-SNAPSHOT.jar /app/metrics-collector-daemon.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "metrics-collector-daemon.jar", "0.0.0.0", "8083"]