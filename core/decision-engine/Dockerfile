FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/core.decision-engine-1.0-SNAPSHOT.jar /app/core.decision-engine-1.0-SNAPSHOT.jar

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "core.decision-engine-1.0-SNAPSHOT.jar"]

