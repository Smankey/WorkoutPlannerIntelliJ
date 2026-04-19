package view;

import controller.WorkoutPlannerController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.Exercise;
import model.ExerciseSet;
import model.Workout;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A class the represents the tab that displays the workouts and their exercises and sets.
 */
public class WorkoutLibraryTab extends Tab {

    private Consumer<Workout> onRemoveWorkoutButtonClicked;
    private Runnable onAddWorkoutButtonClicked;
    private Workout previouslySelectedWorkout;      // not ideal solution...
    private WorkoutListView workoutListView;

    private Label workoutName;
    private TextArea workoutDescription;
    private Runnable onAddExerciseButtonClicked;
    private Consumer<Exercise> onRemoveExerciseButtonClicked;
    private ExerciseListView exerciseListView;

    private Label exerciseTitleLabel;
    private ExerciseSetTableView setTableView;

    private ImageView imageView;
    private final Image placeholderImg = new Image("/workout_planner_logo.png");

    private WorkoutPlannerView view;
    private WorkoutPlannerController controller;

    private final double vboxHeight = 500;
    private final double vboxWidth = 300;

    public WorkoutLibraryTab(WorkoutPlannerView view, WorkoutPlannerController controller) {
        super("Workout Library");
        this.view = view;
        this.controller = controller;
        this.controller.registerLibraryTab(this);
        setClosable(false);
        setupTabContent();
    }

    /**
     * Helper-method for setting up the content of the tab.
     */
    private void setupTabContent() {
        VBox workoutListVBox = createWorkoutListVBox();
        workoutListVBox.getStyleClass().add("card");

        VBox exerciseListVBox = createExerciseListVBox();
        exerciseListVBox.getStyleClass().add("card");

        VBox exerciseSetTableVBox = createExerciseSetTableVBox();
        exerciseSetTableVBox.getStyleClass().add("card");

        HBox libraryHBox = new HBox(workoutListVBox, exerciseListVBox, exerciseSetTableVBox);
        libraryHBox.setSpacing(25);
        libraryHBox.setPadding(view.returnInsets());
        setContent(libraryHBox);
    }
    private VBox createWorkoutListVBox() {
        workoutListView = new WorkoutListView();
        workoutListView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                // Single-click: show workout details
                Workout selected = workoutListView.getSelectedWorkout();
                if (selected != null) {
                    updateExercisesAndSets(selected);
                    previouslySelectedWorkout = selected;
                }
            }
            else if (e.getClickCount() == 2) {
                // Double-click: open workout in a new tab
                Workout selected = workoutListView.getSelectedWorkout();
                if (selected != null) {
                    controller.openWorkoutInNewTab(selected);
                }
            }
        });
        setOnRemoveWorkoutButtonClicked(controller::handleRemoveWorkout);
        setOnAddWorkoutButtonClicked(controller::handleAddWorkout);

        VBox buttonBox = setupWorkoutButtons();

        Label title = new Label("Workout Templates");
        title.getStyleClass().add("subheading");

        updateWorkoutListView(controller.getWorkoutList());

        VBox workoutVBox = new VBox(title, workoutListView, buttonBox);
        workoutVBox.setMaxSize(vboxWidth, vboxHeight);
        workoutVBox.setSpacing(2);
        workoutVBox.setFillWidth(true);

        return workoutVBox;
    }

/**
 * Helper-method for creating the VBox that shows the exercises of a workout.
 * @return The VBox
 */
