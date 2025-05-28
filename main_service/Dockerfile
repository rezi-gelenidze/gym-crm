# Use a minimal Java runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the pre-built JAR file from local machine to container
COPY target/main_service-1.0-SNAPSHOT.jar main_service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "main_service.jar"]
