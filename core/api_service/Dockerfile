FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/core.api_service-1.0-SNAPSHOT.jar /app/api_service-1.0-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "api-service-1.0-SNAPSHOT.jar"]