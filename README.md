# Budget API

This repository is a personal space for hands-on learning and experimentation.

It is used to practice and improve skills in:

- Java
- Maven
- Spring Boot
- Test automation strategies
- AI tools (e.g. Copilot, Claude)

## Purpose

The main goal of this repository is continuous learning and exploration, not production usage.
Code here may be experimental, incomplete, or frequently refactored as new concepts are tested.

## Areas of Focus

- Building robust API test automation frameworks
- Exploring modern testing practices
- Experimenting with AI-assisted development workflows
- Prototyping AI agents for productivity and testing use cases
- Improving software engineering practices through iteration

## Disclaimer

This is a personal repository.
There are no guarantees regarding stability, structure, or completeness.

## How to run locally

- Ensure you have `dotenv-cli` installed globally (example: `sudo npm install -g dotenv-cli`)
- Open terminal and execute the following:

```
mvn clean install
dotenv -e .env mvn spring-boot:run
```

- Or select the file `delivery/src/main/java/com.budget/BudgetApiApplication.java`, right click and select "Run"

## Swagger

- Local: http://localhost:8443/budget/swagger-ui/index.html#/
- Release: https://budget-cemp.onrender.com/budget/swagger-ui/index.html

## Deploy

Deploy is done using [Render](https://dashboard.render.com/)

## DB

Database is SQL, hosted on [Neon](https://console.neon.tech/).