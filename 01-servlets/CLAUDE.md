# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Educational module for FHNW's "Web Frameworks" course (FS26). Teaches Java Servlet fundamentals before introducing Spring Boot. Written in German. Contains three assignments (Arbeitsblätter):

- **ab1.1**: Development environment setup (Tomcat 11, JDK, IDE)
- **ab1.2** (`initial/`): Study a flashcard Servlet app — understand lifecycle, routing, deployment
- **ab1.3** (`complete/`): Extend BasicServlet with detail view (parametrized routes)

## Build & Test Commands

Each assignment directory (`ab1.2/initial/`, `ab1.3/complete/`) is an independent Gradle project. Run commands from within the project directory.

```bash
# Build WAR
./gradlew war
# Output: build/libs/flashcard-basic.war

# Run tests (JUnit 5)
./gradlew test

# Clean build
./gradlew clean war
```

## Tech Stack

- **Java 21** (toolchain enforced in build.gradle)
- **Jakarta Servlet API 11.0.10** (Tomcat 11)
- **Gradle** with wrapper (no global install needed)
- **JUnit 5** for testing
- **Apache Tomcat 11.x** as servlet container

## Architecture

Classic layered servlet architecture (no framework):

```
HTTP Request → BasicServlet (web/) → QuestionnaireRepository (persistence/) → Questionnaire (domain/)
                    ↓
              HTML response (generated inline)
```

- **`web/BasicServlet`**: Extends `HttpServlet`, manually parses URL paths from `request.getRequestURI()` to route requests. Generates HTML directly in Java code.
- **`persistence/QuestionnaireRepository`**: In-memory ArrayList-based store. Not thread-safe (intentional — demonstrates why frameworks help).
- **`domain/Questionnaire`**: Simple POJO (id, title, description).
- **`util/QuestionnaireInitializer`**: Populates test data during `Servlet.init()`.
- **`webapp/WEB-INF/web.xml`**: Maps `/app/*` to BasicServlet.

## Deployment

Build the WAR, then either:
- Copy to `$TOMCAT_HOME/webapps/` and start Tomcat
- Use Docker: `docker-compose up` with WAR in `./webapps/` volume mount

App URL: `http://localhost:8080/flashcard-basic/app`

## Key Package

`ch.fhnw.webfr.flashcard` — all source lives under this package across `web`, `persistence`, `domain`, and `util` sub-packages.
