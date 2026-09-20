# SEW 5 · E03 · Übungsblatt Maven

Name: Suisa Shehi  Gruppe: 5bw  Datum: 20.09.2026

Ihr arbeitet im Projekt `sew5`, das ihr aus dem Ordner `Angabe` kopiert habt.
Die Übungen 1 bis 3 sind **Pflicht**, 4 und 5 sind Erweiterung, 6 ist für die,
die früher fertig sind. Die Hausübung macht jeder.

Nach jeder Übung: `mvn package` muss ohne roten Text durchlaufen.
Am Ende der Stunde: committen und pushen.

---

## Übung 1 · Das Projekt zum Laufen bringen — Stufe 1

In der `pom.xml` fehlt der `properties`-Block.

1. Ergänzt ihn so, dass mit **Java 21** übersetzt wird und die Quelldateien als
   **UTF-8** gelesen werden.
2. Ruft `mvn package` auf.
3. Sieht euch den Ordner `target/` an. Schreibt auf, welche Dinge darin
   entstanden sind:

   `classes/` (übersetzte .class-Dateien + Ressourcen) · `sew5-0.1.0.jar`
   (das fertige Archiv) · `maven-status/` und `maven-archiver/`
   (Buchführung von Maven, z. B. welche Dateien übersetzt wurden)

4. Legt `target/` in die `.gitignore` — falls es noch nicht darinsteht und
   beschreibe in einem Satz, warum:

   Weil in `target/` ausschließlich generierte Dateien liegen, die `mvn`
   jederzeit aus dem Quellcode neu erzeugen kann — sie ins Repository zu
   legen bläht es nur auf und erzeugt bei jedem Build Konflikte.

---

## Übung 2 · Die erste eigene Klasse — Stufe 1

Die Klasse `at.htl.shkodra.Schueler` ist nur ein Gerüst.

1. Gebt ihr die Felder **Name**, **Jahrgang** und **Gruppe**.
2. Die `main`-Methode legt drei Schülerobjekte an und gibt sie aus.
3. Baut das Projekt und startet das entstandene JAR von der Kommandozeile:

   ```bash
   mvn package
   java -cp target/sew5-0.1.0.jar at.htl.shkodra.Schueler
   ```

4. Ändert die `version` in der `pom.xml` auf `0.2.0` und baut erneut. Wie heißt
   das JAR jetzt, und warum ist der Befehl aus Punkt 3 damit kaputt?

   Das JAR heißt jetzt `sew5-0.2.0.jar`, denn Maven setzt den Dateinamen aus
   `artifactId` + `version` zusammen. Der Befehl aus Punkt 3 zeigt per `-cp`
   noch auf `sew5-0.1.0.jar`; nach einem `mvn clean` existiert diese Datei
   nicht mehr, der Classpath ist leer und Java meldet:
   `Error: Could not find or load main class at.htl.shkodra.Schueler`.
   (Ohne `clean` liegt das alte JAR noch da und der Befehl läuft scheinbar
   weiter — man testet dann aber den Stand von gestern.)

---

## Übung 3 · Eine Datei, die mit ins JAR wandert

Im Projekt liegt `src/main/resources/schueler.csv`.

1. Baut das Projekt und sucht die Datei in `target/`. Wo ist sie gelandet?

   In `target/classes/schueler.csv` — also direkt neben den `.class`-Dateien.
   Maven kopiert alles aus `src/main/resources` in die Wurzel von `classes/`.

2. Öffnet das JAR mit einem Zip-Programm (ein JAR *ist* ein Zip). Ist die CSV
   drin?  ☒ ja  ☐ nein

   (`unzip -l target/sew5-0.2.0.jar` zeigt `schueler.csv` im Wurzelverzeichnis
   des Archivs, weil alles aus `target/classes` ins JAR gepackt wird.)

3. Warum ist das der Grund, aus dem man eine solche Datei **nicht** über einen
   Pfad wie `C:\Users\...\schueler.csv` öffnet?

   Weil die Datei im Auslieferungszustand gar keine eigene Datei auf der
   Platte mehr ist, sondern ein Eintrag *innerhalb* des JARs — ein absoluter
   Pfad existiert also nur auf dem Rechner des Entwicklers und ist auf jedem
   anderen Rechner (und unter jedem anderen Betriebssystem) falsch. Man liest
   sie stattdessen über den Classpath, z. B. mit
   `Schueler.class.getResourceAsStream("/schueler.csv")`.

---

## Übung 4 · Eine fremde Bibliothek einbinden

1. Sucht auf `search.maven.org` die Bibliothek **Gson** von
   `com.google.code.gson` und tragt sie als Abhängigkeit in die `pom.xml` ein.
2. Gebt eure drei Schülerobjekte als JSON aus. Zwei Zeilen genügen:

   ```java
   Gson gson = new GsonBuilder().setPrettyPrinting().create();
   System.out.println(gson.toJson(liste));
   ```