private VBox createExerciseListVBox() {

        workoutName = new Label("Workout");
        workoutName.getStyleClass().add("subheading");
        workoutDescription = new TextArea("Workout Description");
        workoutDescription.setWrapText(true);
        workoutDescription.setEditable(true);
        workoutDescription.setOnKeyTyped(event -> {
            Workout selected =  workoutListView.getSelectedWorkout();
            if(selected != null) {
                selected.setDescription(workoutDescription.getText());
            }
        });
        workoutDescription.setMaxHeight(50);

        exerciseListView = new ExerciseListView();
        exerciseListView.setOnMouseClicked(event -> {
            List<Exercise> selectedExercises = exerciseListView.getSelectedExercises();
            if (event.getClickCount() == 1 && selectedExercises.size() == 1) {
                updateSetTableVBox(selectedExercises.getFirst());
                view.setMessage(selectedExercises.getFirst().getName() + "\n" + selectedExercises.getFirst().getDescription());
                Image img = controller.getExerciseImages().get(selectedExercises.getFirst().getName());
                imageView.setImage(Objects.requireNonNullElse(img, placeholderImg));
            }
        });

        Button addExerciseButton = new Button("Add Exercise");
        setOnAddExerciseButtonClicked(controller::handleAddExercise);
        addExerciseButton.setMaxWidth(Double.MAX_VALUE);
        addExerciseButton.setOnAction(event -> {
            if(onAddExerciseButtonClicked != null) {
                onAddExerciseButtonClicked.run();
            }
        });

        setOnRemoveExerciseButtonClicked(controller::handleRemoveExercise);

        Button removeExerciseButton = new Button("Remove Exercise");
        removeExerciseButton.setMaxWidth(Double.MAX_VALUE);
        removeExerciseButton.setOnAction(event -> {
            Exercise selectedExercise = exerciseListView.getSelectedExercise();
            if(onRemoveExerciseButtonClicked != null && selectedExercise != null) {
                onRemoveExerciseButtonClicked.accept(selectedExercise);
                clearSetTableVBox();
            } else {
                view.setMessage("Choose an exercise to remove!");
            }
        });

        VBox exerciseVBox = new VBox(workoutName, workoutDescription, exerciseListView, addExerciseButton, removeExerciseButton);
        exerciseVBox.setMaxSize(vboxWidth, vboxHeight);
        exerciseVBox.setSpacing(2);
        exerciseVBox.setFillWidth(true);
        exerciseVBox.setAlignment(Pos.CENTER);
        return  exerciseVBox;
    }

/**
 * Helper-method for creating the VBox that holds the set table that displays the sets of a exercise.
 * @return The VBox
 */
private VBox createExerciseSetTableVBox() {
        exerciseTitleLabel = new Label("Exercise Sets");
        exerciseTitleLabel.getStyleClass().add("subheading");
        setTableView = new ExerciseSetTableView();
        Button addSetBtn = new Button("Add Set");
        addSetBtn.setMaxWidth(Double.MAX_VALUE);
        addSetBtn.setOnAction(event -> {
            Workout w = workoutListView.getSelectedWorkout();
            Exercise e = exerciseListView.getSelectedExercise();
            if (w == null || e == null) {
                return;
            }
            List<ExerciseSet> sets = e.getSets();
            if (sets.isEmpty()) {
                controller.addExerciseSet(w, e, 0,0);
            } else {
                controller.addExerciseSet(w, e, sets.getLast().getReps(), sets.getLast().getWeight());
            }
        });

        Button removeSetBtn = new Button("Remove Set");
        removeSetBtn.setMaxWidth(Double.MAX_VALUE);
        removeSetBtn.setOnAction(event -> {
            Workout w = workoutListView.getSelectedWorkout();
            Exercise e = exerciseListView.getSelectedExercise();
            ExerciseSet set = setTableView.getSelectedSet();
            if (w == null || e == null || set == null) {
                return;
            }
            controller.removeExerciseSet(w, e, set);
        });

        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(200);
        //imageView.setFitWidth(vboxWidth);
        imageView.setImage(placeholderImg);

        VBox setTableVBox = new VBox(exerciseTitleLabel, setTableView, addSetBtn, removeSetBtn,  imageView);
        setTableVBox.setMaxSize(vboxWidth, vboxHeight);
        setTableVBox.setSpacing(2);
        setTableVBox.setAlignment(Pos.CENTER);
        return setTableVBox;
    }

    /**
     * Helper-method that creates the button-objects and configures them for user induced events.
     * @return An HBox with the buttons.
     */
    private VBox setupWorkoutButtons() {
        Button addWorkoutButton = new Button("Create New Workout");
        Button removeWorkoutButton = new Button("Remove Workout");

        addWorkoutButton.setMaxWidth(Double.MAX_VALUE);
        removeWorkoutButton.setMaxWidth(Double.MAX_VALUE);

        addWorkoutButton.setOnAction(e -> {
            if (onAddWorkoutButtonClicked == null) {
                return;
            }
            onAddWorkoutButtonClicked.run();
        });

        removeWorkoutButton.setOnAction(e -> {
            if (onRemoveWorkoutButtonClicked == null) return;

            Workout selectedWorkout = workoutListView.getSelectedWorkout();
            if (selectedWorkout != null) {
                onRemoveWorkoutButtonClicked.accept(selectedWorkout);
            }
        });

        VBox buttonVBox = new VBox(addWorkoutButton, removeWorkoutButton);
        buttonVBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addWorkoutButton, Priority.ALWAYS);
        HBox.setHgrow(removeWorkoutButton, Priority.ALWAYS);

        return buttonVBox;
    }

