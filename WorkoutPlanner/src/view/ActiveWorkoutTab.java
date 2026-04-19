package view;

import controller.WorkoutPlannerController;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import model.Exercise;
import model.ExerciseSet;
import model.Workout;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * A class that represents an active workout.
 */
public class ActiveWorkoutTab extends Tab {

    private WorkoutPlannerView view;
    private WorkoutPlannerController controller;

    private ExerciseListView exerciseList;
    private ExerciseSetTableView setTable;

    private Workout workout;

    private Label exerciseTitleLabel;
    private TextArea descriptionTextArea;

    private ImageView exerciseImage;

    private final double vboxHeight = 400;
    private final double vboxWidth = 300;

    public ActiveWorkoutTab(WorkoutPlannerView view, Workout workout, WorkoutPlannerController controller) {
        super(workout.getTitle());
        setClosable(true);
        this.view = view;
        this.controller = controller;
        this.workout = workout;
        HBox tabHBox = initTab();
        tabHBox.getStyleClass().add("card");
        tabHBox.setSpacing(5);
        tabHBox.setPadding(view.returnInsets());
        //tabHBox.setAlignment(Pos.CENTER);

        setContent(tabHBox);

        this.setOnClosed(e -> {
            view.removeWorkoutTab(this.workout);
        });
    }

    /**
     * Helper-method for initializing the tab.
     * @return the HBox which is used to structure the contents of the tab.
     */
    private HBox initTab() {
        HBox hBox = new HBox();

        VBox exerciseListVBox = new VBox();
        exerciseListVBox.setMaxSize(vboxWidth, vboxHeight);
        exerciseListVBox.setSpacing(2);
        Label titleLabel = new Label("Exercises");
        exerciseList = new ExerciseListView();
        exerciseList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Exercise selectedExercise = exerciseList.getSelectedExercise();
                if (selectedExercise != null) {
                    displaySets(selectedExercise);
                    exerciseImage.setImage(controller.getExerciseImages().get(selectedExercise.getName()));
                }
            }
        });
        exerciseList.display(workout.getExercises());
        exerciseListVBox.getChildren().addAll(titleLabel, exerciseList);
        VBox tableVBox = initTableView();
        tableVBox.setMaxWidth(250);
        tableVBox.setSpacing(2);
        tableVBox.setMaxSize(vboxWidth, vboxHeight);
        Label latestWorkoutLabel = new Label("Last performed: Never! A new workout, cool!");
        if (workout.getDateTime() != null) {
            latestWorkoutLabel.setText("Last performed: " + workout.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + " " + workout.getDateTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        }

        exerciseImage = new ImageView();
        exerciseImage.setPreserveRatio(true);
        exerciseImage.setFitWidth(vboxWidth);
        VBox finishVBox = new VBox(exerciseImage);
        finishVBox.setMaxSize(vboxWidth, vboxHeight);
        hBox.getChildren().addAll(exerciseListVBox, tableVBox, finishVBox);
        return hBox;
    }


    /**
     * Helper-method for setting up the VBox that displays the exercise name, description, sets and buttons for adding and removing sets.
     * @return the VBox.
     */
    private VBox initTableView() {
        setTable = new ExerciseSetTableView();

        TableColumn<ExerciseSet, Integer> completedRepsCol = createCompletedRepsCol();

        setTable.getColumns().addAll(completedRepsCol);

        exerciseTitleLabel = new Label("Exercise Sets");
        descriptionTextArea = new TextArea("Exercise Description");
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setWrapText(true);
        descriptionTextArea.setMaxHeight(50);

        Button addSetButton = new Button("Add Set");
        Button removeSetButton = new Button("Remove Set");

        addSetButton.setMaxWidth(Double.MAX_VALUE);
        removeSetButton.setMaxWidth(Double.MAX_VALUE);

        addSetButton.setOnAction(e -> {
            Exercise exercise = exerciseList.getSelectedExercise();
            List<ExerciseSet> sets = exercise.getSets();
            if (!sets.isEmpty()) {
                controller.addExerciseSet(workout, exercise, sets.getLast().getReps(), sets.getLast().getWeight());
            } else {
                controller.addExerciseSet(workout, exercise, 0, 0);
            }
        });

        removeSetButton.setOnAction(e -> {
            Exercise exercise = exerciseList.getSelectedExercise();
            ExerciseSet set = setTable.getSelectedSet();
            if (exercise != null && set != null) {
                controller.removeExerciseSet(workout, exercise, set);
            }
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(exerciseTitleLabel, descriptionTextArea, setTable, addSetButton, removeSetButton);
        return vBox;

    }

    /**
     * Helper-method for adding the column for displaying and editing the number of completed repetitions.
     * @return the editable TableColumn.
     */
    private TableColumn<ExerciseSet, Integer> createCompletedRepsCol() {
        TableColumn<ExerciseSet, Integer> completedRepsCol = new TableColumn<>("Completed Repetitions");

        completedRepsCol.setStyle( "-fx-alignment: CENTER;");

        completedRepsCol.setCellValueFactory(new PropertyValueFactory<ExerciseSet, Integer>("completedReps"));
        completedRepsCol.setCellFactory(col ->
            new TextFieldTableCell<>(new IntegerStringConverter()) {
                @Override
                public void commitEdit(Integer value) {
                    super.commitEdit(value);
                }
            }
        );
        completedRepsCol.setOnEditCommit(
            (TableColumn.CellEditEvent<ExerciseSet, Integer> t) -> {
                ((ExerciseSet) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setCompletedReps(t.getNewValue());
            }
        );
        return completedRepsCol;
    }

    /**
     * Method for displaying the exercises of a workout in the tab.
     * @param workout the workout that holds the exercises.
     */
    public void displayExercises(Workout workout) {
        exerciseList.display(workout.getExercises());
    }

    /**
     * Method for displaying the exercise name, description and sets of a given exercise.
     * @param e the exercise.
     */
    public void displaySets(Exercise e) {
        exerciseTitleLabel.setText(e.getName());
        descriptionTextArea.setText(e.getDescription());
        setTable.display(e);
    }

}