3. **Nehmt die Abhängigkeit wieder weg** und baut erneut. Notiert die
   Fehlermeldung wörtlich:

   ```
   [ERROR] .../Schueler.java:[3,23] package com.google.gson does not exist
   [ERROR] .../Schueler.java:[4,23] package com.google.gson does not exist
   [ERROR] .../Schueler.java:[28,9] cannot find symbol
   [ERROR]   symbol:   class Gson
   [ERROR]   location: class at.htl.shkodra.Schueler
   [ERROR]   symbol:   class GsonBuilder
   [ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:
           3.15.0:compile (default-compile) on project sew5: Compilation failure
   ```

   Ohne den `<dependency>`-Block legt Maven das Gson-JAR nicht auf den
   Compile-Classpath, also kennt der Compiler das Paket nicht.

4. Tragt sie wieder ein. Wo auf eurem Rechner liegt die heruntergeladene
   Gson-Datei?

   Im lokalen Repository unter
   `~/.m2/repository/com/google/code/gson/gson/2.11.0/gson-2.11.0.jar`
   (unter Windows `C:\Users\<name>\.m2\repository\...`). Der Pfad ist genau
   `groupId` (Punkte → Ordner) / `artifactId` / `version`. Heruntergeladen
   wird nur einmal, danach bedient sich jedes Projekt aus diesem Cache.

---

## Übung 5 · Fehlersuche — Stufe 2

Im Ordner `Beispiele` liegt `pom_kaputt.xml`. Darin stecken **vier** Fehler.

Findet sie die vier Fehler und schreibt sie auf.
Danach prüft ihr eure Liste, indem ihr die Datei als `pom.xml` in ein leeres
Projekt legt und `mvn package` aufruft.

| # | Zeile | Was ist falsch |
|---|---|---|
| 1 | 14 | `<version>0.1.0` wird nie geschlossen — das `</version>` fehlt. Damit ist die Datei kein gültiges XML mehr. Maven: *„Non-parseable POM: TEXT must be immediately followed by END_TAG"* |
| 2 | 10 | `<modelVersion>4.0.0</modelVersion>` fehlt komplett. Maven meldet *„'modelVersion' is missing"* — die Angabe ist in jeder POM Pflicht. |
| 3 | 22 / 26 | Tippfehler `<dependancy>` statt `<dependency>`. Maven: *„Malformed POM: Unrecognised tag: 'dependancy'"* |
| 4 | 24 | `<artifactId>Gson</artifactId>` — der Artefaktname ist kleingeschrieben `gson`. Maven: *„Could not find artifact com.google.code.gson:Gson:jar:2.11.0 in central"* |

**Achtung, Stolperfalle bei Fehler 4:** Auf macOS und Windows ist das
Dateisystem nicht case-sensitiv. Liegt Gson schon im lokalen `~/.m2`-Cache,
findet Maven den Ordner `Gson` trotzdem und der Build läuft *scheinbar* durch.
Erst mit leerem Repository (`mvn -Dmaven.repo.local=... -U package`) muss
Maven bei Maven Central nachfragen — und dort ist der Pfad case-sensitiv,
also schlägt es fehl. Genau so wurde der Fehler hier nachgewiesen.

Nach dem Beheben aller vier Fehler: `BUILD SUCCESS`.


*(Die reparierte Fassung liegt als `Beispiele/pom_repariert.xml` bei.)*

---

## Übung 6 · Für die Schnellen — Stufe 3

> **Nicht bearbeitet.**

Zuerst die Änderungen von Übung 4 auskommentieren. 
Dann sorgt dafür, dass `java -jar target/sew5-0.1.0.jar` ohne weitere Angaben
funktioniert. Hinweis: die Hauptklasse muss im Manifest des JAR stehen, und
dafür gibt es in der `pom.xml` einen `build`-Block.

Zweite Frage: Startet euer JAR danach auch dann, wenn es Gson (Übung 4 ) verwendet?
Probiert es aus und erklärt das Ergebnis.

_________________________________________________________________________

---

## Hausübung

Sucht auf `search.maven.org` die Bibliothek `org.apache.commons:commons-lang3`,
tragt sie in die `pom.xml` ein und ruft eine beliebige Methode daraus auf —
zum Beispiel `StringUtils.reverse("Shkodra")`.

Committen und pushen. Fällig zur nächsten Einheit.

**Erledigt.** In der `pom.xml` steht jetzt zusätzlich:

```xml
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-lang3</artifactId>
  <version>3.20.0</version>
</dependency>
```

3.20.0 war zum Abgabezeitpunkt die neueste Version auf Maven Central.
In `Schueler.main` kam dazu:

```java
System.out.println(StringUtils.reverse("Shkodra"));
```

Ausgabe: `ardokhS` — `mvn package` läuft grün durch (`BUILD SUCCESS`).
