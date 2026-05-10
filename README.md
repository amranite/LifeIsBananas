# LifeIsBananas

Small Java/Spring learning project around recipe and product lifecycle management.

This branch, `design-pattern/state`, applies the GoF State pattern to the `Product` lifecycle.

## State Pattern In This Branch

The State pattern is used for `Product`.

- `Product` is the context
- `ProductState` is the state interface
- the concrete states are:
  - `InDevelopmentState`
  - `TestedState`
  - `ApprovedState`
  - `InProductionState`
  - `RejectedState`

The goal is to move lifecycle behavior out of a `switch` statement and into dedicated state classes.

Before this refactor, `Product.promote()` and `Product.reject()` decided behavior with status checks inside `Product`.
After this refactor, `Product` delegates behavior to the current `ProductState`.

## Relevant Files

- [src/main/java/be/lifeisbananas/Product.java](src/main/java/be/lifeisbananas/Product.java)
- [src/main/java/be/lifeisbananas/state/ProductState.java](src/main/java/be/lifeisbananas/state/ProductState.java)
- [src/main/java/be/lifeisbananas/state/InDevelopmentState.java](src/main/java/be/lifeisbananas/state/InDevelopmentState.java)
- [src/main/java/be/lifeisbananas/state/TestedState.java](src/main/java/be/lifeisbananas/state/TestedState.java)
- [src/main/java/be/lifeisbananas/state/ApprovedState.java](src/main/java/be/lifeisbananas/state/ApprovedState.java)
- [src/main/java/be/lifeisbananas/state/InProductionState.java](src/main/java/be/lifeisbananas/state/InProductionState.java)
- [src/main/java/be/lifeisbananas/state/RejectedState.java](src/main/java/be/lifeisbananas/state/RejectedState.java)

## How To Show The Pattern Works

### 1. Design Explanation

- callers still use the same method: `product.promote()`
- the behavior changes depending on the current state object
- each state class controls its own transition rules

### 2. Lifecycle

Expected product lifecycle:

1. `IN_DEVELOPMENT`
2. `TESTED`
3. `APPROVED`
4. `IN_PRODUCTION`

Expected rejection behavior:

- reject from `IN_DEVELOPMENT`: blocked
- reject from `TESTED`: moves to `REJECTED`

### 3. Focused Tests

Run:

```bash
mvn -Dtest=ProductTest test
mvn -Dtest=ProductLifecycleUseCaseTest test
```

Run the full suite:

```bash
mvn clean test
```

### 4. Output

For a normal promotion flow, it shows that:

- a new product starts in `IN_DEVELOPMENT`
- first `promote()` returns `Product is now TESTED`
- second `promote()` returns `Product is APPROVED`
- third `promote()` returns `Product is now IN_PRODUCTION`
- promoting again returns `Product is already IN_PRODUCTION and cannot be promoted further`

For rejection flow, it shows that:

- reject in `IN_DEVELOPMENT` returns `Cannot reject: product is still IN_DEVELOPMENT`
- reject in `TESTED` changes the status to `REJECTED`

## Local Setup

This project is configured for Java 25 in Maven.

Check your tools:

```bash
java -version
mvn -v
```

Run tests:

```bash
mvn test
```

If IntelliJ uses the wrong JDK, set the project SDK to Java 25 and reimport the Maven project.
