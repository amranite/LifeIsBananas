Feature: Raw material CRUD management
  As a lab coordinator
  I want to maintain the raw material catalogue
  So that the banana product team can work with correct ingredient data

  Scenario: Register a raw material
    Given I am on the raw material registration page
    When I enter code "RM-001"
    And I enter name "Organic Banana Puree"
    And I enter amount "6.00"
    And I enter price "4.75"
    And I choose unit "LITER"
    And I mark the raw material as organic certified
    And I save the raw material
    Then I should see the raw material details

  Scenario: Update a raw material
    Given a raw material with code "RM-002" already exists
    When I choose to edit the raw material
    And I enter name "Banana Powder Premium"
    And I enter amount "4.50"
    And I enter price "13.95"
    And I choose unit "KG"
    And I save the raw material
    Then I should see the updated raw material details

  Scenario: Delete a raw material
    Given a raw material with code "RM-003" already exists
    When I delete the raw material
    Then the raw material should no longer appear in the list
