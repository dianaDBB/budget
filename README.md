# Getting Started

## Create jar file
- `mvn clean`
- `mvn install`
- `mvn package`

## Run
- `java -jar path_to_delivery-1.0-SNAPSHOT.jar`
- Kill the process: `kill -9 $(lsof -ti:443)`

## Swagger
- Local: https://localhost:443/budget/swagger-ui/index.html#/
- Deployed: https://budget-dbb.up.railway.app/budget/swagger-ui/index.html#/