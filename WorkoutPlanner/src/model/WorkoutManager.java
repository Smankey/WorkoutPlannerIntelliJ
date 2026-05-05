package model;

import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// handles all workouts, workout history and anything regarding the exercises.
public class WorkoutManager {
    private List<Workout> workouts;

    private List<Exercise> exerciseLibrary;
    private Map<String, Image> exerciseImages;


    public WorkoutManager() {
        workouts = new ArrayList<>();

        try {
            exerciseLibrary = ExerciseLibraryManager.loadExercises();
        } catch (IOException e) {
            e.printStackTrace();
            exerciseLibrary = new ArrayList<>(); // fallback so the app doesn't crash
        }

        exerciseImages = ImageLoader.loadExerciseImages(exerciseLibrary);
    }


    // adding a workout to the workout planner.
    public void addWorkout(String title, String description) {
        Workout w = new Workout(title, description);
        workouts.add(w);
    }

    // removing a workout from the workout planner.
    public void removeWorkout(Workout workout) {
        workouts.remove(workout);
    }

    // adding existing exercises from the exercise library to a workout.
    public void addExercise(Workout workout, List<Exercise> exercises) {
        for (Exercise e : exercises) {
            workout.addExercise(e);
        }
    }

        // removing an exercise from a workout.
    public void removeExercise(Workout workout, Exercise exercise) {
            workout.removeExercise(exercise);
    }

    // adding a set to an exercise.
    public void addSetToExercise(Exercise exercise, int reps, float weight) {
            ExerciseSet set = new ExerciseSet(reps, weight);
            exercise.addExerciseSet(set);
    }

    // removing an exercise set from an exercise.
    public void removeSetFromExercise(Exercise exercise, ExerciseSet exerciseSet) {
            exercise.removeExerciseSet(exerciseSet);
    }

    // adding a new exercise to the exercise library
    public void addExerciseToLibrary(String name, String description, String imagePath) {
        exerciseLibrary.add(new Exercise(name, description, imagePath));
    }

    // removing an exercise from the exercise library.
    public void removeExerciseFromLibrary(Exercise exercise) {
        exerciseLibrary.remove(exercise);
    }


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
