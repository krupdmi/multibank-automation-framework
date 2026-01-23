# MultiBank UI Automation Framework

Author: **Dmitrii Krupa**

## Technology Stack

### Core

- **Java 21**
- **Maven**
- **JUnit 5**

### UI Automation

- **Playwright for Java**
- **Page Object Model (POM)**
- **Data-Driven Testing (DDT)**

### Reporting

- **Allure Reports**
- **Screenshots on failure**

### Cross-Browser Support

- Chromium
- Firefox
- WebKit

### Multi-Language Support

- English (en)
- German (de)
- Spanish (es)

### Platform Support

- Desktop
- Mobile

## Architecture Overview

```
multibank-automation-framework/
│
├── core-automation/         # Playwright engine, sessions, retry utils
│   ├── playwright/          # PlaywrightSession, provider, config
│   ├── utils/               # Retry utils, common helpers
│
├── test-automation-ui/
│   ├── config/              # UI test config loading (JSON)
│   ├── data/                # Navigation/menu expected values
│   ├── pages/               # Page Objects (locators only)
│   ├── utils/               # Actions, waits, helpers
│   ├── assertions/          # Unified soft assertion layer
│   ├── tests/               # Test classes
```

---

## Running Tests

### Run from IDE

You can run tests directly from your IDE (IntelliJ IDEA, Eclipse):

- **Single test class**: Right-click on any test class (e.g. `NavigationTest.java`) and select "Run"
- **Full regression suite**: Run the `RegressionSuite.java` class located at:
  ```
  test-automation-ui/src/test/java/org/multibank/ui/suites/RegressionSuite.java
  ```
  This suite includes all tests tagged with `@Tag("Regression")`

### Run from command line

```bash
mvn -pl test-automation-ui test
```

### Generate Allure report after tests execution

```bash
mvn -pl test-automation-ui allure:serve
```

### Run with parameters

```bash
mvn -pl test-automation-ui test -Dlang=de -Dbrowser=chromium -Dplatform=desktop
mvn -pl test-automation-ui test -Dlang=en -Dbrowser=webkit -Dplatform=mobile
mvn -pl test-automation-ui test -Dlang=es -Dbrowser=firefox -Dheadless=true
```

### Run with Docker

Docker support allows running tests in an isolated environment with all dependencies.

#### Build the Docker image

```bash
docker-compose build
```

#### Run tests with default configuration

```bash
docker-compose run ui-tests
```

#### Run with custom configuration

You can override any environment variable:
```bash
export BASE_URL=https://trade.multibank.io/
export BROWSER=chromium
export ENVIRONMENT=PROD
export EXECUTION=docker
export LANG=en
export PLATFORM=desktop
export HEADLESS=false
export RETRY_COUNT=5
export RETRY_DELAY_MS=1000
export TEST_RETRY_COUNT=3
```

#### Available environment variables

- `BASE_URL` - Application URL (default: https://trade.multibank.io/)
- `BROWSER` - Browser: chromium, firefox, webkit (default: chromium)
- `ENVIRONMENT` - Environment name (default: PROD)
- `EXECUTION` - Execution mode (default: docker)
- `LANG` -  Language (default: en)
- `PLATFORM` - Platform (default: desktop)
- `HEADLESS` - Headless mode (default: false)
- `RETRY_COUNT` - Number of retries for flaky tests (default: 3)
- `RETRY_DELAY_MS` - Delay between retries in milliseconds (default: 1000)
- `TEST_RETRY_COUNT` - Number of attempts to rerun failed scenarios

#### Access test results

Test results are automatically stored locally:

```bash
./test-automation-ui/allure-results/
```

---

### Data-Driven Approach

Test data is stored externally in JSON format, for example:

```
/testdata/${lang}/content-validation.json
/testdata/${lang}/navigation-menu-items.json
/testdata/${lang}/spot-trading.json
```

This allows easy updates without touching the code.

---

## Parallel Execution

Tests can be run in parallel using JUnit 5 parallel execution. Configuration is located at:

```
test-automation-ui/src/test/resources/junit-platform.properties
```

Current settings:
```properties
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.config.fixed.parallelism = 2
```

Adjust `parallelism` value to control the number of concurrent test threads.

---

## Automatic Test Retries

Failed tests are automatically retried using Maven Surefire plugin. Configuration in `test-automation-ui/pom.xml`:

```xml
<configuration>
    <rerunFailingTestsCount>${test.retry.count}</rerunFailingTestsCount>
</configuration>
```

Default retry count is **3**. Override via command line:

```bash
mvn -pl test-automation-ui test -Dtest.retry.count=1
```

---

## Test Evidence

Successful test execution screenshots are stored in the `test-evidence/` folder in the project root with the proof of successful test runs.

---

## Roadmap

### CI/CD Integration

The framework is designed for CI/CD pipeline integration with support for parallel matrix execution:

**Strategy example:**

```yaml
strategy:
  testMatrix:
    browser: [chromium, firefox, webkit]
    platform: [desktop, mobile]
    lang: [en, de, es]
```

**CI/CD capabilities:**

- Run tests in parallel across multiple browsers and platforms
- Each matrix combination runs as a separate job in isolated container
- Automatic test retries handle flaky tests
- Allure reports aggregated from all parallel runs
- Docker execution ensures consistent environment across CI runners

**Example of Jenkins pipeline stages:**

1. Build Docker image
2. Run test matrix in parallel (browser, platform, language)
3. Collect and merge Allure results
4. Generate combined test report and deliver it via email or messenger

---