package model;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class that handles all workouts, workout history and anything regarding the exercises.
 * Functions as an interface towards the controller.
 */
public class WorkoutManager {
    private List<Workout> workouts;

    private List<Exercise> exerciseLibrary;
    private Map<String, Image> exerciseImages;


    public WorkoutManager() {
        workouts = new ArrayList<>();
        exerciseLibrary = ExerciseLibraryManager.loadExercises();
        exerciseImages = ImageLoader.loadExerciseImages(exerciseLibrary);
    }

    /**
     * Method for adding a workout to the workout planner.
     * @param title the title of the new workout.
     * @param description the description of the new workout.
     */
    public void addWorkout(String title, String description) {
        Workout w = new Workout(title, description);
        workouts.add(w);
    }

    /**
     * Method for removing a workout from the workout planner.
     * @param workout the workout to remove.
     */
    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
    }

    /**
     * Method for adding existing exercises from the exercise library to a workout.
     * @param workout the workout to which to add the exercises.
     * @param exercises the exercises to add.
     */
    public void addExercise(Workout workout, List<Exercise> exercises) {
        for (Exercise e : exercises) {
            workout.addExercise(e);
        }
    }

        /**
         * Method for removing an exercise from a workout.
         * @param workout the workout which the exercise is to be removed from.
         * @param exercise the exercise to remove.
         */
    public void removeExercise(Workout workout, Exercise exercise) {
            workout.removeExercise(exercise);
    }

    /**
     * Method for adding a set to an exercise.
     * @param exercise the exercise.
     * @param reps repetitions of the set.
     * @param weight weight in kg of the set.
     */
    public void addSetToExercise(Exercise exercise, int reps, float weight) {
            ExerciseSet set = new ExerciseSet(reps, weight);
            exercise.addExerciseSet(set);
    }

    /**
     * Method for removing an exercise set from an exercise.
     * @param exercise the exercise.
     * @param exerciseSet the set to remove.
     */
    public void removeSetFromExercise(Exercise exercise, ExerciseSet exerciseSet) {
            exercise.removeExerciseSet(exerciseSet);
    }

    /**
     * Method for adding a new exercise to the exercise library.
     * @param name name of the exercise.
     * @param description description for the exercise.
     * @param imagePath path to a local image file or URL to an online image.
     */
    public void addExerciseToLibrary(String name, String description, String imagePath) {
        exerciseLibrary.add(new Exercise(name, description, imagePath));
    }

    /**
     * Method for removing an exercise from the exercise library.
     * @param exercise the exercise to remove.
     */
    public void removeExerciseFromLibrary(Exercise exercise) {
        exerciseLibrary.remove(exercise);
    }


    /*
     * ********************
     * GETTERS AND SETTERS
     * ********************
     */

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<Exercise> getExerciseLibrary() {
        return exerciseLibrary;
    }

    public void setExerciseLibrary(List<Exercise> exerciseLibrary) {
        this.exerciseLibrary = exerciseLibrary;
    }

    public Map<String, Image> getExerciseImages() {
        return exerciseImages;
    }

    public void setExerciseImages(Map<String, Image> exerciseImages) {
        this.exerciseImages = exerciseImages;
    }

}
