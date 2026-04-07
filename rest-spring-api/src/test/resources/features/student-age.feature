Feature: Student age calculation

  # Describes the expected age behavior for a student born on a known date.

  Scenario: Student born in 2002 has age 23 on reference date in 2025
    # Provide the birth date used to build the Student object in the step definitions.
    Given a student with birth date "2002-04-07"
    # Fix the reference date so the scenario stays deterministic over time.
    And today's date is "2025-04-07"
    # Trigger the age calculation in the Student entity.
    When I calculate the student's age
    # Assert the expected age for the chosen reference date.
    Then the returned age should be 23
