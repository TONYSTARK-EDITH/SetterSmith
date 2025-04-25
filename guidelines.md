# SetterSmith Development Guidelines

This document provides essential information for developers working on the SetterSmith IntelliJ IDEA plugin project.

## Build/Configuration Instructions

### Project Setup

SetterSmith is an IntelliJ IDEA plugin built using Gradle with the Kotlin DSL. The project uses the IntelliJ Platform Gradle Plugin for building and deploying the plugin.

### Prerequisites

- JDK 17 or later
- IntelliJ IDEA (Community or Ultimate)
- Gradle 8.0 or later (or use the included Gradle wrapper)

### Build Configuration

The project uses the following Gradle configuration files:

- `build.gradle.kts`: Main build script
- `settings.gradle.kts`: Project settings
- `gradle.properties`: Gradle and project properties

Key properties in `gradle.properties`:

```properties
# IntelliJ Platform configuration
platformType = IC       # IC for IntelliJ IDEA Community, IU for Ultimate
platformVersion = 2023.3  # Target IntelliJ IDEA version
```

### Building the Project

To build the project, run:

```bash
./gradlew build
```

This will compile the code, run tests, and create a plugin distribution in the `build/distributions` directory.

### Running the Plugin

To run the plugin in a development instance of IntelliJ IDEA:

```bash
./gradlew runIde
```

This will start a new instance of IntelliJ IDEA with the plugin installed.

## Testing Information

### Test Structure

Tests are organized in a directory structure that mirrors the main source code:

```
src/
├── main/
│   └── java/
│       └── org/
│           └── stark/
│               └── settersmith/
│                   ├── actions/
│                   └── util/
└── test/
    └── java/
        └── org/
            └── stark/
                └── settersmith/
                    ├── actions/
                    └── util/
```

### Test Dependencies

The project uses JUnit 4 for testing. The test dependencies are configured in the `build.gradle.kts` file:

```kotlin
dependencies {
    testImplementation("junit:junit:4.13.2")
}
```

### Writing Tests

Tests should be placed in the `src/test/java` directory with a package structure that mirrors the main source code. Test classes should be named with a `Test` suffix.

Example test class:

```java
package org.stark.settersmith.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void testCapitalize() {
        Assert.assertEquals("Hello", StringUtils.capitalize("hello"));
        Assert.assertEquals("Hello", StringUtils.capitalize("Hello"));
        Assert.assertEquals("A", StringUtils.capitalize("a"));
        Assert.assertEquals("", StringUtils.capitalize(""));
        Assert.assertNull(StringUtils.capitalize(null));
    }
}
```

### Running Tests

To run all tests:

```bash
./gradlew test
```

To run a specific test class:

```bash
./gradlew test --tests "org.stark.settersmith.util.StringUtilsTest"
```

### Testing IntelliJ Platform Components

Testing components that depend on the IntelliJ Platform (like actions and PSI operations) requires special setup:

1. Add the IntelliJ Platform Test Framework dependency:

```kotlin
dependencies {
    testImplementation("org.jetbrains.intellij.platform:test-framework:$platformVersion")
}
```

2. Extend the appropriate test base class (e.g., `BasePlatformTestCase` for platform tests or `LightJavaCodeInsightFixtureTestCase` for Java-specific tests).

3. Use the provided test fixtures to set up the test environment.

Example:

```java
package org.stark.settersmith.actions;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class GenerateSetterActionTest extends BasePlatformTestCase {
    public void testActionAvailability() {
        // Test code here
    }
}
```

## Additional Development Information

### Code Style

- Follow the standard Java code style conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public classes and methods
- Keep methods focused on a single responsibility

### Plugin Structure

The plugin consists of the following main components:

1. **Actions**: Classes in the `org.stark.settersmith.actions` package that implement IntelliJ IDEA actions
   - `GenerateSetterAction`: The main action that generates setter method calls

2. **Utilities**: Helper classes in the `org.stark.settersmith.util` package
   - `StringUtils`: Utility methods for string manipulation

### Plugin Configuration

The plugin is configured in the `plugin.xml` file located in the `src/main/resources/META-INF` directory. This file defines:

- Plugin metadata (ID, name, vendor)
- Dependencies on IntelliJ Platform modules
- Action registrations
- Extension points

### Debugging

To debug the plugin:

1. Run the plugin in debug mode:

```bash
./gradlew runIde --debug-jvm
```

2. Connect your IDE's debugger to the running process.

### Deployment

To build a deployable plugin zip file:

```bash
./gradlew buildPlugin
```

The plugin zip file will be created in the `build/distributions` directory.

### Publishing

To publish the plugin to the JetBrains Plugin Repository:

1. Set up your JetBrains Plugin Repository token in the environment variable `PUBLISH_TOKEN`
2. Run:

```bash
./gradlew publishPlugin
```
