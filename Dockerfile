FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the JAR file to the working directory
COPY target/booking-service.jar app.jar

# Expose the correct port
EXPOSE 8094

# Run the application
ENTRYPOINT ["java", "-jar", "booking-service.jar"]