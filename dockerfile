# Use the official OpenJDK image as base
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/Bank-1.jar /app/Bank-1.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "Bank-1.jar", "--spring.datasource.url=jdbc:postgresql://postgres:5433/postgres"]