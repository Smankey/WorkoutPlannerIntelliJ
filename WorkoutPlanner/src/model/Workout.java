package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Represents a workout with a name, description and a list of exercises
public class Workout {

    // Basic workout info
    private String imagePath;
    private String name;
    private String description;
    private List<Exercise> exercises;

    // Timestamp for when the workout was created
    private LocalDateTime dateTime;

    // Addition required for JACKSON
    public Workout() {
        this.imagePath = null;
        this.name = "";
        this.description = "";
        this.exercises = new ArrayList<>();
        this.dateTime = LocalDateTime.now();
    }

    // Create a workout with name and description
    public Workout(String name, String description) {
        assert name != null && !name.isEmpty();
        assert description != null;

        this.name = name;
        this.description = description;
        this.exercises = new ArrayList<>();
        this.dateTime = LocalDateTime.now();

        checkInvariant();
    }

    // Add an exercise
    public void addExercise(Exercise e) {
        assert e != null;

        exercises.add(e);

        assert exercises.contains(e);
        checkInvariant();
    }

    // Remove an exercise
    public void removeExercise(Exercise e) {
        assert e != null;

        exercises.remove(e);

        assert !exercises.contains(e);
        checkInvariant();
    }

    // Return a copy of the exercise list
    public List<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // GUI expects getTitle() instead of getName()
    public String getTitle() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    private void checkInvariant() {
        assert name != null;
        assert description != null;
        assert exercises != null;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }
}
