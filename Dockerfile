# Use maven with JDK 17 as the build image
FROM maven:3.9.6-sapmachine-17 AS BUILD
# Set the working directory
WORKDIR /home/app
# Copy pom.xml and get all dependencies
COPY pom.xml .
# Copy pom file
COPY test-util/pom.xml ./test-util/pom.xml
COPY domain/pom.xml ./domain/pom.xml
COPY application/pom.xml ./application/pom.xml
COPY infrastructure/pom.xml ./infrastructure/pom.xml

RUN mvn -B dependency:go-offline
# Copy sources
COPY test-util ./test-util
COPY domain ./domain
COPY application ./application
COPY infrastructure ./infrastructure
# Build the application
RUN mvn -B clean test package

# Use OpenJDK 17 as the runtime image
FROM openjdk:17-jdk-alpine
# Copy the built jar file from the build image
COPY --from=BUILD /home/app/infrastructure/target/*.jar app.jar
# Expose the application's port
EXPOSE 8080
# Set the startup command
ENTRYPOINT ["java","-jar","app.jar"]
