# LifeIsBananasSecurity

Spring Boot security prototype for Part 4 of the Life Is Bananas project.

This application demonstrates:

- validation with Spring Validation
- authentication with Spring Security
- authorization with role-based access control
- Spring MVC with Thymeleaf
- persistence with JPA
- MariaDB integration

## Purpose

The implemented use case is:

As a bio-engineer, I want to securely maintain the raw material catalogue so that ingredient data remains correct and only authorized users can modify it.

The project was built by staying as close as possible to the professor's reference project structure, security flow, and architecture, while adapting it to the Life Is Bananas domain.

## Tech Stack

- Java 17
- Spring Boot 3.4.5
- Spring Web MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MariaDB / MySQL
- H2 for automated tests
- Maven

## Role Model

The prototype uses three access levels:

- `GUEST`
  - anonymous visitor
  - can access home and login pages only
- `BIO_ENGINEER`
  - full CRUD access to raw materials
- `WAREHOUSE_MANAGER`
  - read-only access to raw materials

One seeded user has multiple roles so the role-selection flow remains close to the professor's example.

## Seeded Users

- `hannah.peel@lifeisbananas.be` / `banana123`
  - `BIO_ENGINEER`
- `marcus.store@lifeisbananas.be` / `warehouse123`
  - `WAREHOUSE_MANAGER`
- `nina.cross@lifeisbananas.be` / `multi123`
  - `BIO_ENGINEER`
  - `WAREHOUSE_MANAGER`

Passwords are stored as BCrypt hashes in the database.

## Implemented Features

- custom login page
- authentication against database users
- BCrypt password verification
- role selection menu after login
- protected role-specific pages
- raw material catalogue
- create raw material
- view raw material details
- edit raw material
- delete raw material
- validation errors for invalid form input
- access denied handling for unauthorized actions

## Project Structure

- `src/main/java/be/lifeisbananas/security`
  - application, security config, controllers, services, repositories, domain classes
- `src/main/resources/templates`
  - Thymeleaf views
- `src/main/resources/schema.sql`
  - database schema
- `src/main/resources/data.sql`
  - seeded data
- `database-setup.sql`
  - database and user creation script

## Database Setup

Run the SQL in this order:

1. `database-setup.sql`
2. `src/main/resources/schema.sql`
3. `src/main/resources/data.sql`

This creates:

- database: `life_is_bananas_security`
- user: `lifeisbananas_user`
- password: `lifeisbananas_pw`

If your local MariaDB setup differs, update:

- `src/main/resources/application.yml`

## Run The Application

From this directory:

```bash
mvn spring-boot:run
```

Open:

- `http://localhost:8888/`

## Run The Tests

```bash
mvn test
```

The automated test suite verifies:

- anonymous users are redirected to login
- bio-engineers can create, update, and delete raw materials
- warehouse managers have read-only access
- duplicate codes are rejected by validation

## Expected Demonstration Flow

### Anonymous user

- can open home page
- can open login page
- cannot open `/raw-materials`

### Bio-engineer

- can login
- can choose `BIO_ENGINEER`
- can create, edit, view, and delete raw materials

### Warehouse manager

- can login
- can choose `WAREHOUSE_MANAGER`
- can view the catalogue and details
- cannot create, edit, or delete raw materials

### Multi-role user

- can login
- sees both roles in the role menu
- can switch between role-specific entry points

## Notes

- The project intentionally keeps a structure close to the professor's example.
- The domain and use case come from the Life Is Bananas MVC project.
- The database scope was intentionally reduced to the minimum needed for this prototype:
  - `persons`
  - `roles`
  - `raw_materials`
