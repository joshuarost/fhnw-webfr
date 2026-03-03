# Zusammenfassung Servlet-Komponenten

Hier finden Sie eine Zusammenfassung der wichtigsten Aspekte zu:

- [Servlet-Container](#servlet-container)
- [Servlet](#servlet)
- [Filter](#filter)
- [Listener](#listener)

SpringMVC baut auf diesen Komponenten auf. Wir werden ab nächste Lektion SpringMVC sowohl für die Webapplikation wie auch für die Webservices einsetzen. Deshalb ist es wichtig, dass man als Entwickler und Entwicklerinnen diese Basis-Komponenten kennt, um schlussendlich die Funktionsweise von SpringMVC zu verstehen und bei einem Fehler an der richtigen Stelle zu suchen.

## Servlet-Container

Servlets sind zunächst einmal nichts anderes als ganz normale Java-Klassen, die ein bestimmtes Interface `jakarta.servlet.Servlet` implementieren. Damit diese Klassen auf HTTP-Anfragen eines Browsers reagieren und die gewünschte Antwort liefern können, müssen die Servlets in einer bestimmten Umgebung laufen. Diese Umgebung wird vom sogenannten Servlet-Container bereitgestellt. 

Der Container sorgt zunächst einmal für den korrekten Lebenszyklus der [Servlets](#servlet).
Zumeist arbeitet der Container im Zusammenspiel mit einem Webserver. Der Webserver bedient z.B. die Anfragen nach statischem HTML-Content, nach Bildern, multimedialen Inhalten oder Download-Angeboten. Kommt hingegen eine Anfrage nach einem Servlet herein, so leitet der Webserver diese an den Servlet-Container weiter. Dieser ermittelt das zugehörige Servlet und ruft dieses mit den Umgebungsinformationen auf. Ist das Servlet mit seiner Abarbeitung des Requests fertig, wird das Ergebnis zurück an den Webserver geliefert, der dieses dem Browser wie gewöhnlich zurückgibt. 

## Servlet

`jakarta.servlet.Servlet` ist das Interface, das letztendlich entscheidet, ob eine Java-Klasse überhaupt ein Servlet ist. Jedes Servlet muss dieses Interface direkt oder indirekt implementieren. De facto wird man aber wohl in den meisten Fällen nicht das Interface selbst implementieren, sondern auf `jakarta.servlet.GenericServlet` (falls man kein Servlet für HTTP-Anfragen schreibt), bzw. `jakarta.servlet.http.HttpServlet` für HTTP-Requests zurückgreifen. Das `HttpServlet` implementiert alle wesentlichen Methoden bis auf die HTTP-doXXX-Methoden wie `doGet(...)` oder `doPost(...)`. Als Entwickler muss man schlussendlich nur die Methoden `doGet`, `doPost` oder `doXYZ` überschreiben, je nachdem, welche HTTP-Methoden man unterstützen will.

### ServletRequest und ServletResponse

In den Verarbeitungsmethoden von Servlets (`service(), doGet(), doPost()...`) hat man stets Zugriff auf ein Objekt vom Typ `ServletRequest`. Arbeitet man mit `HttpServlet` handelt es sich um das Interface `jakarta.servlet.http.HttpServletRequest`, ansonsten um das Interface `jakarta.servlet.ServletRequest`. Diese Interfaces stellen wesentliche Informationen über den Client-Request zur Verfügung.

Ein JavaEE-Webserver, wie Tomcat, muss konkrete Implementierungen dieses Interfaces den Entwickler zur Verfügung stellen. Grundlage des eigenen Codes sollten aber immer die Interfaces selbst sein, niemals die konkrete Implementierung eines Container-Herstellers, da sonst die Portabilität verloren geht.

Letztlich geht es bei Servlets immer darum, auf einen Client-Request mit einer Antwort zu reagieren. Um diese Antwort generieren zu können, benötigt man ein Objekt des Typs `ServletResponse`, bzw. `jakarta.servlet.http.HttpServletResponse` in HTTP. Es dient bspw. im Falle eines Fehlers dazu, einen anderen HTTP-Statuscode als "200" zu setzen. Auch kann man HTTP-Header in der Response setzen. Hier ein paar Methoden dieser Klasse:

- `setContentType(String)`, um Mime-Type zu setzen, so dass der Browser korrekt reagieren kann.  
  __Wichtig__: Die Methode muss vor `getWriter()` aufgerufen werden.
- `setCharacterEncoding(String)`, um korrekten Zeichensatz zu setzen (bspw. UTF-8 oder ISO-8859-1), so dass auch Umlaute im Browser dargestellt werden.

Schlussendlich muss das Servlet seine Ausgabe auch irgendwohin schreiben. Dazu holt man sich aus dem `Response`-Objekt mit der Methode `getOutputStream()` ein Objekt vom Typ `ServletOutputStream` (für binäre Inhalte wie bspw. generierte PDF-Dateien oder Bilder) oder mit der Methode `getWriter()` ein Objekt vom Typ `PrintWriter`, wenn man als Antwort textuelle Daten generiert (bspw. HTML-Code oder XML-Daten).

### ServletContext

Jedes Servlet wird im Kontext der Webapplikation ausgeführt. Dieser Kontext gilt für alle Servlets der entsprechenden Webapplikation. Deshalb werden Informationen, die im Scope der Webapplikation gelten, hier abgelegt, so dass jedes Servlet auf diese zugreifen kann. 

Kontext-Parameter werden im Deployment-Deskriptor konfiguriert. Zum Beispiel:

```xml
<web-app ...>
    <context-param>
        <param-name>DBUSER</param-name>
        <param-value>webfr</param-value>
    </context-param>
    ...
</web-app>
```

In einem Servlet kann man über das `ServletRequest`-Objekt auf Werte zugreifen.

```java
protected void doGet(HttpServletRequest request, HttpServletResponse response)... {
    String dbuser = (String) request.getServletContext().getInitParameter("DBUSER");
    ...
```

Ebenfalls kann man zur Laufzeit den `ServletContext` mit eigenen Attributen füllen und diese wieder auslesen. Hier ein einfaches Beispiel:

```java
servletContext.setAttribute("qRepo", QuestionnaireRepository.getInstance());
...
QuestionnaireRepository qr = (QuestionnaireRepository) servletContext.getAttribute("qRepo");
```

### Lebenszyklus eines Servlet

Ein Servlet wird von einem Servlet-Container verwaltet. Ein Servlet wird über den sogenannten Deployment-Deskriptor (DD) dem Container bekannt gemacht. Der Deployment-Deskriptor ist eine XML-Datei, die stets unter dem in der Servlet-Spezifikation festgelegten Namen `"web.xml` im ebenfalls spezifizierten Verzeichnis `WEB-INF` abgelegt werden muss. Nehmen wir an, wir hätten in einer Webapplikation `basic` ein Servlet `BasicServlet` im Package `ch.fhnw.webfr.flashcard.web`. Dieses Servlet wollen wir unter der URL `/` aufrufen. Dann würden wir dazu folgenden Deployment-Deskriptor schreiben: 

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/javaee" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="3.0"
    xsi:schemaLocation="http://java.sun.com/xml/ns/javaee 
        http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"> 
    <servlet>
        <servlet-name>BasicServlet</servlet-name>
        <servlet-class>ch.fhnw.webfr.flashcard.web.BasicServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>BasicServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>

</web-app>
```

Bemerkungen:

- Ein Servlet __muss__ immer mit einem Namen und einer Klasse konfiguriert werden, siehe `<servlet>`.
- Es __muss__ ein URL-Mapping auf ein Servlet existieren, siehe `<servlet-mapping>`. Erst mit diesem Mapping kann das Servlet vom Container angesprochen werden.

Seit der __Servlet Spec 3.0__ kann man für die Konfiguration aller Basis-Komponenten (Servlet, Filter, Listener) auch Annotationen einsetzen. Damit kann man die Grösse des Deployment Descriptor `web.xml` stark verringern. Der obige Eintrag wird dann in der Klasse `BasicServlet` selber zu:

```java
@WebServlet(urlPatterns={"/*"})
public class BasicServlet extends HttpServlet {
...
```

Bemerkung:

- Diese Annotation entspricht dem XML-Element `<servlet>` und `<servlet-mapping>` im File `web.xml`.
- Falls der Name des Servlets (Attribute `name`) nicht explizit gesetzt ist, wird als Name der fully-qualified-classname verwendet 

Der Container leitet nicht nur Aufrufe an den Endpoint, d.h. an das Servlet weiter, er instanziiert zudem das Servlet, ruft die Lebenszyklus-Methoden auf und sorgt für die korrekte Umgebung. Jedes Servlet durchläuft dabei die in der Spezifikation definierten Phasen: 

__Laden__ Zunächst muss der Classloader des Containers die Servlet-Klasse laden. Wann der Container dies macht, bleibt ihm überlassen, es sei denn, man definiert den entsprechenden Servlet-Eintrag im Deployment-Deskriptor. Mit dem optionalen Eintrag "load-on-startup" wird definiert, dass der Servlet-Container die Klasse bereits beim Start des Containers lädt. Die Zahlen geben dabei die Reihenfolge vor (Servlets mit niedrigeren "load-on-startup"-Werten, werden früher geladen).

__Instanziieren__ Unmittelbar nach dem Laden wird das Servlet instanziiert, d.h. der leere Konstruktor wird aufgerufen.

__Initialisieren__ Bevor der erste Request an das Servlet weitergereicht werden kann, wird dieses initialisiert. Dazu wird die Methode `init(ServletConfig)` der Servlet-Instanz aufgerufen. Diese Methode wird genau einmal im Lebenszyklus eines Servlets (und nicht etwa vor jedem Request) aufgerufen und dient dazu, grundlegende Konfigurationen vorzunehmen. Der Servlet-Container wird dem Servlet dabei ein Objekt vom Typ `jakarta.servlet.ServletConfig` mitgegeben. In dieser `ServletConfig`-Instanz sind die Init-Parameter abgelegt. Diese Parameter können im File `web.xml` oder über Annotationen gesetzt werden. Beispiel mit Init-Parameter `email` im `web.xml`: 

````xml
<servlet>
    <servlet-name>ch.fhnw.webfr.flashcard.web.BasicServlet</servlet-name>
    <servlet-class>BasicServlet</servlet-class>
    <init-param>
        <param-name>email</param-name>
        <param-value>hugo.tester@fhnw.ch</param-value>
    </init-param>
</servlet>
````

Nach Abarbeiten der `init()`-Methode ist das Servlet bereit, Anfragen zu verarbeiten.

__Verarbeiten__ Bei jedem  Request an ein Servlet wird die Methode `service(ServletRequest, ServletResponse)` aufgerufen. Die Servlet-Container organisieren ihre Servlet Instanzen normalerweise in Thread-Pools. Jeder Request wird in einem eigenen Thread aufgerufen, aber es ist nicht festgelegt, welches Servlet den Request verarbeitet. Die daraus entstehenden Threading-Issues müssen unbedingt beachtet werden. Deshalb sollte ein Servlet __stateless__ sein.

Alle wesentlichen Informationen, welche den Client-Request betreffen, befinden sich im `ServletRequest`-Objekt. Zur Erzeugung der Antwort nutzt man Methoden des `ServletResponse`-Objekts. In den Servlets für HTTP-Anfragen (und das wird der Regelfall sein) sollte man nicht die `service()`-Methode selber überschreiben. Vielmehr bietet es sich an, die eigene Implementation von `HttpServlet` abzuleiten und die Methoden `doGet()`, `doPost()` etc. zu überschreiben.

__Löschen__ Der Container kann eine Servlet-Instanz als überfällig ansehen und diese aus dem Pool entfernen. Dazu ruft er die `destroy()`-Methode der entsprechenden Servlet-Instanz auf. Das Servlet hat nun die Möglichkeit Ressourcen freizugeben, wie z.B. Datenbank-Verbindungen. Ist einmal die Methode `destroy()` aufgerufen, steht diese Servlet-Instanz nicht mehr für weitere Anfragen zur Verfügung.

## Filter

Servlet-Filter bieten eine Möglichkeit auf Request und Response zwischen Client und Servlet zuzugreifen. Dabei können mehrere Filter eine Filterkette bilden. Dabei wird mittels Mapping-Regeln bestimmt, welche Filter für welche Requests wann zuständig sind. 

Es gibt zahlreiche Möglichkeiten, bei denen der Einsatz eines Filters sinnvoll sein kann. Der einfachste Anwendungsfall ist das Tracing, um zu sehen welche Ressource angesprochen wurde und wie lange die Bereitstellung der Ressource gedauert hat. Weitere typische Anwendungsfälle umfassen die Security bspw. eine Entschlüsselung des Requests und die Verschlüsselung der Response.

Filter sind im File `web.xml` deklariert (oder über die Annotation `@WebFilter`). Sie werden vom Container verwaltet und müssen das Interface `jakarta.servlet.Filter` implementieren. Es gibt drei Methoden, die den Lifecycle bestimmen:

- `init(FilterConfig)` - wird gerufen, nachdem der Container die Instanz der Filterklasse erzeugt hat
- `doFilter(ServletRequest, ServletResponse, FilterChain)` - wird bei der Abarbeitung der FilterChain aufgerufen
- `destroy()` - wird aufgerufen, falls die Filterinstanz gelöscht werden soll.

Ein oder mehrere deklarierte Filter bilden eine Filterkette. Die typische Implementierung von `doFilter()` macht dieses Konzept deutlich:

```java
doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
  // Code der etwas vor Aufruf der Kette macht
  chain.doFilter(request, response); // weitere Abarbeitung im Filterstack
  // Code der etwas nach Aufruf der Kette macht
}
```

Damit der Servlet-Container eine Filterinstanz erzeugen kann, muss jeder Filter einen parameterlosen Konstruktor besitzen.

In `web.xml` werden Filter deklariert und mittels Mapping-Regeln per URL-Pattern oder Servlet-Name bei der Abarbeitung eines Requests berücksichtigt:

```xml
// Filter deklarieren
<filter>
   <filter-name>My cool filter</filter-name>
   <filter-class>foo.bar.MyFilter</filter-class>
   <init-param> // kann im FilterConfig abgegriffen werden
      <param-name>loglevel</param-name>
      <param-value>10</param-value>
   </init-param>
</filter>

// Filter auf eine URL mappen
<filter-mapping>
   <filter-name>My cool filter</filter-name>
   <url-pattern>*.do</url-pattern>
</filter-mapping>
````

oder mit einem entsprechenden Mapping über `<servlet-name>`.

```xml
// Filter auf ein Servlet mappen
<filter-mapping>
   <filter-name>My cool filter</filter-name>
   <servlet-name>SomeServlet</servlet-name>
</filter-mapping>
````

Die Reihenfolge des Aufrufs der Filter in einer Filterkette bestimmt der Container nach den folgenden Regeln:

1. Zunächst werden alle passenden url-pattern gesucht und in der Reihenfolge ihres Erscheinens in `web.xml` auf den Filterstack gelegt.
2. Anschliessend wird der gleiche Vorgang mit allen passenden `servlet-name` Filtern durchgeführt.

## Listener

Im Gegensatz zum Filter, der sich in den Request/Response-Pfad zwischen Client und Servlet einbindet, reagiert ein Listener auf verschiedene Ereignisse des Servlet-Containers selbst. 

Listener sind Klassen, die das Servlet Listener Interface implementieren und die in `web.xml` (oder über die Annotation `@WebListener`) dem Container bekannt gemacht werden. In einer Webapplikation können problemlos mehrere Listener vorhanden sein. Sie werden z.B. bei Lifecycle-Ereignissen der Webapplikation vom Container aufgerufen. Listener werden pro Anwendung einmal in der Reihenfolge ihres Erscheinens im `web.xml`instanziiert. Eine Listener-Klasse muss den Standard-Konstruktor besitzen, damit der Container eine Instanz erzeugen kann.

```xml
<listener>
  <listener-class>a.b.MyRequestListener</listener-class>
</listener>
````

Da Listener-Instanzen nur einmal existieren, können Zustände der entsprechenden Scopes (application, session, request) nicht in den Listener-Klassen gehalten werden dürfen. Die Listener Implementierungen müssen selbst dafür sorgen und es gilt ähnlich wie für Filter: nichttriviale Listener werden synchronisierten Code enthalten und die Synchronisation erfolgt am entsprechenden Scope. Ein typisches Codefragment für das Setzen eines Zustands im Scope der Session eines Nutzers sieht dann so aus:

```java
..
HttpSession session = event.getSession();
synchronized (session) {
  session.setAttribute("SOME_STATE", new Integer(1));
}
..
```

Für alle möglichen Ereignisse, welche in einer Webapplikation passieren können, gibt es insgesamt acht Listener-Interfaces. Sie bieten die Möglichkeit auf entsprechende Ereignisse zu reagieren. Folgende Listener Interfaces sind spezifiziert:

`ServletContextListener`: Wird eine Webapplikation deployed, undeployed oder neu gestartet, wird deren `ServletContextListener` benachrichtigt. Typischerweise gibt es in einer Webapplikation eine Klasse, die diesen Listener implementiert. Zum Beispiel hat die Klasse die Aufgabe die Webapplikation zu initialisieren, wie Datenbank-Connections herzustellen, Logger zu konfigurieren etc. Geht dabei irgendwas schief und die Webapplikation ist nicht lebensfähig, kann man eine RuntimeException werfen. Tomcat beendet die Webapplikation und ruft `contextDestroyed()` des Listeners auf.

`ServletContextAttributeListener`: Diese Listener werden benachrichtigt, wenn ein Attribut im `ServletContext` gesetzt, ersetzt oder entfernt wird.

`HttpSessionListener`: Diese Listener werden benachrichtigt, wenn eine `HttpSession` erzeugt oder zerstört wurde. Damit kann man z.B. die Anzahl der aktiven Sessions (also User der Webapplikation) zählen, um z.B. mehr Ressourcen bereitstellen.

`HttpSessionAttributeListener`: Diese Listener werden benachrichtigt, wenn irgendein Attribut einer Session gesetzt, ersetzt oder entfernt wird.

`HttpSessionBindingListener`: Objekte einer Klasse, welche diesen Listener implementiert, werden vom Container benachrichtigt, wenn sie als Attribut in einer HttpSession gespeichert werden bzw. wenn sie aus der Session entfernt werden.

`HttpSessionActivationListener`: In verteilten WebApps darf es nur genau ein Session-Objekt pro Session-ID geben, egal über wieviel JVMs die WebApp verteilt ist. Durch Load-Balancing des Containers kann es passieren, dass jeder Request bei einer anderen Instanz des gleichen Servlets ankommt. Also muss die Session für diesen Request von einer JVM zu der anderen umziehen.
Der `HttpSessionActivationListener` wird wiederum von den Attributen der Session implementiert, sodass sie benachrichtigt werden bevor und nachdem eine Session-Migration stattfindet. Die Attribute können dann dafür sorgen, dass sie den Trip überleben.

`ServletRequestListener`: Dieser Listener wird benachrichtigt, wenn ein Request die Webapplikation erreicht. Damit kann man z.B. Requests loggen.

`ServletRequestAttributeListener`: Dieser Listener wird benachrichtigt, wenn ein Attribut eines Requests gesetzt, ersetzt oder entfernt wird.
