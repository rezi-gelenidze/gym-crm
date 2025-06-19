Feature: Training Creation
  As a user of the gym management system
  I want to create training sessions
  So that I can manage workout schedules for trainees

  Background:
    Given the training service is available

  Scenario: Successfully create a valid training
    When I create a training with valid information
    Then the training should be created successfully
    And the workload service should be notified with ADD type
    And the response status code should be 201

  Scenario Outline: Fail to create a training with missing required field
    When I create a training with missing "<field>"
    Then the response status code should be 400
    And the error code should be "VALIDATION_ERROR"

    Examples:
      | field            |
      | traineeUsername  |
      | trainerUsername  |
      | trainingName     |
      | trainingDate     |
      | trainingDuration |

  Scenario: Fail to create a training with non-existent trainee
    When I create a training with a non-existent trainee
    Then the response status code should be 404
    And the error code should be "USER_NOT_FOUND"

  Scenario: Fail to create a training with non-existent trainer
    When I create a training with a non-existent trainer
    Then the response status code should be 404
    And the error code should be "USER_NOT_FOUND"

  Scenario: Fail to create a training with empty training name
    When I create a training with an empty training name
    Then the response status code should be 400
    And the error code should be "VALIDATION_ERROR"

  Scenario: Fail to create a training with negative duration
    When I create a training with a negative duration
    Then the response status code should be 400
    And the error code should be "VALIDATION_ERROR"

  Scenario: Successfully create a training with minimum required information
    When I create a training with minimum required information
    Then the training should be created successfully
    And the workload service should be notified with ADD type
    And the response status code should be 201