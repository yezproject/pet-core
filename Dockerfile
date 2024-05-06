FROM maven:3.9.6-sapmachine-17 AS build

COPY pet_test_utils /home/app/pet_test_utils
COPY pet_domain /home/app/pet_domain
COPY pet_application /home/app/pet_application
COPY pet_infrastructure /home/app/pet_infrastructure

COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean test package

FROM openjdk:17-jdk-alpine
COPY --from=build /home/app/pet_infrastructure/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
