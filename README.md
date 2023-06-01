# Getting Started 

## Create jar file
- `mvn clean`
- `mvn install`
- `mvn package`

## Run
- `java -jar path_to_delivery-1.0-SNAPSHOT.jar`
- Kill the process: `kill -9 $(lsof -ti:8443)`

## Swagger
- Local: http://localhost:8443/budget/swagger-ui/index.html#/
- Deployed: https://budget-bank.up.railway.app/budget/swagger-ui/index.html#/

## Deploy
- [Railway](https://railway.app/project/e4deb5b5-5c63-4119-bb58-fa72752119aa/service/7b28f149-a5b9-485a-9d30-29207c066607)
