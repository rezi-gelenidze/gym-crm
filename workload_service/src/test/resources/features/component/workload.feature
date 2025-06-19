Feature: Trainer Workload Management
  Background:
    Given the workload service is available

  Scenario: Successfully record a new trainer workload via JMS message
    When a workload message is received with ADD action type
    Then the workload data should be stored in MongoDB
    And the trainer should have the correct workload duration

  Scenario: Successfully update an existing trainer workload
    Given a trainer already has workload data
    When a workload message is received with ADD action type
    Then the workload data should be updated in MongoDB
    And the trainer workload should be increased accordingly

  Scenario: Successfully decrease a trainer workload
    Given a trainer already has workload data
    When a workload message is received with DELETE action type
    Then the workload data should be updated in MongoDB
    And the trainer workload should be decreased accordingly

  Scenario: Retrieve a trainer's workload information via API
    Given a trainer has workload data across multiple months
    When I request the workload data for the trainer via the API
    Then the response status code should be 200
    And the response should contain the correct workload data

  Scenario: Attempt to retrieve workload for a non-existent trainer
    When I request the workload data for a non-existent trainer
    Then the response status code should be 404

  Scenario: Handle invalid JMS message
    When an invalid workload message is received
    Then the service should handle the error gracefully