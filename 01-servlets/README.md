# Lektion 1: Einführung in das Modul "Web Frameworks"

## Drehbuch

Im [Drehbuch](https://gitlab.fhnw.ch/sgi/moduluebersicht/-/blob/2026fs/webfr/drehbuch.adoc?ref_type=heads) finden Sie alle Angaben zum [Semesterplan](https://gitlab.fhnw.ch/sgi/moduluebersicht/-/blob/2026fs/webfr/drehbuch.adoc?ref_type=heads#user-content-semesterplan) - insbesondere zu den **Prüfungsterminen**.

## Einleitung

Die ersten beiden Wochen beinhalten eine Repetition der grundlegenden Konzepte, die in einer Java Webapplikation zum Einsatz kommen. Diese Konzepte wurden im Modul **Verteilte Systeme** behandelt. Auch das Modul **Kommunikation in verteilten Anwendungen** aus dem Studiengang iCompetence enthält Aspekte dieser Konzepte, jedoch werden diese nicht in der gleichen Ausführlichkeit behandelt.

Zu diesen Konzepten gehören:

* Servlet
* Filter
* Listener

## Arbeitsauftrag

Machen Sie sich mit dem Modul-Anlass vertraut. Die entsprechenden Informationen finden Sie im [Drehbuch](https://gitlab.fhnw.ch/sgi/moduluebersicht/-/blob/2026fs/webfr/drehbuch.adoc?ref_type=heads).

Zur Vorbereitung und Erarbeitung der Theorie lesen Sie sich in das aktuelle Thema ein, mit

* der [Einführung](#einführung) und
* den Folien ([pdf](./docs/Introduction.pdf))

Anschliessend vertiefen Sie die Theorie mit folgenden Arbeitsblättern:

* [Arbeitsblatt AB1.1](./ab1.1/README.md)  
In diesem Arbeitsblatt werden Sie die Entwicklungsumgebung auf Ihrem Computer aufsetzen, um Java Webapplikationen ohne Einsatz irgendwelcher Frameworks implementieren und ausführen zu können.

* [Arbeitsblatt AB1.2](./ab1.2/README.md)  
In diesem Arbeitsblatt wird die Komponente _Servlet_ repetiert. Dazu werden Sie die einfache Java Webapplikation _flashcard-basic_ installieren und in Ihrem Tomcat deployen. Sie sollten anschliessend verschiedene Fragen beantworten, die sich auf die Webapplikation beziehen.

* [Arbeitsblatt AB1.3](./ab1.3/README.md)  
In diesem Arbeitsblatt erweitern Sie die Komponente _BasicServlet_. Es ist das Ziel dieser Übung, dass Sie mindestens einmal ganz direkt ein Servlet programmieren. Wir werden im Verlauf dieses Moduls *Spring Boot* als Framework einsetzen. Dabei wird die Komponente *Servlet* immer mehr in der Hintergrund verschwinden, aber in jeder Spring Boot Applikation gleichwohl vorhanden sein. Das Servlet wird in jeder Java Webapplikation eine zentrale Rolle spielen - und deshalb ist es wichtig, die Komponente Servlet zu verstehen.

## Einführung

### Servlet API

Folgende Komponenten im Kontext des _Servlet API_ müssen Sie verstehen und erklären können. Jede dieser Komponenten übernimmt eine bestimmte Aufgabe während der Verarbeitung eines HTTP Requests. Die Zuordnung eines HTTP Requests in Form einer URL auf die entsprechenden Komponenten müssen Sie aufzeigen können.

#### _Servlet_

> A servlet is a Jakarta technology-based web component, managed by a container, that generates dynamic content. Like other Jakarta technology-based components, servlets are platform-independent Java classes that are compiled to platform-neutral byte code that can be loaded dynamically into and run by a Jakarta technology-enabled web server. Containers, sometimes called servlet engines, are web server extensions that provide servlet functionality. Servlets interact with web clients via a request/response paradigm implemented by the servlet container.

--- from [Jakarta Servlet 6.1 Specification](https://jakarta.ee/specifications/servlet/6.1/jakarta-servlet-spec-6.1.html#what-is-a-servlet)


#### _Servlet Container_ or _Servlet Engine_

> The servlet container is a part of a web server or application server that provides the network services over which requests and responses are sent, decodes MIME-based requests, and formats MIME-based responses. A servlet container also contains and manages servlets through their lifecycle.

--- from [Jakarta Servlet 6.1 Specification](https://jakarta.ee/specifications/servlet/6.1/jakarta-servlet-spec-6.1.html#what-is-a-servlet-container)


#### Tomcat als _Webserver_

> The Apache Tomcat® software is an open source implementation of the Jakarta Servlet, Jakarta Server Pages, Jakarta Expression Language, Jakarta WebSocket, Jakarta Annotations and Jakarta Authentication specifications. These specifications are part of the Jakarta EE platform.

--- from [Apache Tomcat](https://tomcat.apache.org/index.html)
