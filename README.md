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
multibank-ui-framework/
│
├── config/            # Browser config, base URL, test settings
├── data/              # External expected-data JSON files
├── pages/             # Page Objects (locators only)
├── services/          # Navigation & UI service layers
├── utils/             # Actions, waits, helpers
└── tests/             # Test classes
```

### Design Principles

✔ Pages contain **only locators**  
✔ Services contain **interaction logic**  
✔ Tests contain **only orchestration and assertions**  
✔ All expected values stored in external data files (DDT)  
✔ Zero hard-coded assertions in test classes

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

---

## Reporting

- Screenshots captured automatically
- Allure provides:
    - UI steps timeline
    - Failure attachments
    - Trend charts
    - Per-test artifacts

---

## Key Components

### Base Structure

- `HeaderPage` — top navigation locators
- `NavigationService` — interactions for nav items, dropdowns, validation
- `TradingPage` — trading UI locators
- `Actions` — wrapper for click, fill, wait, safe operations
- `Assertions` — unified fluent assertion layer

### Data-Driven Approach

Expected menu items, trading categories, footer content, etc. are stored in:

```
/data/navigation.json
/data/trading.json
/data/footer.json
```

This allows easy updates without touching code.

---