# Getting Started 

## Create jar file
- `mvn clean`
- `mvn install`
- `mvn package`

## Run
- `java -jar path_to_delivery-1.0-SNAPSHOT.jar`
- Kill the process: `kill -9 $(lsof -ti:8443)`

## Postman local
- http://localhost:8443/budget/budget/file/all

## Swagger
- Local: http://localhost:8443/budget/swagger-ui/index.html#/