/**
 * Method for opening the dialog for adding a new workout.
 * @return A string array that holds the workout name at index 0 and the description at index 1.
 */
public String[] promptForNewWorkout() {
        WorkoutInputDialog dialog = new WorkoutInputDialog("New Workout", "Workout Name", "Workout Description");

        Optional<String[]> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Method that creates a dialog for checking if the user are sure about removing a workout.
     * @return True if user wants to remove workout.
     */
    public boolean promptForRemoveWorkout() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Remove Workout");
        Label removeWorkoutLabel = new Label("Remove Selected Workout?");
        removeWorkoutLabel.getStyleClass().add("subheading");
        VBox removeWorkoutVBox = new VBox(removeWorkoutLabel);
        removeWorkoutVBox.setMaxWidth(Double.MAX_VALUE);
        removeWorkoutVBox.setAlignment(Pos.CENTER);
        dialog.getDialogPane().setContent(removeWorkoutVBox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
        dialog.getDialogPane().getStylesheets().add(view.getScene().getStylesheets().getFirst());

        dialog.setResultConverter(buttonType -> buttonType == ButtonType.YES);
        Optional<Boolean> result = dialog.showAndWait();
        return result.orElse(false);
    }


    public WorkoutListView getWorkoutListView() {
        return workoutListView;
    }

    /**
     * Method for updating exerciseListView, workout name label, workout description and the exerciseSetTableView with the given workout.
     * @param workout the workout
     */
    public void updateExercisesAndSets(Workout workout) {
        exerciseListView.display(workout.getExercises());
        workoutName.setText(workout.getTitle());
        workoutDescription.setText(workout.getDescription());
        clearSetTableVBox();
    }

    /**
    * Method for resetting the labels, listview and tableview.
    */
    public void clearExercisesAndSets() {
        exerciseListView.clear();
        workoutName.setText("Workout");
        workoutDescription.setText("Workout Description");
        clearSetTableVBox();
    }

    /**
     * Method for updating the content of the name label and set table with the give exercise.
     * @param e the exercise.
     */
    public void updateSetTableVBox(Exercise e) {
        setTableView.display(e);
        exerciseTitleLabel.setText(e.getName());
    }
    public void clearSetTableVBox() {
        exerciseTitleLabel.setText("Exercise Sets");
        setTableView.clear();
    }

    /**
     * Method for setting the method to be called during button click.
     * @param callback the method to be called.
     */
    public void setOnAddExerciseButtonClicked(Runnable callback) {
        this.onAddExerciseButtonClicked = callback;
    }

    /**
     * Method for setting the method to be called during button click.
     * @param callback The method to be called.
     */
    public void setOnRemoveExerciseButtonClicked(Consumer<Exercise> callback) {
        this.onRemoveExerciseButtonClicked = callback;
    }

    /**
     * Method for setting the method to be called during button click.
     * @param callback the method to be called.
     */
    public void setOnAddWorkoutButtonClicked(Runnable callback) {
        this.onAddWorkoutButtonClicked = callback;
    }

    /**
     * Method for setting the method to be called during button click.
     * @param callback the method to be called.
     */
    public void setOnRemoveWorkoutButtonClicked(Consumer<Workout> callback) {
        this.onRemoveWorkoutButtonClicked = callback;
    }

    /**
     * Method for updating the list of workouts.
     * @param templates the list of workouts.
     */
    public void updateWorkoutListView(List<Workout> templates) {

        workoutListView.displayWorkouts(templates);

    }

    /**
     * Method for opening the dialog that lets the user pick exercises to add to a workout.
     * @return A list of exercises.
     */
    public List<Exercise> promptForAddingExercises() {
        ExercisePickerDialog dialog = new ExercisePickerDialog("Add Exercises", controller.getExerciseLibrary());
        Optional<List<Exercise>> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
