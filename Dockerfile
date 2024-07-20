# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
#WORKDIR /app

# Copy the Spring Boot application JAR file to the container
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/user-service.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the entry point to run the Spring Boot application
ENTRYPOINT ["java","-jar","/app/user-service.jar"]
