FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package

FROM eclipse-temurin:21.0.1_12-jdk
COPY --from=build /target/car-booking-backend-0.0.1-SNAPSHOT.jar car-booking.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","car-booking.jar"]