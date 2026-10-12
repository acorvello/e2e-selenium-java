# e2e-selenium-java

End-to-end UI tests built with **Selenium 4 and JUnit 5 (Java)**, validating the
login flow of the [OWASP Juice Shop](https://owasp.org/www-project-juice-shop/),
used here as a target application for test automation practice.

This suite complements the other repositories in the portfolio
([Cypress](https://github.com/acorvello/e2e-cypress-js),
[Playwright](https://github.com/acorvello/e2e-playwright-python) and
[API tests](https://github.com/acorvello/api-testing)) and runs in the same
Jenkins pipeline.

## Stack

- Java 17
- Selenium 4.27 (`RemoteWebDriver` against a Selenium Chrome container)
- JUnit 5
- Maven (Surefire for JUnit XML reports)

## Design

- **Page Object Model**: `HomePage` and `LoginPage` hide locators and waiting
  logic, so tests read as user intent.
- **Explicit waits only**: no `Thread.sleep` and no implicit waits. Clicks are
  retried while an animation or overlay is in the way, but a real user must
  still be able to perform the action (no forced JavaScript clicks).
- **Screenshot on failure**: a JUnit 5 extension saves a PNG to
  `target/screenshots/` before the browser is closed.
- **Environment-driven**: `BASE_URL` and `SELENIUM_URL` select the target
  application and the Selenium server, so the same code runs locally, in
  Docker and in Jenkins.

## Project structure

```
e2e-selenium-java/
├── pom.xml
└── src/test/java/io/github/acorvello/selenium/
    ├── support/BaseTest.java
    ├── pages/
    │   ├── BasePage.java
    │   ├── HomePage.java
    │   └── LoginPage.java
    └── tests/JuiceShopLoginTest.java
```

## Covered scenarios

- Home page loads and lists products
- Login form opens from the account menu
- An error message is shown for invalid credentials

## Running locally

Prerequisites: JDK 17, Maven, Docker, and a running Juice Shop on port 3001.

```bash
docker run -d --name selenium -p 4444:4444 --shm-size=2g selenium/standalone-chrome:4.27.0

BASE_URL=http://host.docker.internal:3001 SELENIUM_URL=http://localhost:4444/wd/hub mvn test
```

`host.docker.internal` lets the Chrome container reach the application running
on your machine.

## Running through the full pipeline

This repository is consumed by the
[docker-test-env](https://github.com/acorvello/docker-test-env) orchestration
project, which starts Juice Shop and a Selenium Chrome container, runs this
suite inside a Maven container and publishes the JUnit results to Jenkins.

## Next steps

- Cover cart and checkout flows
- Record video of failed runs with the Selenium video container
- Static analysis of this code with SonarQube
