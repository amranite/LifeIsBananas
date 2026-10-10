# Life Is Bananas Catalogue

This is the complete web coursework stage within `LifeIsBananas`, combining MVC
and authentication. The earlier [MVC stage](https://github.com/amranite/LifeIsBananas/tree/web/mvc)
and [domain baseline](https://github.com/amranite/LifeIsBananas/tree/main) remain separate.
Original MVC and Security commits and contributor metadata are retained unchanged.

Academic Spring Boot MVC application for maintaining a raw material catalogue.
This repository incorporates the authentication and role exercises from
`life-is-bananas-security`; its original commit history is retained unchanged in the merge.

Java 21 and Maven 3.9+ are required.

## Local demo

```bash
mvn spring-boot:run
```

Open `http://localhost:8080/`. The default `demo` profile uses a disposable
in-memory H2 database, two sample raw materials and these fictional accounts:

| Account | Password | Access |
| --- | --- | --- |
| hannah.peel@example.test | banana123 | Bio-engineer: read, create, edit and delete |
| marcus.store@example.test | warehouse123 | Warehouse manager: read |
| nina.cross@example.test | multi123 | Both roles |

Demo data disappears when the process stops. The demo datasource is deliberately
fixed to an in-memory database. Demo accounts are created only in that profile;
passwords are BCrypt encoded. Forms use Spring Security's CSRF protection.
Role selection and role pages also check account ownership.

## Persistent local database

Import [life_is_bananas_mvc.sql](life_is_bananas_mvc.sql) into a local MySQL or
MariaDB instance. It creates the catalogue and account tables. Choose a database
user and supply its credentials through `SPRING_DATASOURCE_USERNAME` and
`SPRING_DATASOURCE_PASSWORD`; there is no default database password.

On the first start, also supply `CATALOG_ADMIN_EMAIL` and `CATALOG_ADMIN_PASSWORD`
(at least 12 characters). The initializer creates one account with both roles
only when the account table is empty. Later starts preserve existing accounts
and passwords, and no longer require these two bootstrap variables.

```bash
export SPRING_DATASOURCE_USERNAME='your_database_user'
read -s SPRING_DATASOURCE_PASSWORD
export SPRING_DATASOURCE_PASSWORD
export CATALOG_ADMIN_EMAIL='your_account_email'
read -s CATALOG_ADMIN_PASSWORD
export CATALOG_ADMIN_PASSWORD
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Use `-Dspring-boot.run.profiles=mariadb` for MariaDB. Override
`SPRING_DATASOURCE_URL` if needed. These profiles validate an existing schema;
application startup does not drop tables or import SQL fixtures. The supplied
SQL file upserts the two sample materials, so import it into a dedicated local
database rather than using it as a production migration.

## Verification

```bash
mvn test
```

The 18 integration tests cover catalogue CRUD, successful and rejected login,
role access, account ownership, rejected requests without CSRF tokens, missing
records and persistent account initialization. Persistent-profile tests use H2;
they do not certify MySQL or MariaDB compatibility.

The separate Selenium/Serenity suite requires Chrome and the running default
demo on port 8080:

```bash
mvn -Dtest=AcceptanceTestSuite test
```

It logs in as the demo bio-engineer and removes only records with the `AT-RM-`
prefix. Browser execution remains a separate check.

## Scope

This is a coursework showcase for MVC, JPA, Thymeleaf and Spring Security. The
current dependency baseline needs a fresh vulnerability review and upgrade
before internet hosting. Account administration, schema migrations and hosting
configuration are outside this local demo. Screenshots and portfolio presentation
will be added after publication preparation is reviewed.

Original contributor metadata and commit hashes are retained unchanged.
New cleanup and integration work is recorded in ordinary commits. This project is an academic exercise
and does not claim affiliation with the company used in its assignment.
