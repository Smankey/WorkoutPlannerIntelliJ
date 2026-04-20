package controller;

import javafx.scene.image.Image;
import model.*;
import view.*;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class WorkoutPlannerController {
    private WorkoutPlannerView view;
    private WorkoutManager manager;
    private ExerciseLibraryTab exerciseLibraryTab;
    private WorkoutLibraryTab workoutLibraryTab;

    private ActiveWorkoutTab activeWorkoutTab;

    public void registerView(WorkoutPlannerView view) {
        this.view = view;
    }

    public void registerManager(WorkoutManager manager) {
        this.manager = manager;
    }

    public void registerExerciseLibraryTab(ExerciseLibraryTab exerciseLibraryTab) {
        this.exerciseLibraryTab = exerciseLibraryTab;
    }

    public void registerLibraryTab(WorkoutLibraryTab workoutLibraryTab) {
        this.workoutLibraryTab = workoutLibraryTab;
    }

    public void registerActiveWorkoutTab(ActiveWorkoutTab activeWorkoutTab) {
        this.activeWorkoutTab = activeWorkoutTab;
    }

    /**
     * Method that adds a workout to the workout library and updated the view.
     */
    public void handleAddWorkout() {
        String[] workoutInfo = workoutLibraryTab.promptForNewWorkout();
        if (workoutInfo != null) {
            manager.addWorkout(workoutInfo[0], workoutInfo[1]);
            workoutLibraryTab.updateWorkoutListView(manager.getWorkouts());
        }
    }

    /**
     * Method that removes a workout from the workout library and updates the view.
     * @param workout the workout to remove.
     */
    public void handleRemoveWorkout(Workout workout) {
        boolean check = workoutLibraryTab.promptForRemoveWorkout();
        if (check) {
            manager.removeWorkout(workout);
            workoutLibraryTab.updateWorkoutListView(manager.getWorkouts());
            workoutLibraryTab.clearExercisesAndSets();
            view.removeWorkoutTab(workout);
        }
    }

    /**
     * Method that adds a list of exercises to a selected workout and updates the view.
     */
    public void handleAddExercise() {
        List<Exercise> newExercises = workoutLibraryTab.promptForAddingExercises();
        Workout selectedWorkout = view.getSelectedWorkoutFromListView();
        if (selectedWorkout != null && newExercises != null) {


            for (Exercise e : newExercises) {
                selectedWorkout.addExercise(e);
            }

            workoutLibraryTab.updateExerciseListView(selectedWorkout.getExercises());

            if (view.hasActiveWorkoutTab(selectedWorkout)) {
                view.getActiveWorkoutTab(selectedWorkout).displayExercises(selectedWorkout);
            }
        } else {
            view.setMessage("Please select a workout from the list");
        }
    }

    /**
     * Method that removes an exercise from a selected workout and updates the view.
     * @param exercise the exercise to remove.
     */
    public void handleRemoveExercise(Exercise exercise) {
        Workout selectedWorkout = view.getSelectedWorkoutFromListView();
        if (selectedWorkout != null) {


            selectedWorkout.removeExercise(exercise);

            workoutLibraryTab.updateExerciseListView(selectedWorkout.getExercises());

            if (view.hasActiveWorkoutTab(selectedWorkout)) {
                view.getActiveWorkoutTab(selectedWorkout).displayExercises(selectedWorkout);
            }
        }
    }

    /**
     * Method that adds a new set to an exercise in a workout.
     * @param wo The workout.
     * @param ex The exercise.
     * @param reps The number of repetitions of the set.
     * @param weight The weight to be lifted during the set.
     */
    public void addExerciseSet(Workout wo, Exercise ex, int reps, float weight) {
        manager.addSetToExercise(ex, reps, weight);
        if (view.hasActiveWorkoutTab(wo)) {
            registerActiveWorkoutTab(view.getActiveWorkoutTab(wo));
            activeWorkoutTab.displaySets(ex);
        }
        workoutLibraryTab.updateSetTableVBox(ex);
    }

    /**
     * Method for removing a set from an exercise in a workout.
     * @param wo The workout.
     * @param ex The exercise.
     * @param set The set to remove.
     */
    public void removeExerciseSet(Workout wo, Exercise ex, ExerciseSet set) {
        manager.removeSetFromExercise(ex, set);
        if (view.hasActiveWorkoutTab(wo)) {
            registerActiveWorkoutTab(view.getActiveWorkoutTab(wo));
            activeWorkoutTab.displaySets(ex);
        }
        workoutLibraryTab.updateSetTableVBox(ex);
    }

    /**
     * Method that handles the request of adding a new exercise to the exercise library.
     */
    public void addExerciseToLibrary() {
        String[] exerciseInfo = exerciseLibraryTab.promptForNewExercise();
        if (exerciseInfo != null) {
            if (exerciseInfo[2].isEmpty()) {
                manager.addExerciseToLibrary(exerciseInfo[0], exerciseInfo[1], null);
            } else {
                manager.addExerciseToLibrary(exerciseInfo[0], exerciseInfo[1], exerciseInfo[2]);
                manager.getExerciseImages().put(exerciseInfo[0], new Image(exerciseInfo[2]));
            }
            ExerciseLibraryManager.saveExercises(manager.getExerciseLibrary());
            exerciseLibraryTab.displayExercises(manager.getExerciseLibrary());
        }
    }

    /**
     * Method that handles the request of removing an exercise from the exercise library.
     * @param exercise the exercise to remove.
     */
    public void removeExerciseFromLibrary(Exercise exercise) {
        manager.removeExerciseFromLibrary(exercise);
        exerciseLibraryTab.displayExercises(manager.getExerciseLibrary());
        exerciseLibraryTab.clearFields();
        ExerciseLibraryManager.saveExercises(manager.getExerciseLibrary());
    }

    /**
     * Method for loading a list of workouts from a file.
     * @param file The file to load from.
     * @param propertyName The property name in the JSON-file.
     * @return The list of workouts from the file.
     */
    public List<Workout> loadWorkoutLibrary(File file,  String propertyName) {
        return WorkoutsReader.load(file, propertyName);
    }

    /*
     * **********************************
     * Getters from the workout manager.
     * **********************************
     */

    public List<Workout> getWorkoutList() {
        return manager.getWorkouts();
    }
    public List<Exercise> getExerciseLibrary() {
        return manager.getExerciseLibrary();
    }
    public Map<String, Image> getExerciseImages() {
        return manager.getExerciseImages();
    }

    public void openWorkoutInNewTab(Workout workout) {
        view.openWorkoutTab(workout);
    }
}
