# Getting Started

## Create jar file
- `mvn clean`
- `mvn install`
- `mvn package`

## Run
- `java -jar path_to_delivery-1.0-SNAPSHOT.jar`
- Kill the process: `kill -9 $(lsof -ti:8081)`

## Swagger
http://localhost:8081/budget/swagger-ui/index.html#/