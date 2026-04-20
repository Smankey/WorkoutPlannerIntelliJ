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
                Workout selected = workoutListView.getSelectedWorkout();
                if (selected != null) {
                    updateExercisesAndSets(selected);
                    previouslySelectedWorkout = selected;
                }
            }
            else if (e.getClickCount() == 2) {
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

            int[] values = promptForNewSet();
            if (values != null) {
                controller.addExerciseSet(w, e, values[0], values[1]);
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
        imageView.setImage(placeholderImg);

        VBox setTableVBox = new VBox(exerciseTitleLabel, setTableView, addSetBtn, removeSetBtn,  imageView);
        setTableVBox.setMaxSize(vboxWidth, vboxHeight);
        setTableVBox.setSpacing(2);
        setTableVBox.setAlignment(Pos.CENTER);
        return setTableVBox;
    }

    /**
     * Dialog for entering reps + weight for a new set.
     */
    private int[] promptForNewSet() {
        TextInputDialog repsDialog = new TextInputDialog();
        repsDialog.setHeaderText("Enter number of repetitions");
        repsDialog.setContentText("Reps:");

        Optional<String> repsResult = repsDialog.showAndWait();
        if (repsResult.isEmpty()) return null;

        TextInputDialog weightDialog = new TextInputDialog();
        weightDialog.setHeaderText("Enter weight in kg");
        weightDialog.setContentText("Weight:");

        Optional<String> weightResult = weightDialog.showAndWait();
        if (weightResult.isEmpty()) return null;

        try {
            int reps = Integer.parseInt(repsResult.get());
            int weight = Integer.parseInt(weightResult.get());
            return new int[]{reps, weight};
        } catch (NumberFormatException e) {
            return null;
        }
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

    public String[] promptForNewWorkout() {
        WorkoutInputDialog dialog = new WorkoutInputDialog("New Workout", "Workout Name", "Workout Description");

        Optional<String[]> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public void updateExerciseListView(List<Exercise> exercises) {
        exerciseListView.getItems().setAll(exercises);
    }

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

    public void updateExercisesAndSets(Workout workout) {
        exerciseListView.display(workout.getExercises());
        workoutName.setText(workout.getTitle());
        workoutDescription.setText(workout.getDescription());
        clearSetTableVBox();
    }

    public void clearExercisesAndSets() {
        exerciseListView.clear();
        workoutName.setText("Workout");
        workoutDescription.setText("Workout Description");
        clearSetTableVBox();
    }

    public void updateSetTableVBox(Exercise e) {
        setTableView.display(e);
        exerciseTitleLabel.setText(e.getName());
    }

    public void clearSetTableVBox() {
        exerciseTitleLabel.setText("Exercise Sets");
        setTableView.clear();
    }

    public void setOnAddExerciseButtonClicked(Runnable callback) {
        this.onAddExerciseButtonClicked = callback;
    }

    public void setOnRemoveExerciseButtonClicked(Consumer<Exercise> callback) {
        this.onRemoveExerciseButtonClicked = callback;
    }

    public void setOnAddWorkoutButtonClicked(Runnable callback) {
        this.onAddWorkoutButtonClicked = callback;
    }

    public void setOnRemoveWorkoutButtonClicked(Consumer<Workout> callback) {
        this.onRemoveWorkoutButtonClicked = callback;
    }

    public void updateWorkoutListView(List<Workout> templates) {
        workoutListView.displayWorkouts(templates);
    }

    public List<Exercise> promptForAddingExercises() {
        ExercisePickerDialog dialog = new ExercisePickerDialog("Add Exercises", controller.getExerciseLibrary());
        Optional<List<Exercise>> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
