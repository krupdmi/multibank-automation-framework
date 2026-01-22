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

### Run full test suite

```bash
mvn -pl test-automation-ui test
```

### Generate Allure report after tests execution

```bash
mvn -pl test-automation-ui allure:serve
```

### Run with parameters

```bash
mvn -pl test-automation-ui test -Dlang=en -Dbrowser=chromium -Dplatform=desktop
mvn -pl test-automation-ui test -Dlang=es -Dbrowser=webkit -Dplatform=mobile
mvn -pl test-automation-ui test -Dlang=de -Dbrowser=firefox -Dheadless=true
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
export HEADLESS=true
export RETRY_COUNT=5
export RETRY_DELAY_MS=1000
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

### Roadmap

- CI/CD integration with retries at test level

---