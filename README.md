# Adrenaline: an Online Multiplayer Implementation
Online multiplayer implementation of the [Adrenaline table game](https://czechgames.com/en/adrenaline/) for the Software Engineering Capstone Project (A.Y 19-20)

# Dependencies
JavaFX is mandatory to run the JARs.
If you are running Arch Linux (similar commands for other distributions):
```
sudo pacman -S java-openjfx
```

Then you need to set the path of JavaFX in your system (any distribution):
```
java --module-path <path to javafx, e.g. /usr/share/openjfx/lib/> --add-modules javafx.controls -jar sample.jar
```
# Running
The pre-packaged jars use as config files:
- `files/server.properties`
- `files/client.properties`

Change the above files to set server and client network addresses.
Running the server:
```
java -jar adrenalina-server.jar
```

Running the client:
```
java -jar adrenalina-client.jar
```
