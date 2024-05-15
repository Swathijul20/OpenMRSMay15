Feature: To Validate Registration Desk

  Scenario: To verify registration desk functionality
    Given To launch any browser and url of application
    When To pass valid credentials in email and password field
    And To pick any location and click on Login and verify user redirected to dashboard
    And To click patient register menu and fill all details in demographics and contact info
    And To confirm start visit then attaching problems then verifying successfully attached message and ending visit
    And To visit again for body bmi calculation
		And To add past visit and delete the records