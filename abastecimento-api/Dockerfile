# Use a slim base image to reduce the size of the final image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file first to optimize the build process
COPY pom.xml .

# Install Maven.  Using a specific version is recommended for reproducibility.
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy the rest of the application code
COPY . .

# Copy the application.properties file
COPY src/main/resources/application.properties .

# Build the Spring Boot application. Skip tests during the image build to speed up the process.
# Tests should be run separately in a CI/CD pipeline or locally during development.
RUN mvn clean package -DskipTests

# Expose the port used by the Spring Boot application
EXPOSE 8088
ENV SPRING_DATASOURCE_URL=jdbc:h2:mem:abastecimento-api

# Define the command to run the application 

# Define the command to run the application
# CMD ["java", "-jar", "target/abastecimento-api-0.0.1-SNAPSHOT.jar"]
CMD ["java", "-jar", "-Dspring.datasource.url=jdbc:h2:mem:abastecimento-api", "target/abastecimento-api-0.0.1-SNAPSHOT.jar"]