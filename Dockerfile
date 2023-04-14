#
# Build stage
#
FROM maven:3.8.6-jdk-17-slim AS build
COPY budget /home/app/budget
RUN mvn -f /home/app/budget/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-jre-slim
COPY --from=build /home/app/budget/delivery/target/delivery-1.0.0-SNAPSHOT.jar /usr/local/lib/budget.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]