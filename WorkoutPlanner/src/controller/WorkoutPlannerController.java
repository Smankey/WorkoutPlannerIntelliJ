package controller;

import javafx.scene.image.Image;
import model.*;
import view.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import Export.WorkoutExporter;
import Export.JsonWorkoutExporter;
import Export.HtmlWorkoutExporter;

import javafx.stage.FileChooser;

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

    public void handleAddWorkout() {
        String[] workoutInfo = workoutLibraryTab.promptForNewWorkout();
        if (workoutInfo != null) {
            manager.addWorkout(workoutInfo[0], workoutInfo[1]);
            workoutLibraryTab.updateWorkoutListView(manager.getWorkouts());
        }
    }

    public void handleRemoveWorkout(Workout workout) {
        boolean check = workoutLibraryTab.promptForRemoveWorkout();
        if (check) {
            manager.removeWorkout(workout);
            workoutLibraryTab.updateWorkoutListView(manager.getWorkouts());
            workoutLibraryTab.clearExercisesAndSets();
            view.removeWorkoutTab(workout);
        }
    }

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

    public void addExerciseSet(Workout wo, Exercise ex, int reps, float weight) {
        manager.addSetToExercise(ex, reps, weight);
        if (view.hasActiveWorkoutTab(wo)) {
            registerActiveWorkoutTab(view.getActiveWorkoutTab(wo));
            activeWorkoutTab.displaySets(ex);
        }
        workoutLibraryTab.updateSetTableVBox(ex);
    }

    public void removeExerciseSet(Workout wo, Exercise ex, ExerciseSet set) {
        manager.removeSetFromExercise(ex, set);
        if (view.hasActiveWorkoutTab(wo)) {
            registerActiveWorkoutTab(view.getActiveWorkoutTab(wo));
            activeWorkoutTab.displaySets(ex);
        }
        workoutLibraryTab.updateSetTableVBox(ex);
    }

    public void handleSave() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files", "*.json"),
                new FileChooser.ExtensionFilter("HTML files", "*.html")
        );

        File file = chooser.showSaveDialog(view.getStage());
        if (file == null) return;

        WorkoutExporter exporter;

        if (file.getName().endsWith(".json")) {
            exporter = new JsonWorkoutExporter();
        } else {
            exporter = new HtmlWorkoutExporter();
        }

        try {
            exporter.export(manager.getWorkouts(), file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleLoad() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON files", "*.json")
        );

        File file = chooser.showOpenDialog(view.getStage());
        if (file == null) return;

        try {
            // Load new workouts
            List<Workout> imported = WorkoutsReader.load(file);

            // Clears old workouts before setting new ones
            manager.setWorkouts(imported);

            // Refreshes the UI
            workoutLibraryTab.updateWorkoutListView(manager.getWorkouts());

            view.setMessage("Import successful!");
        } catch (Exception e) {
            view.setMessage("Import failed: " + e.getMessage());
        }
    }



    public void addExerciseToLibrary() {
        String[] exerciseInfo = exerciseLibraryTab.promptForNewExercise();
        if (exerciseInfo != null) {
            if (exerciseInfo[2].isEmpty()) {
                manager.addExerciseToLibrary(exerciseInfo[0], exerciseInfo[1], null);
            } else {
                manager.addExerciseToLibrary(exerciseInfo[0], exerciseInfo[1], exerciseInfo[2]);
                manager.getExerciseImages().put(exerciseInfo[0], new Image(exerciseInfo[2]));
            }
            try {
                ExerciseLibraryManager.saveExercises(manager.getExerciseLibrary());
            } catch (IOException e) {
                e.printStackTrace();
                view.setMessage("Failed to save exercise library: " + e.getMessage());
            }
            exerciseLibraryTab.displayExercises(manager.getExerciseLibrary());
        }
    }

    public void removeExerciseFromLibrary(Exercise exercise) {
        manager.removeExerciseFromLibrary(exercise);
        exerciseLibraryTab.displayExercises(manager.getExerciseLibrary());
        exerciseLibraryTab.clearFields();

        try {
            ExerciseLibraryManager.saveExercises(manager.getExerciseLibrary());
        } catch (IOException e) {
            e.printStackTrace();
            view.setMessage("Failed to save exercise library: " + e.getMessage());
        }
    }

    public List<Workout> loadWorkoutLibrary(File file) throws IOException {
        return WorkoutsReader.load(file);
    }

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
