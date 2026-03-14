Feature: Product Lifecycle Management
  As a bio-engineer
  I want to promote products through development stages
  So that I can eventually launch them into production

  # ── HAPPY PATH: valid status transitions ──────────────────────────────────

  Scenario: Submit a product in development for testing
    Given a product has status "IN_DEVELOPMENT"
    When the bio-engineer submits the product for testing
    Then the product status should change to "TESTED"

  Scenario: Approve a product after successful testing
    Given a product has status "TESTED"
    When the bio-engineer approves the product
    Then the product status should change to "APPROVED"

  Scenario: Reject a product during the testing phase
    Given a product has status "TESTED"
    When the bio-engineer rejects the product
    Then the product status should change to "REJECTED"

  Scenario: Launch an approved product into production
    Given a product has status "APPROVED"
    When the bio-engineer launches the product into production
    Then the product status should change to "IN_PRODUCTION"

  Scenario: Full recipe-to-production lifecycle sequence
    Given a recipe has status "DRAFT"
    And the recipe has at least one ingredient
    When the bio-engineer submits the recipe for testing
    And the bio-engineer approves the recipe
    And the bio-engineer creates a product with ID "PROD-100" and name "Full Flow Product"
    And the bio-engineer submits the product for testing
    And the bio-engineer approves the product
    And the bio-engineer launches the product into production
    Then the product status should change to "IN_PRODUCTION"

  # ── NEGATIVE TESTS: blocked transitions ───────────────────────────────────

  Scenario: Cannot reject a product that is still in development
    Given a product has status "IN_DEVELOPMENT"
    When the bio-engineer rejects the product
    Then the product rejection should fail with message "Cannot reject: product is still IN_DEVELOPMENT"
    And the product status should remain "IN_DEVELOPMENT"

  Scenario: Cannot reject a product that is already in production
    Given a product has status "IN_PRODUCTION"
    When the bio-engineer rejects the product
    Then the product rejection should fail with message "Cannot reject: product is already IN_PRODUCTION"
    And the product status should remain "IN_PRODUCTION"

  Scenario: Cannot promote a product that is already in production
    Given a product has status "IN_PRODUCTION"
    When the bio-engineer attempts to launch the product again
    Then the system should indicate that this is the final state
    And the product status should remain "IN_PRODUCTION"
