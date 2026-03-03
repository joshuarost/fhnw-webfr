# Arbeitsblatt 2.1: Servlet Komponenten

## Ziel

Sie kennen die verschiedenen Basis-Komponenten für Entwicklung von Java Webapplikation mit Java EE und können diese korrekt einsetzen.

## Vorbereitung

Laden Sie die Lektion 02 als zip-File auf Ihren Computer und entpacken Sie das zip-File. Verschieben/kopieren Sie den Ordner `02-*` an einen Ort Ihrer Wahl.

Importieren Sie das Gradle-Projekt `02-*/ab2.1/initial` in Ihre IDE. Das Projekt entspricht der Lösung aus AB1.3 bis auf folgende Anpassung:

- Das URL-Mapping auf das Servlet wird mit der Annotation `@WebServlet` konfiguriert - und nicht im File `web.xml`.

## Ausgangslage

Die Webapplikation aus `initial` können Sie fehlerfrei ausführen.

## Aufgabe 1: Filter _BasicFilter_ implementieren

Implementieren Sie den Filter `BasicFilter`, der bei jedem HTTP-Request die entsprechende URL in das Log bzw. auf die Console schreibt, zum Beispiel:

```text
Before request [uri=/flashcard-basic/]
Before request [uri=/flashcard-basic/questionnaires]
```

Hinweise:

- Nutzen Sie den Logger `java.util.logging.Logger` für die Ausgabe.
- Das Mapping für das `BasicServlet` ist neu auf `/*` gesetzt. Siehe entsprechende Annotation im Servlet!

## Aufgabe 2: Listener _BasicListener_ implementieren

Momentan gilt folgende Ausgangslage: Das `QuestionnaireRepository` wird in `BasicServlet.init()` initialisiert. Diese Initialisierung ist konzeptuell keine Aufgabe des Servlets. Sie gehört zum Aufbau der Infrastruktur der Webapplikation. Deshalb soll neu das Repository beim Start erzeugt werden, d.h. unabhängig vom Servlet erzeugt werden. Das Repository soll anschliessend über den Servlet-Context den Servlets zur Verfügung stehen.

Aktuell werden ein paar Demo-Fragebögen bei der Initialisierung des Repository angelegt. Diese Demo-Fragebögen sollen neu aber nur im Test-Modus generiert werden. Dieser Modus wird über den Context-Parameter `mode` gesteuert.

Führen Sie deshalb im File `web.xml` den Context-Parameter `mode` folgendermassen ein: 

```xml
<context-param>
    <param-name>mode</param-name>
    <param-value>test</param-value>
</context-param>
```

Überlegen Sie sich zuerst, wie `BasicListener` diesen Context-Parameter auslesen kann, so dass nur im Test-Modus das `QuestionnaireRepository` initialisiert wird.

Nun müssen Sie einen Weg finden, wie das Servlet auf die im Listener erzeugte Instanz des `QuestionnaireRepository` zugreifen kann. Im Servlet selbst soll das `QuestionnaireRepository` folgendermassen genutzt werden können:

```java
Questionnaire questionnaire = questionnaireRepository.findById(id);
```

Implementieren Sie `BasicListener` als `ServletContextListener` und passen Sie das Servlet `BasicServlet` entsprechend an. Nutzen Sie dabei den `ServletContext` programmatisch, um entsprechende Attribute im Listener zu setzen und im Servlet auszulesen. Das Servlet wird per Default erst beim ersten Request geladen, so dass die Informationen im Listener vorhanden sind, falls die Listener-Instanz auf Context-Events hört.