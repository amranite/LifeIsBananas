# Life Is Bananas — domain and design-pattern exercises

Academic Java exercises modelling recipe approval and product development. `main` is the runnable baseline; branches retain course checkpoints and alternative implementations.

Use Java 21 and Maven:

```sh
mvn test
java -cp target/classes org.example.Main
```

The demo runs one recipe/product workflow. `ServiceImpl` is stateful by design at that scope: manual callers create their own instance, and Spring uses prototype scope. It is not intended as a shared multi-user catalogue. Ingredients can be read through an unmodifiable list; changes go through the recipe's operations.

| Branch | Exercise and status |
| --- | --- |
| [design-pattern/state](https://github.com/amranite/LifeIsBananas/tree/design-pattern/state) | State-pattern implementation; Java 25, separately tested. |
| [deel-5/AOP](https://github.com/amranite/LifeIsBananas/tree/deel-5/AOP) | Aspect-oriented service logging on an earlier service architecture. |
| [Singleton](https://github.com/amranite/LifeIsBananas/tree/Singleton) | Shared production-settings example; existing tests pass, lazy initialization is unsynchronized. |
| [Persistentie](https://github.com/amranite/LifeIsBananas/tree/Persistentie) | Hibernate persistence alternative; requires separate database setup. |
| [Facotory](https://github.com/amranite/LifeIsBananas/tree/Facotory) | Unfinished factory exercise; abstract-recipe callers still require adaptation. |
| [deel-5/dependency-injection](https://github.com/amranite/LifeIsBananas/tree/deel-5/dependency-injection) | Constructor-injection checkpoint; historical tests still call the removed constructor. |
| [CRUD](https://github.com/amranite/LifeIsBananas/tree/CRUD) | Earlier MVC/product-list experiment; Java tests were removed in this variant. |
| [sequence-test](https://github.com/amranite/LifeIsBananas/tree/sequence-test) | Earlier service API and sequence-test exercise. |

`deel-2/UCT`, `deel-3/acceptance-tests`, `deel-4/service-impl`, `spring`, and `test/unit-test` are checkpoints already in main's ancestry. `feat/spring-di` and its revert record an architecture experiment and its reversal. `Client/server` duplicates the earlier main checkpoint. Branches are alternatives and historical exercises, not promises of a single cumulative feature set.

The original collaborative commit history, author metadata and commit hashes are retained unchanged. Cleanup is recorded in new commits. This is an educational project using the Life Is Bananas theme, with no claim of company affiliation.
