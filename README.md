# MultiBank UI Automation Framework

Author: **Dmitrii Krupa**

## 🛠️ Technology Stack

### Core

- **Java 17**
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

### Run full test suite

```bash
mvn test
```

### Run with Allure reporting

```bash
mvn clean test
mvn allure:serve
```

### Select browser

```bash
mvn test -Dbrowser=firefox
mvn test -Dbrowser=webkit
```

### Run with Docker

Docker support allows running tests in an isolated environment with all dependencies.

#### Build the Docker image

```bash
docker-compose build --no-cache
```

#### Run tests with default configuration

```bash
docker-compose run ui-tests
```

#### Run with custom configuration

You can override any environment variable:
```bash
BASE_URL=https://example.site.com BROWSER=webkit docker-compose run ui-tests
```

#### Available environment variables

- `BASE_URL` - Application URL (default: https://trade.multibank.io/)
- `BROWSER` - Browser: chromium, firefox, webkit (default: chromium)
- `ENVIRONMENT` - Environment name (default: PROD)
- `EXECUTION` - Execution mode (default: docker)
- `RETRY_COUNT` - Number of retries for flaky tests (default: 3)
- `RETRY_DELAY_MS` - Delay between retries in milliseconds (default: 1000)

#### Access test results

Test results are automatically stored locally:

```bash
./test-automation-ui/allure-results/
```

---

### Data-Driven Approach

Expected test data for web pages are stored in:

```
/testdata/navigation.json
/testdata/trading.json
/testdata/footer.json
```

This allows easy updates without touching code.

---

### Roadmap

- Multi-browser grid execution
- Automated retries at test level
- CI/CD integration
- API module creation

---