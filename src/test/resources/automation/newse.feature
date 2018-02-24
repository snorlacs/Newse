Feature: Editor can successfully create, update and delete articles

  Scenario: Editor gets a created article
    When the editor creates an article
    And the article is created successfully
    And the user gets the article
    Then receives 200 status code
    And the retrieved article is correct

  Scenario: Editor updates an existing article
    Given an article exists
    When the editor updates the article
    And receives 200 status code
    And the user gets the article
    Then receives 200 status code
    And the article is updated

  Scenario: Editor deletes an existing article
    Given an article exists
    When the editor deletes the article
    And receives 204 status code
    And the user gets the article
    Then receives 404 status code
