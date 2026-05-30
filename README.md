# Life Is Bananas MVC

Spring Boot MVC application for Life Is Bananas.

## Implemented user story

As a bio-engineer, I want to maintain the raw material catalogue so that I can work with correct ingredient data.

This project demonstrates:

- Create a raw material
- Read the list and details of a raw material
- Update a raw material
- Delete a raw material

## Tech stack

- Spring Boot
- Spring MVC
- Thymeleaf
- Spring Data JPA
- H2 in-memory database
- Selenium + Cucumber acceptance test scaffold

## Run the application

```bash
mvn spring-boot:run
```

Open:

- `http://localhost:8080/`
- `http://localhost:8080/raw-materials`

## Run the verified integration tests

```bash
mvn -Dtest=RawMaterialControllerIntegrationTest test
```

## Run the Selenium acceptance test

The Selenium test uses SafariDriver on macOS.

Before running it, enable:

1. Safari
2. Settings
3. Advanced
4. Show features for web developers
5. Developer
6. Allow Remote Automation

Then run:

```bash
mvn test
```

The acceptance scenarios are defined in `src/test/resources/features/raw_material_crud.feature`.
