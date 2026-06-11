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
- MariaDB/MySQL
- Selenium + Serenity Cucumber acceptance tests

## Prerequisites

- Java 21
- Maven 3.9+
- IntelliJ IDEA
- Optional: MySQL with phpMyAdmin if you want persistent sample data

## Quick start

There are two supported ways to run the app.

### Run immediately with H2

This is the fastest way to start the project. No database setup is required.

The application now starts out of the box with an in-memory H2 database:

```bash
mvn spring-boot:run
```

Open:

- `http://localhost:8080/`
- `http://localhost:8080/raw-materials`

Important:

- Data is stored only for the lifetime of the process when using H2.
- H2 starts empty. If you open `/raw-materials` and see no rows, that is expected.
- If you want the two sample raw materials from the SQL file, use the MySQL setup below.

If you open the project in IntelliJ IDEA, use the shared run configuration `Life Is Bananas (H2)` and press Run.

### Run with MySQL and phpMyAdmin

Use this option if you want persistent data and the sample rows from the SQL file.

Import [`life_is_bananas_mvc.sql`](life_is_bananas_mvc.sql). The script creates:

- the `life_is_bananas_mvc` database
- the `raw_material` table
- two sample rows

Then create or reuse a MySQL user that has access to that database.

The app uses these datasource defaults:

- URL: `jdbc:mysql://localhost:3306/life_is_bananas_mvc?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC`
- Username: `lifeisbananas`
- Password: `lifeisbananas`

Override them with standard Spring environment variables if needed:

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/life_is_bananas_mvc?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC \
SPRING_DATASOURCE_USERNAME=your_user \
SPRING_DATASOURCE_PASSWORD=your_password \
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

The application validates the schema at startup. If the SQL script was not imported correctly, startup will fail instead of creating tables automatically.

If you open the project in IntelliJ IDEA, use the shared run configuration `Life Is Bananas (MySQL)` and press Run.

After startup, open:

- `http://localhost:8080/`
- `http://localhost:8080/raw-materials`

You should see the sample rows `RM-001` and `RM-002`.

## Exact phpMyAdmin steps

1. Open your browser.
2. Go to `http://localhost/phpmyadmin` or the URL your local stack uses.
3. Log in as a MySQL user that is allowed to create databases and users. On many local installs this is `root`.
4. In the top navigation bar, click `Import`.
5. Click `Choose File`.
6. Select [`life_is_bananas_mvc.sql`](life_is_bananas_mvc.sql).
7. Scroll to the bottom and click `Import`.
8. In the left sidebar, confirm that a database named `life_is_bananas_mvc` now exists.
9. Click `Privileges` in the top navigation bar.
10. Click `Add user account`.
11. In `User name`, type `lifeisbananas`.
12. In `Host name`, choose `Local`.
13. In `Password`, type `lifeisbananas`.
14. In `Re-type`, type `lifeisbananas` again.
15. Under `Database for user account`, choose `Grant all privileges on database "life_is_bananas_mvc"`.
16. Leave the rest unchanged unless your local setup requires something specific.
17. Click `Go`.

## IntelliJ IDEA steps

1. Open IntelliJ IDEA.
2. Click `Open`.
3. Select the project folder `life-is-bananas-mvc`.
4. Wait for Maven import and indexing to finish.
5. On the top toolbar, open the run configuration dropdown.
6. Choose `Life Is Bananas (H2)` for the fastest startup with an empty in-memory database.
7. Choose `Life Is Bananas (MySQL)` if you want to use phpMyAdmin/MySQL and see the sample rows.
8. Click the green Run button.
9. Open `http://localhost:8080/raw-materials`.

## Command-line start

If you prefer the terminal instead of IntelliJ:

### H2

```bash
mvn spring-boot:run
```

### MySQL

```bash
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/life_is_bananas_mvc?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC \
SPRING_DATASOURCE_USERNAME=your_user \
SPRING_DATASOURCE_PASSWORD=your_password \
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

## Manual verification

1. Import [`life_is_bananas_mvc.sql`](life_is_bananas_mvc.sql) in phpMyAdmin.
2. Confirm the `life_is_bananas_mvc.raw_material` table contains `RM-001` and `RM-002`.
3. Start the application with the `Life Is Bananas (MySQL)` run configuration in IntelliJ.
4. Open `http://localhost:8080/raw-materials` and verify the two sample rows are visible.
5. Create a new raw material and confirm it appears in the list.
6. Open that record, edit it, save it, and confirm the changes persist.
7. Delete the record and confirm it disappears from the list.
8. Refresh phpMyAdmin and verify the same changes exist in `life_is_bananas_mvc.raw_material`.

## Automated tests

Run the verified integration tests:

```bash
mvn test
```

Tests still use H2 in-memory configuration from `src/test/resources/application.properties`.

## Run the Selenium acceptance test

The Selenium acceptance test uses Serenity Cucumber and ChromeDriver.

Start the application first:

```bash
mvn spring-boot:run
```

In a second terminal, run the Serenity acceptance suite:

```bash
mvn -Dtest=AcceptanceTestSuite test
```

The acceptance scenarios are defined in `src/test/resources/features/raw_material_crud.feature`.
They drive the UI through Chrome against `http://localhost:8080`.
Before each scenario, they remove only raw materials with the `AT-RM-` test-code prefix through the UI.
