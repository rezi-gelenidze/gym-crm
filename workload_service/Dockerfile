# Use a minimal Java runtime
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the pre-built JAR file from local machine to container
COPY target/workload_service-1.0-SNAPSHOT.jar workload_service.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "workload_service.jar"]
