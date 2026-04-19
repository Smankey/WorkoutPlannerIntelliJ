package view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Exercise;

import java.util.List;

/**
 * A class that displays the exercise library to choose exercises from.
 */
public class ExercisePickerDialog extends Dialog<List<Exercise>> {

    private ExerciseListView exerciseListView;

    public ExercisePickerDialog(String title, List<Exercise> library) {

        setTitle(title);
        Label titleLabel = new Label("Exercise Library");
        getDialogPane().getStylesheets().add(getClass().getResource("/css/workout-planner.css").toExternalForm());
        titleLabel.getStyleClass().add("subheading");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        exerciseListView = new ExerciseListView();
        exerciseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        exerciseListView.display(library);
        VBox vBox = new VBox(titleLabel, exerciseListView);
        getDialogPane().setContent(vBox);

        setOkBtnListener();

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                return exerciseListView.getSelectedExercises();
            }
            return null;
        });
    }

    /**
     * Helper-method for disable button until at least one exercise is chosen.
     */
    private void setOkBtnListener() {
        Button okBtn = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setDisable(true);
        exerciseListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                okBtn.setDisable(false);
            }
        });
    }
}
