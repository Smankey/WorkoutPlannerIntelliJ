package view;

import javafx.collections.FXCollections;
import javafx.scene.control.ListView;
import model.Exercise;

import java.util.List;

/**
 * Class that presents the exercises of a workout in a listview. Also holds the buttons for editing the list of exercises.
 */
public class ExerciseListView extends ListView<Exercise> {

    public ExerciseListView() {
        super();
    }

    /**
     * Method that updates the list view.
     * @param exercises which exercises to be displayed.
     */
    public void display(List<Exercise> exercises) {
        this.setItems(FXCollections.observableList(exercises));
    }

    public void clear() {
        this.getItems().clear();
    }




    public Exercise getSelectedExercise() {
        return this.getSelectionModel().getSelectedItem();
    }

    public List<Exercise> getSelectedExercises() {
        return this.getSelectionModel().getSelectedItems();
    }
}
