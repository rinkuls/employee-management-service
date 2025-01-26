#################
# Build the  JAVA APP #

FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copy the application code
COPY target/employee-management-tool-0.0.1-SNAPSHOT.jar app.jar

# Copy the .env file (only for Docker)
COPY gitHubToken.env /app/gitHubToken.env

# Set the application to use Docker profile
ENV SPRING_PROFILES_ACTIVE docker

ENTRYPOINT ["java", "-jar", "app.jar"]