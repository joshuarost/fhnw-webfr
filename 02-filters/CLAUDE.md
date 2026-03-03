# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Educational Java/Jakarta EE training project (FHNW Webframeworks, Lektion 2) teaching Servlet components: Filters, Listeners, and Response Wrappers. Each assignment has an `initial/` template and a `complete/` solution.

## Build Commands

Each assignment subdirectory (`ab2.1/initial/`, `ab2.1/complete/`, `ab2.2/complete/`) is an independent Gradle project:

```bash
cd ab2.1/complete   # or ab2.1/initial, ab2.2/complete
./gradlew build     # compile and package
./gradlew war       # build WAR file (flashcard-basic.war)
./gradlew test      # run JUnit 5 tests
./gradlew clean     # clean build artifacts
```

Run a single test class:
```bash
./gradlew test --tests "ch.fhnw.webfr.flashcard.test.QuestionnaireRepositoryTest"
```

## Tech Stack

- **Java 21**, **Jakarta EE 11** (Servlet Specification 6.0)
- **Gradle** (wrapper included per project)
- **Tomcat 11** as target server (servlet API: `org.apache.tomcat:tomcat-servlet-api:11.0.10`, `compileOnly`)
- **JUnit 5** (Jupiter) for tests
- No Spring Framework — raw Servlet API only

## Project Structure

```
ab2.1/          # Assignment 2.1: BasicFilter + BasicListener
  initial/      # Student starting template (Filter/Listener not yet implemented)
  complete/     # Reference solution
ab2.2/          # Assignment 2.2: I18NFilter + ResponseWrapper
  complete/     # Reference solution (no initial/ — builds on ab2.1/complete)
```

All projects share the same base package: `ch.fhnw.webfr.flashcard`

```
web/
  BasicServlet.java       # @WebServlet("/*") — routes /, /questionnaires, /questionnaires/{id}
  BasicFilter.java        # @WebFilter("/*") — logs request URIs
  BasicListener.java      # @WebListener — initializes QuestionnaireRepository on startup
  I18NFilter.java         # @WebFilter("/*") — wraps response, translates DE→EN (ab2.2)
  ResponseWrapper.java    # HttpServletResponseWrapper capturing output for modification (ab2.2)
persistence/
  QuestionnaireRepository.java   # In-memory ArrayList-based repository
domain/
  Questionnaire.java             # Entity: id, title, description
util/
  QuestionnaireInitializer.java  # Creates test data
```

## Key Architectural Patterns

**Request Flow:**
```
Browser → BasicFilter (logs URI) → [I18NFilter wraps response] → BasicServlet → response
                                        ↑ BasicListener pre-populated repository at startup
```

**Filter pattern** (all Filter implementations follow this):
```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    // pre-processing
    chain.doFilter(req, res);
    // post-processing (I18NFilter modifies response here)
}
```

**Response wrapping pattern** (ab2.2): `ResponseWrapper` extends `HttpServletResponseWrapper`, captures servlet output in a `CharArrayWriter`, allowing `I18NFilter` to replace German HTML strings with English equivalents from `src/main/resources/i18n.properties` before flushing to the client.

**Listener-based initialization**: `BasicListener` reads the `mode` context parameter from `web.xml` (`test` vs `productive`) via `ServletContextEvent.getServletContext().getInitParameter("mode")` and stores the initialized repository as a `ServletContext` attribute.

## Deployment

Build produces `build/libs/flashcard-basic.war` — deploy to a Tomcat 11 instance. The `web.xml` context parameter `mode` controls whether test data is loaded on startup.
