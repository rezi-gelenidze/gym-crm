Feature: Training and Workload Service Integration
  # Positive Scenarios
  Scenario: Creating a training updates trainer workload
    When I create a training with trainer "test_trainer" for 60 minutes
    Then the training should be saved successfully
    And trainer "test_trainer" workload should be increased by 60 minutes

  Scenario: Deleting a training updates trainer workload
    Given a training exists for trainer "test_trainer" with 45 minutes duration
    When I delete the training
    Then the training should be removed successfully
    And trainer "test_trainer" workload should be decreased by 45 minutes

  Scenario: Creating multiple trainings for the same trainer
    When I create a training with trainer "test_trainer" for 30 minutes
    Then the training should be saved successfully
    When I create another training with trainer "test_trainer" for 45 minutes
    Then the training should be saved successfully
    And trainer "test_trainer" workload should be increased by 75 minutes

  Scenario: Creating and deleting trainings for multiple trainers
    When I create a training with trainer "trainer_a" for 60 minutes
    Then the training should be saved successfully
    When I create a training with trainer "trainer_b" for 90 minutes
    Then the training should be saved successfully
    And trainer "trainer_a" workload should be increased by 60 minutes
    And trainer "trainer_b" workload should be increased by 90 minutes

  # Negative Scenarios
  Scenario: Creating a training with non-existent trainer
    When I create a training with non-existent trainer "unknown_trainer"
    Then the training creation should fail with status 404
    And no workload update should be sent to workload service

  Scenario: Creating a training with invalid duration
    When I create a training with trainer "test_trainer" for -10 minutes
    Then the training creation should fail with status 400
    And no workload update should be sent to workload service

  Scenario: Handling concurrent workload updates
    When I create 5 trainings simultaneously for trainer "test_trainer"
    Then all trainings should be saved successfully
    And trainer "test_trainer" workload should reflect the total duration

  Scenario: Recovering from temporary workload service outage
    Given the workload service is temporarily unavailable
    When I create a training with trainer "test_trainer" for 30 minutes
    Then the training should be saved successfully
    When the workload service becomes available again
    Then trainer "test_trainer" workload should be increased by 30 minutes

  Scenario: Create training with zero duration
    When I create a training with trainer "test_trainer" for 0 minutes
    Then the training should be saved successfully
    And trainer "test_trainer" workload should not be changed