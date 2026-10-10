Feature: Raw material CRUD management
  As a lab coordinator
  I want to maintain the raw material catalogue
  So that the banana product team can work with correct ingredient data

  Scenario: Add one raw material - see its details
    Given I am on the page where I can register a new raw material
    When I enter "AT-RM-001" in the code field
    And I enter "Organic Banana Puree" in the name field
    And I enter "6.00" in the amount field
    And I enter "4.75" in the price field
    And I choose "LITER" in the unit field
    And I mark the raw material as organic certified
    And I press on the Submit button
    Then I should see the following raw material details
      | label              | data                  |
      | Code:              | AT-RM-001             |
      | Name:              | Organic Banana Puree  |
      | Organic certified: | Yes                   |
      | Amount:            | 6.00                  |
      | Price:             | 4.75                  |
      | Unit:              | LITER                 |
    And I close the browser

  Scenario: Add one raw material - see it in the list
    Given I am on the page where I can register a new raw material
    When I enter "AT-RM-002" in the code field
    And I enter "Banana Powder" in the name field
    And I enter "2.50" in the amount field
    And I enter "12.50" in the price field
    And I choose "KG" in the unit field
    And I mark the raw material as organic certified
    And I press on the Submit button
    And I click the Raw materials list link
    Then I should see a raw materials list containing "Banana Powder"
    And I close the browser

  Scenario: Update a raw material - see the changed details
    Given I have registered a raw material with code "AT-RM-003"
    When I click the Edit raw material link
    And I enter "Banana Powder Premium" in the name field
    And I enter "4.50" in the amount field
    And I enter "13.95" in the price field
    And I choose "KG" in the unit field
    And I press on the Submit button
    Then I should see the following raw material details
      | label   | data                  |
      | Name:   | Banana Powder Premium |
      | Amount: | 4.50                  |
      | Price:  | 13.95                 |
      | Unit:   | KG                    |
    And I close the browser

  Scenario: Delete a raw material - it disappears from the list
    Given I have registered a raw material with code "AT-RM-004"
    When I press on the Delete raw material button
    Then I should not see a raw materials list containing "Banana Powder"
    And I close the browser
