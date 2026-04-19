package view;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import model.Workout;

import java.util.List;

/**
 * Class that presents the list of workouts and other elements that lets the user modify the list of workouts.
 */
public class WorkoutListView extends ListView<Workout> {

    public WorkoutListView() {
        super();
    }

    /**
     * Method that updates the listview and also handles events regarding the listview.
     * @param workouts the workouts to display in the listview.
     */
    public void displayWorkouts(List<Workout> workouts) {
        this.setItems(FXCollections.observableList(workouts));
    }

    /**
     * Method for getting the selected workout from the listview.
     * @return the selected workout.
     */
    public Workout getSelectedWorkout() {
        return this.getSelectionModel().getSelectedItem();
    }
}
