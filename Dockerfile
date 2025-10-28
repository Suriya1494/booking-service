FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file to the working directory
COPY target/booking-service.jar app.jar

# Expose the correct port
EXPOSE 8094

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]