#
# Build stage
#
FROM maven:3.8.5-openjdk-17 AS build
COPY . /home/app/
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /home/app/delivery/target/delivery-1.0.0-SNAPSHOT.jar /usr/local/lib/budget.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/budget.jar"]