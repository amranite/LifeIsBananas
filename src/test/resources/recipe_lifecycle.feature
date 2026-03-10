Feature: Recipe Lifecycle Management
  As a bio-engineer
  I want to manage the lifecycle stages of a recipe
  So that only verified and approved recipes are used for production

  Scenario: Submit a draft recipe for testing
    Given a recipe has status "DRAFT"
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

  # Negative test
  Scenario: Attempting to edit an approved recipe
    Given a recipe has status "APPROVED"
    When the bio-engineer tries to add an ingredient
    Then the system should refuse the update with a "locked" error
    And the recipe status should remain "APPROVED"