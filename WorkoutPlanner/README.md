# WORKOUT PLANNER
Kursprojekt för kursen Objektorienterad Design. Projektet är skapat i IntelliJ Idea med JDK 25.
## Model
Model-lagret innehåller klasser för att representera data för ett träningspass. 

| Klass                  | Beskrivning                                                                                                                                      |
|:-----------------------|:-------------------------------------------------------------------------------------------------------------------------------------------------|
| Exercise               | Klass som representerar en övning i ett träningspass                                                                                             |
| ExerciseLibraryManager | Klass som hanterar läsandet från och skrivandet till JSON-filen som håller övningsbiblioteket                                                    |
| ExerciseSet            | Klass som representerar ett set för en övning                                                                                                    |
| ImageLoader            | Klass som läser in övningsbilder till en HashMap utgående från övningarnas variabel `imagePath` och parar ihop bilden med övningens namn         |
| Workout                | Klass som representerar ett träningspass                                                                                                         |
| WorkoutManager         | Klass som håller alla sparade träningspass och standardövningarna samt fungerar som ett gränssnitt mellan resten av modellagret och controllern. |
| WorkoutsReader         | Klass som läser in lista med träningspass från en JSON-fil                                                                                       |

## View
View innehåller klasserna som skapar det grafiska användargränssnittet. View-paketet är modulärt och det är bara klassen WorkoutPlannerView som har en instans av controllern. 

| Klass                | Beskrivning                                                                                                           |
|:---------------------|:----------------------------------------------------------------------------------------------------------------------|
| ActiveWorkoutTab     | Klass som representerar en flik för att visa ett aktivt träningspass.                                                 |
| ExerciseInputDialog  | Klass som representerar ett fönster för att fylla i information om en ny övning.                                      |
| ExerciseLibraryTab   | Klass som representerar en flik för att hantera övningsbiblioteket.                                                   |
| ExerciseListView     | Klass som visar övningarna i ett träningspass.                                                                        |
| ExercisePickerDialog | Klass som representerar ett fönster för att välja övningar från övningsbiblioteket att lägga till i ett träningspass. |
| ExerciseSetTableView | Klass som visar repetitioner och belastning för en övning.                                                            |
| WorkoutInputDialog   | Klass som representerar ett fönster för att fylla i information om ett nytt träningspass.                             |
| WorkoutLibraryTab    | Klass som representerar en flik för att hantera träningspassen i applikationen.                                       |
| WorkoutListView      | Klass som visar en lista med träningspass.                                                                            |
| WorkoutPlannerView   | Klass som representerar flikhanteraren och huvudfönstret i applikationen.                                             |

## Controller
Applikationens controller. En klass som fungerar som gränssnitt mellan model och view.

## PlannerApplication
Main-klassen i applikationen.

# Om ni använder IntelliJ IDEA
För att IntelliJ IDEA ska kopiera resurserna i katalogen `resources` till **build output** katalogen (out/) vid kompilering behöver ni göra följande före ni kör ert program första gången:
1. Högerklicka på katalogen `resources`
2. För muspekaren till `Mark Directory As` och välj `Resources Root` från undermenyn.

## JavaFX i IntelliJ IDEA

1. Ladda ner Javafx SDK från https://gluonhq.com/products/javafx/ .
2. Unzip filerna någonstans.
3. Öppna `Project Structure (Ctrl+Shift+Alt+S)`.
4. Öppna `Libraries` och klicka på plusset (`New Project Library`) och välj `Java`
5. Välj alla `.jar` filer i `<javafx-dir>/lib` och klicka ok tills det lagt till i **WorkoutPlanner**.
6. Uppe till höger, vänster om gröna _(run)_ plusset expandera menyn och välj `Edit Configurations`.
7. Lägg till en ny `Application` Configuration `(Alt+Insert)` och lägg till `PlannerApplication` som main class.
8. I `Build and Run` balken, välj `Modify options (Alt+M)` och välj `Add VM options (Alt+V)`.
9. Klistra detta i VM options:

   ```
   --module-path "C:\Users\samue\Downloads\openjfx-26_windows-x64_bin-sdk\javafx-sdk-26\lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
   ```

   Byt ut `path_to_javafx` mot den absoluta sökvägen till ert nedladdade JavaFX.

