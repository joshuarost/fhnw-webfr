# Arbeitsblatt 1.1: Entwicklungsumgebung

## Ziel

Sie haben die notwendige Programmierinfrastruktur für das Modul auf Ihrem Rechner installiert.

## Ausgangslage

Sie haben auf Ihrem Rechner folgende Java Entwicklungsumgebung installiert:

* Mindestens Java JDK 21.x  
Testen Sie Ihre Installation aus der Command Shell mit dem Befehl `java -version`

* [IntelliJ IDEA](https://www.jetbrains.com/idea/) oder [Visual Studio Code](https://code.visualstudio.com/) (oder Eclipse/STS oder ...)  
Sind Sie vertraut im Umgang mit Ihrer IDE?

## Aufgabe 1: Entwicklungsumgebung aufsetzen

Installieren Sie auf Ihrem Rechner folgenden Webserver:

* [Tomcat Webserver](https://tomcat.apache.org/)  
Da Tomcat die Referenz-Implementation der [Jakarta Servlet Spec](https://jakarta.ee/specifications/servlet/6.1/) ist, werden wir diesen Server nutzen. Die aktuelle Servlet Spec ist in der Version 6.1 und wird von Tomcat 11.x.x umgesetzt. Installieren Sie deshalb die [Version 11.0.x](https://tomcat.apache.org/download-11.cgi#11.0.18) und testen Sie den Webserver. Nutzen Sie dabei folgende Informationen:

  * Hinweise zur Installation finden Sie im [Application Developer's Guide](https://tomcat.apache.org/tomcat-11.0-doc/index.html).
  * Testen der Installation  
    Um zu testen, ob Tomcat richtig installiert ist, führen Sie das Startskript (startup.bat oder startup.sh) aus. Falls auf der Konsole keine Fehler angezeigt werden, können Sie über den Browser die *Tomcat Landing Page* unter `http://localhost:8080` öffnen.

Wir werden Docker als bevorzugte Variante für das Aufsetzen einer Datenbank in einer späteren Lektion einsetzen. Für alle, die [Docker Desktop](https://www.docker.com/) bereits installiert haben und Docker bereits jetzt einsetzen wollen, hier das entsprechende `docker-compose.yaml` File:

```yaml
services:
  tomcat:
    image: tomcat:jre21
    container_name: tomcat
    restart: always
    ports:
      - 8080:8080
    volumes:
      - ./webapps:/usr/local/tomcat/webapps
```

* **Nicht vergessen:** Ordner `webapps` muss im gleichen Ordner wie `docker-compose.yaml` erstellt werden. Hierhin werden die `*.war` Files kopiert.

### Bemerkungen

* Sie werden nur in den ersten Wochen einen eigenen Webserver einsetzen müssen. Anschliessend werden wir den *Embedded Tomcat Webserver* mit Spring Boot verwenden.

* [Gradle](https://gradle.org/) als Build und Dependency Management Tool  
Wir werden für die Verwaltung der Java Bibliotheken Gradle verwenden. Gradle müssen Sie jedoch nicht speziell installieren. Ich werde alle Aufgaben als Gradle-Projekte abgeben und diese Projekte haben einen entsprechenden, projektspezifischen Gradle-Wrapper integriert. Alle wichtigen IDEs können mit diesem Gradle-Wrapper problemlos arbeiten. Normalerweise können Sie ein solches Projekt mit den Default-Einstellungen in Ihre IDE importieren. Sie müssen nur beachten, dass Sie beim Import allenfalls die Variante "Gradle" wählen müssen.
