package view;

import controller.WorkoutPlannerController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Workout;

import java.util.HashMap;

// main view of the application, Holds references to the other view elements.
public class WorkoutPlannerView extends VBox {

    private WorkoutPlannerController controller;

    private TabPane tabPane;
    private TextArea messageArea;

    private WorkoutLibraryTab workoutLibraryTab;
    private ExerciseLibraryTab exerciseLibraryTab;

    private HashMap<Workout, ActiveWorkoutTab> activeWorkoutTabs;

    private final Insets insets = new Insets(10);

    private Stage stage;


    public WorkoutPlannerView(WorkoutPlannerController controller, Stage stage) {
        super();
        this.controller = controller;
        this.stage = stage;

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem saveItem = new MenuItem("Save");
        MenuItem loadItem = new MenuItem("Load");
        MenuItem exitItem = new MenuItem("Exit");

        fileMenu.getItems().addAll(saveItem, loadItem, exitItem);
        menuBar.getMenus().add(fileMenu);

        // Lägg menyn överst i VBox
        this.getChildren().add(menuBar);

        // Koppla till controller
        saveItem.setOnAction(e -> controller.handleSave());
        loadItem.setOnAction(e -> controller.handleLoad());
        exitItem.setOnAction(e -> stage.close());

        activeWorkoutTabs = new HashMap<>();

        messageArea = new TextArea();
        messageArea.setEditable(false);
        messageArea.setWrapText(true);
        messageArea.setMaxWidth(Double.MAX_VALUE);
        messageArea.setMaxHeight(100);

        workoutLibraryTab = new WorkoutLibraryTab(this, controller);
        workoutLibraryTab.setOnSelectionChanged(event -> {
            messageArea.clear();
        });

        exerciseLibraryTab = new ExerciseLibraryTab(this, controller);
        exerciseLibraryTab.setOnSelectionChanged(event -> {
            messageArea.clear();
        });

        tabPane = new TabPane();

        tabPane.getTabs().addAll(exerciseLibraryTab, workoutLibraryTab);
        workoutLibraryTab.setClosable(false);
        exerciseLibraryTab.setClosable(false);

        // Added vbox for messageArea to have the textArea padded
        VBox messageAreaVBox = new VBox();
        messageAreaVBox.setPadding(new Insets(10));
        messageAreaVBox.getChildren().add(messageArea);

        this.getChildren().addAll(tabPane, messageAreaVBox);

    }

    public Stage getStage() {
        return stage;
    }

    public Workout getSelectedWorkoutFromListView() {
        return workoutLibraryTab.getWorkoutListView().getSelectedWorkout();
    }

    public Insets returnInsets() {
        return insets;
    }

    // adding a new active workout tab to the tab pane.
    public void addWorkoutTab(Workout workout) {
        ActiveWorkoutTab newTab = new ActiveWorkoutTab(this, workout, controller);
        if (!activeWorkoutTabs.containsKey(workout)) {
            activeWorkoutTabs.put(workout, newTab);
            tabPane.getTabs().add(newTab);
        }
    }

    // removing a active workout tab from the tab pane.
    public void removeWorkoutTab(Workout workout) {
        if (activeWorkoutTabs.containsKey(workout)) {
            tabPane.getTabs().remove(activeWorkoutTabs.get(workout));
            activeWorkoutTabs.remove(workout);
        }
    }

    // getting the tab that is associated with the given workout
     
    public ActiveWorkoutTab getActiveWorkoutTab(Workout workout) {
        return activeWorkoutTabs.get(workout);
    }

    // checking if the given workout is already open in an existing active workout tab.
     
    public boolean hasActiveWorkoutTab(Workout workout) {
        return activeWorkoutTabs.containsKey(workout);
    }

    public void setMessage(String message) {
        messageArea.setText(message);
    }

    // opens a workout in a new ActiveWorkoutTab.
     
    public void openWorkoutTab(Workout workout) {

        // If already open, switch to it
        if (activeWorkoutTabs.containsKey(workout)) {
            tabPane.getSelectionModel().select(activeWorkoutTabs.get(workout));
            return;
        }

        // Otherwise create a new tab
        ActiveWorkoutTab tab = new ActiveWorkoutTab(this, workout, controller);
        activeWorkoutTabs.put(workout, tab);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
}
