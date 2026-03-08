Feature: Product Lifecycle Management
  As a bio-engineer
  I want to promote products through development stages
  So that I can eventually launch them into production

  Scenario: Submit a product in development for testing
    Given a product has status "IN_DEVELOPMENT"
    When the bio-engineer executes the "submitForTesting()" action
    Then the product status should change to "TESTED"

  Scenario: Reject a product during the testing phase
    Given a product has status "TESTED"
    When the bio-engineer executes the "reject()" action
    Then the product status should change to "REJECTED"

  Scenario: Approve a product for launch
    Given a product has status "TESTED"
    When the bio-engineer executes the "approve()" action
    Then the product status should change to "APPROVED"

  Scenario: Launch an approved product into production
    Given a product has status "APPROVED"
    When the bio-engineer executes the "launch()" action
    Then the product status should change to "IN_PRODUCTION"

  # Negative test
  Scenario: Attempting to promote a product that is already in production
    Given a product has status "IN_PRODUCTION"
    When the bio-engineer attempts to "launch()" again
    Then the system should indicate that this is the final state
    And the product status should remain "IN_PRODUCTION"