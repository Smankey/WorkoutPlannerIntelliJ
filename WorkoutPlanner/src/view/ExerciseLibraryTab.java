package view;

import controller.WorkoutPlannerController;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Exercise;

import java.util.List;
import java.util.Optional;

/**
 * A class that displays the exercise library.
 */
public class ExerciseLibraryTab extends Tab {

    private WorkoutPlannerView view;
    private WorkoutPlannerController controller;

    private ExerciseListView libraryListView;

    private Label exerciseNameLabel;
    private ImageView imgView;
    private TextArea exerciseDescription;
    private Button addButton, removeButton;

    public ExerciseLibraryTab(WorkoutPlannerView view, WorkoutPlannerController controller) {
        super("Exercise Library");
        this.view = view;
        this.controller = controller;

        this.controller.registerExerciseLibraryTab(this);
        setupTab();
    }

    /**
     * Helper-method for setting up the tab.
     */
    private void setupTab() {
        Label listName = new Label("Exercises in Library");
        listName.getStyleClass().add("subheading");

        exerciseNameLabel = new Label("Exercise Name");
        exerciseNameLabel.getStyleClass().add("subheading");

        libraryListView = new ExerciseListView();
        libraryListView.display(controller.getExerciseLibrary());

        Label boxTitle = new Label("New Exercise:");
        boxTitle.getStyleClass().add("subheading");

        TextField name =  new TextField();
        String namePrompt = "Exercise Name";
        name.setPromptText(namePrompt);

        TextField description =  new TextField();
        String descriptionPrompt = "Description";
        description.setPromptText(descriptionPrompt);

        String imgPrompt = "(Optional) URL/path to image";
        TextField imagePath = new TextField();
        imagePath.setPromptText(imgPrompt);

        addButton = new Button("New Exercise");
        addButton.setMaxWidth(Double.MAX_VALUE);

        removeButton = new Button("Remove Exercise");
        removeButton.setMaxWidth(Double.MAX_VALUE);

        addButton.setOnAction((e) -> {
            controller.addExerciseToLibrary();
        });

        removeButton.setOnAction((e) -> {
            Exercise selectedExercise = libraryListView.getSelectedExercise();
            if(selectedExercise != null) {
                controller.removeExerciseFromLibrary(selectedExercise);
            }
        });
        VBox listVBox = new VBox(listName, libraryListView, addButton, removeButton);

        imgView = new ImageView();
        imgView.setPreserveRatio(true);
        imgView.setFitWidth(250);

        libraryListView.setOnMouseClicked(event -> {
            Exercise selected = libraryListView.getSelectedExercise();
            if(selected != null) {
                /*
                TODO:
                    Display the name of the selected exercise on the designated Label
                    AND
                    display the description of the selected exercise in the designated TextArea.
                 */
                imgView.setImage(controller.getExerciseImages().get(selected.getName()));
            }
        });

        exerciseDescription = new TextArea();
        exerciseDescription.setWrapText(true);
        exerciseDescription.setMaxWidth(Double.MAX_VALUE);
        exerciseDescription.setPromptText("Exercise Description");
        exerciseDescription.setEditable(true);
        exerciseDescription.setOnKeyTyped(event -> {
            Exercise selectedExercise = libraryListView.getSelectedExercise();
            if(selectedExercise != null) {
                selectedExercise.setDescription(exerciseDescription.getText());
            }
        });

        VBox imgVBox = new VBox(exerciseNameLabel, exerciseDescription, imgView);
        imgVBox.setSpacing(10);
        listVBox.getStyleClass().add("card");
        imgVBox.getStyleClass().add("card");

        HBox tabHBox = new HBox(listVBox, imgVBox);
        tabHBox.setSpacing(20);
        tabHBox.setAlignment(Pos.CENTER);
        tabHBox.setPadding(view.returnInsets());

        listVBox.setMaxWidth(300);
        imgVBox.setMaxWidth(300);

        this.setContent(tabHBox);
    }

    /**
     * Method that opens the dialog for adding a new exercise.
     * @return A string array containing the exercise name at index 0, the exercise description at index 1 and an optional exercise image path at index 2.
     */
    public String[] promptForNewExercise() {
        ExerciseInputDialog dialog = new ExerciseInputDialog("New Exercise", "Exercise name", "Exercise description", "Image Path");

        Optional<String[]> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Method for displaying a list of exercises in the ListView.
     */
    public void displayExercises(List<Exercise> exercises) {
        libraryListView.display(exercises);
    }

    /**
     * Method for resetting the fields to default values.
     */
    public void clearFields() {
        exerciseDescription.clear();
        exerciseNameLabel.setText("Exercise Name");
        imgView.setImage(new Image("/workout_planner_logo.png"));
    }
}
