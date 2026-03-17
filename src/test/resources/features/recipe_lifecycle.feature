Feature: Recipe Lifecycle Management
  As a bio-engineer
  I want to manage the lifecycle stages of a recipe
  So that only verified and approved recipes are used for production

  # ── HAPPY PATH: valid status transitions ──────────────────────────────────

  Scenario: Submit a draft recipe with ingredients for testing
    Given a recipe has status "DRAFT"
    And the recipe has at least one ingredient
    When the bio-engineer submits the recipe for testing
    Then the recipe status should change to "TESTING"

  Scenario: Approve a recipe after successful testing
    Given a recipe has status "TESTING"
    When the bio-engineer approves the recipe
    Then the recipe status should change to "APPROVED"

  Scenario: Reject a recipe due to composition errors
    Given a recipe has status "TESTING"
    When the bio-engineer rejects the recipe
    Then the recipe status should change to "REJECTED"

  Scenario: Create a product from an approved recipe
    Given a recipe has status "APPROVED"
    When the bio-engineer creates a product with ID 1 and name "Banana Crunch Snack"
    Then a product with status "IN_DEVELOPMENT" should be returned
    And the product should be linked to the recipe

  # ── NEGATIVE TESTS: blocked transitions ───────────────────────────────────

  Scenario: Cannot promote a draft recipe without ingredients
    Given a recipe has status "DRAFT"
    And the recipe has no ingredients
    When the bio-engineer submits the recipe for testing
    Then the recipe promotion should fail with message "Cannot promote: recipe has no ingredients"
    And the recipe status should remain "DRAFT"

  Scenario: Cannot reject a recipe in draft status
    Given a recipe has status "DRAFT"
    And the recipe has at least one ingredient
    When the bio-engineer rejects the recipe
    Then the recipe rejection should fail with message "Cannot reject: recipe is still in DRAFT"
    And the recipe status should remain "DRAFT"

  Scenario: Cannot reject an already approved recipe
    Given a recipe has status "APPROVED"
    When the bio-engineer rejects the recipe
    Then the recipe rejection should fail with message "Cannot reject: recipe is already APPROVED"
    And the recipe status should remain "APPROVED"

  Scenario: Cannot promote a recipe beyond APPROVED
    Given a recipe has status "APPROVED"
    When the bio-engineer submits the recipe for testing
    Then the recipe promotion should fail with message "Recipe is already APPROVED and cannot be promoted further"
    And the recipe status should remain "APPROVED"

  Scenario: Cannot add an ingredient to an approved recipe
    Given a recipe has status "APPROVED"
    When the bio-engineer tries to add an ingredient
    Then the system should refuse the update with a "locked" error
    And the recipe status should remain "APPROVED"

  Scenario: Cannot create a product from a non-approved recipe
    Given a recipe has status "DRAFT"
    And the recipe has at least one ingredient
    When the bio-engineer creates a product with ID 1 and name "Banana Crunch Snack"
    Then the system should throw an illegal state exception
