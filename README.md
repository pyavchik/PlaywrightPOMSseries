# Playwright Page Object Model with Java

This project demonstrates a complete Page Object Model (POM) implementation using Playwright and Java.

## Project Structure

```
PlaywrightPOMSeries/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── playwright/
│   │               └── pom/
│   │                   ├── base/
│   │                   │   ├── BasePage.java      # Base class for all page objects
│   │                   │   └── BaseTest.java     # Base class for all tests
│   │                   └── pages/
│   │                       ├── LoginPage.java
│   │                       ├── HomePage.java
│   │                       └── CheckboxesPage.java
│   └── test/
│       └── java/
│           └── com/
│               └── playwright/
│                   └── pom/
│                       └── tests/
│                           ├── LoginTest.java
│                           ├── CheckboxesTest.java
│                           └── HomePageTest.java
├── pom.xml
├── testng.xml
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Install Playwright Browsers

After cloning the project, you need to install Playwright browsers:

```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

Or use the Playwright CLI directly if you have it installed:

```bash
npx playwright install
```

### 2. Run Tests

#### Run all tests:
```bash
mvn test
```

#### Run specific test class:
```bash
mvn test -Dtest=LoginTest
```

#### Run tests with specific browser:
```bash
mvn test -Dbrowser=chromium
mvn test -Dbrowser=firefox
mvn test -Dbrowser=webkit
```

#### Run tests in headed mode (see browser):
```bash
mvn test -Dheadless=false
```

#### Run tests with custom base URL:
```bash
mvn test -Dbase.url=https://example.com
```

## Page Object Model Pattern

### BasePage Class

The `BasePage` class provides common functionality for all page objects:
- Navigation methods
- Element interaction methods (click, type, getText)
- Wait methods
- Screenshot capabilities

### BaseTest Class

The `BaseTest` class handles:
- Browser initialization
- Page creation
- Test setup and teardown
- Browser context management

### Page Objects

Each page object extends `BasePage` and contains:
- Locators (as private final fields)
- Page-specific methods
- Business logic related to that page

### Example Usage

```java
public class LoginTest extends BaseTest {
    @Test
    public void testLogin() {
        LoginPage loginPage = new LoginPage(getPage());
        loginPage.navigateToLoginPage();
        loginPage.login("username", "password");
        Assert.assertTrue(loginPage.isLoginSuccessful());
    }
}
```

## Configuration

### System Properties

- `browser`: Browser type (chromium, firefox, webkit) - default: chromium
- `headless`: Run in headless mode (true/false) - default: true
- `base.url`: Base URL for the application - default: https://the-internet.herokuapp.com

### TestNG Configuration

The `testng.xml` file configures:
- Test suite name
- Parallel execution
- Thread count
- Test classes to run

## Best Practices

1. **Locators**: Store all locators as private final fields in page objects
2. **Methods**: Create methods that represent user actions, not just element interactions
3. **Reusability**: Use BasePage for common functionality
4. **Maintainability**: Keep page objects focused on a single page
5. **Test Data**: Consider using external data sources for test data
6. **Assertions**: Keep assertions in test classes, not in page objects

## Adding New Page Objects

1. Create a new class in `src/main/java/com/playwright/pom/pages/`
2. Extend `BasePage`
3. Define locators as private final fields
4. Create methods for user actions
5. Create corresponding test class in `src/test/java/com/playwright/pom/tests/`

## Troubleshooting

### Browser Installation Issues

If browsers are not installed:
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
```

### Test Failures

- Check if the base URL is accessible
- Verify browser installation
- Check network connectivity
- Review test logs in `test-results/` directory

## Resources

- [Playwright Java Documentation](https://playwright.dev/java/)
- [TestNG Documentation](https://testng.org/doc/documentation-main.html)
- [Page Object Model Pattern](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)

