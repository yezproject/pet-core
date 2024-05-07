# Use maven with JDK 17 as the build image
FROM maven:3.9.6-sapmachine-17 AS BUILD
# Set the working directory
WORKDIR /home/app
# Copy pom.xml and get all dependencies
COPY pom.xml .
# Copy pom file
COPY pet_test_utils/pom.xml ./pet_test_utils/pom.xml
COPY pet_domain/pom.xml ./pet_domain/pom.xml
COPY pet_application/pom.xml ./pet_application/pom.xml
COPY pet_infrastructure/pom.xml ./pet_infrastructure/pom.xml

RUN mvn -B dependency:go-offline
# Copy sources
COPY pet_test_utils ./pet_test_utils
COPY pet_domain ./pet_domain
COPY pet_application ./pet_application
COPY pet_infrastructure ./pet_infrastructure
# Build the application
RUN mvn -B clean test package

# Use OpenJDK 17 as the runtime image
FROM openjdk:17-jdk-alpine
# Copy the built jar file from the build image
COPY --from=BUILD /home/app/pet_infrastructure/target/*.jar app.jar
# Expose the application's port
EXPOSE 8080
# Set the startup command
ENTRYPOINT ["java","-jar","app.jar"]
