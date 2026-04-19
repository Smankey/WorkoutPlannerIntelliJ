import controller.WorkoutPlannerController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.WorkoutManager;
import tools.jackson.core.exc.JacksonIOException;
import view.WorkoutPlannerView;

import java.io.File;

/**
 * The main class of the application.
 */
public class PlannerApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        WorkoutPlannerController plannerController = new WorkoutPlannerController();

        WorkoutManager manager = new WorkoutManager();
        plannerController.registerManager(manager);

        File workouts = new File("workoutLibrary/workouts.json");
        try {
            manager.setWorkouts(plannerController.loadWorkoutLibrary(workouts, "workoutLibrary"));

        } catch (JacksonIOException e) {
            System.out.println(e.getMessage());
        }

        WorkoutPlannerView plannerView = new WorkoutPlannerView(plannerController, stage);
        plannerController.registerView(plannerView);


        stage.setTitle("Workout Planner");
        stage.getIcons().add(new Image("/workout_planner_logo.png"));
        Scene scene = new Scene(plannerView);
        scene.getStylesheets().add(getClass().getResource("/css/workout-planner.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
