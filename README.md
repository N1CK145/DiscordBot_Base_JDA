# DiscordBot_Base_JDA
Basis für einen DiscordBot in Java

## MySQL
- Eine Datenbank ist Pflicht. Ohne Datenbank wird der Bot nicht starten!

## Bot Start
Um den Bot ohne zu starten muss man Folgendes Argument angeben:
- -token [TOKEN]

Diese Methode wurde gewählt, weil dies die sicherste ist. Zu empfehlen bei einem Start Skript ist,
den Titel zu ändern, da sonst der Token sichtbar ist!

## Server
In der Server-Klasse werden Die ServerID, der ServerName und eine Instanz der ServerSettings Klasse gespeichert.

## ServerSettings
In der ServerSettings klasse werden alle einstellungen aus der Datenbank gespeichert. Aktuell gibt es
- Prefix
als Einstellung.

## settings.cfg
```cfg
# Hier wird der MySQL Port angegeben. Standart ist 3306
mysql.port=3306 

# Hier wird das Password angegeben um sich anzumelden
mysql.password=PASSWORD 

# Hier wird die Datenbank angegeben, wo die Tabellen erstellt werden sollen
# Die Datenbank muss eigenhändich erstellt werden (CREATE DATABASE [DB_NAME];)
mysql.database=DiscordBot

# Der Username der Datenbank wird hier festgelegt. Empfohlen wird, dass für den Bot ein eigener User
# erstellt wird, da es sonst zu sicherheitslücken führen kann!
mysql.username=root

# Der Host der Datenbank wird hier festgelegt.
mysql.host=localhost
```

## Build
Die .jar wird über gradle erstellt. Um dies zu errreichen gehe in das Verzeichniss mit der `gradlew` Datei.
Dort wird dann der Befehl `gradlew build` ausgeführt
