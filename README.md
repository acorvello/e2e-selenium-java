# 🏛️ Enterprise-Grade UI Automation Architecture
### Robust End-to-End Test Framework Engineered with Selenium 4, JUnit 5, and Java

This repository represents an enterprise-ready automated testing infrastructure designed under a rigorous **Page Object Model (POM)** architecture pattern. Operating against the **OWASP Juice Shop** platform, this suite demonstrates how to structure decoupled, maintainable, and high-performance automation scripts capable of running seamlessly in heavy enterprise CI/CD loops.

This suite complements the other repositories in the global portfolio ([Cypress](https://github.com), [Playwright](https://github.com), and [API tests](https://github.com)) and runs in the same integrated Jenkins pipeline.

## 💡 Engineering Highlights
* **Page Object Model (POM):** Complete separation of test logic from page locators, reducing maintenance overhead and preventing selector debt.
* **Explicit & Fluent Synchronization:** Implemented robust `WebDriverWait` mechanisms to synchronize animations and element visibility, eliminating brittle execution flakiness without relying on anti-patterns like `Thread.sleep`.
* **Visual Artifact Governance:** Utilizes a custom JUnit 5 extension listener (`TestWatcher`) to automatically capture and dump high-resolution failure PNG screenshots to `target/screenshots/` before session termination.
* **Environment-Driven Flexibility:** Decoupled execution through dynamic `BASE_URL` and `SELENIUM_URL` variable binding, ensuring identical execution parity locally, in Docker, or inside Jenkins.

## 🛠️ Tech Stack & Dependencies
- **Language Core:** Java 17+
- **Automation API:** Selenium WebDriver 4.27 (`RemoteWebDriver` architecture against a Selenium grid container)
- **Execution & Assertions:** JUnit 5 (Jupiter Engine)
- **Build Automation & Reporting:** Maven (Surefire Plugin configuration for JUnit XML absorption)

## 📁 Project Structure
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
## 🔍 Covered Scenarios
- **Core Verification:** Home page loads successfully and aggregates structured product components.
- **Navigation Flow:** Login interface renders correctly when triggered via the account dashboard menu.
- **Exception Handling Validation:** Security barrier triggers error display alerts upon receiving invalid credentials.

## 🚀 Running Locally

**Prerequisites:** JDK 17, Maven, Docker, and a running Juice Shop instance on port 3001.

```bash
docker run -d --name selenium -p 4444:4444 --shm-size=2g selenium/standalone-chrome:4.27.0

BASE_URL=http://host.docker.internal:3001 SELENIUM_URL=http://localhost:4444/wd/hub mvn test
```

🐳 Running Through the Full Pipeline (DevOps)

This repository is actively consumed by the docker-test-env orchestration project. The pipeline starts Juice Shop alongside a Selenium Chrome container, runs this test suite inside an isolated Maven container runner, and automatically publishes unified JUnit results to Jenkins.

📈 Roadmap & Next Steps

• Expand functional E2E validation into multi-step cart and checkout flows.
• Integrate the Selenium Video Container to record and archive full video streams of failed test builds.
• Configure static analysis tracking blocks for this Java repository via SonarQube Quality Gates.

⚖️ License

This repository is licensed under the MIT License. Feel free to clone, study, modify, and distribute the code for educational or portfolio purposes.
---
<sub>Keywords: Selenium 4, Java, JUnit 5, Maven, Page Object Model, POM, Explicit Waits, Enterprise QA, RemoteWebDriver, Jenkins CI/CD, Containerization, Docker, Automation Infrastructure.