## Json-parser i Java med IntelliJ
1. Ladda ner *jackson-core*, *jackson-annotations* och *jackson-databind* jar-filerna från moodle. De finns också att ladda ner från [Central Maven Repository](https://mvnrepository.com/). Om du laddar ner dem från Maven Repositoryn så är det viktigt att versionsnumren överensstämmer för alla tre jar-filer (alla måste vara 3.x om man väljer version 3.x för en av dem).
2. Öppna `Project Structure (Ctrl+Shift+Alt+S)`.
3. Öppna `Libraries` och klicka på plusset (`New Project Library`) och välj `Java`
4. Välj alla `.jar` filer du laddade ner i `steg 1` och klicka ok så de läggs till i **WorkoutPlanner**.
5. Klart!

# Om ni använder Eclipse IDE

För att Eclipse IDE ska kopiera resurserna i katalogen `resources` till **build output** katalogen (bin/) vid kompilering behöver ni göra följande före ni kör ert program första gången:
1. Högerklicka på projektet i package explorer och välj `Build Path` -> `Configure Build Path`.
2. Välj fliken `Source` och klicka på `Add Folder...`
3. Markera `resources`-katalogen och klicka på `OK`.
4. Klicka på `Apply and Close` och det är klart!

## JavaFX i Eclipse IDE
1. Ladda ner JavaFX SDK från https://gluonhq.com/products/javafx/ .
2. Unzip filerna någonstans.
3. För att lägga till de nedladdade .jar-filerna till projektet högerklickar ni på projektet i Eclipse IDE:s package explorer.
4. Välj `Build Path` -> `Configure Build Path`.
5. I fönstret som öppnas väljer ni fliken `Libraries`.
6. Markera `Classpath` och tryck på knappen `Add External JARs...`
7. Lokalisera era nedladdade JavaFX-filer och lägg till alla .jar-filer som finns i **lib**-katalogen.
8. Kör programmet en gång. Eclipse kommer att skapa en run configuration, men inget kommer visas på skärmen. Ni får ett error-meddelande i konsolen.
9. För att konfigurera er run configuration, högerklicka på .java-filen i projektet som har main-funktionen (**PlannerApplication**).
10. Välj `Run As` -> `Run Configurations`.
11. I fönstret som öppnas välj fliken `(x) = Arguments`.
12. Klistra in följande i `VM-arguments`-fältet:
   ```
      --module-path "path_to_javafx/lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
   ```
   Byt ut `path_to_javafx` mot den absoluta sökvägen till ert nedladdade JavaFX.
13. Klicka på `Apply` och kör sedan programmet igen. Nu borde det fungera.

## Json-parser i Java med Eclipse IDE
1. Ladda ner *jackson-core*, *jackson-annotations* och *jackson-databind* jar-filerna från moodle. De finns också att ladda ner från [Central Maven Repository](https://mvnrepository.com/). Om du laddar ner dem från Maven Repositoryn så är det viktigt att versionsnumren överensstämmer för alla tre jar-filer (alla måste vara 3.x om man väljer version 3.x för en av dem).
2. För att lägga till de nedladdade .jar-filerna till projektet högerklickar ni på projektet i Eclipse IDE:s package explorer.
3. Välj `Build Path` -> `Configure Build Path`.
4. I fönstret som öppnas väljer ni fliken `Libraries`.
5. Markera `Classpath` och tryck på knappen `Add External JARs...`
6. Lokalisera de nedladdade .jar-filerna och lägg till dem till projektet.
7. Klart!

# Om ni använder VS Code
You are on your own...

