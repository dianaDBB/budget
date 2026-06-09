FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn -pl delivery -am clean package -DskipTests

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/delivery/target/*.jar app.jar

EXPOSE 8443

ENTRYPOINT ["java","-jar","app.jar"]