package model;

import java.util.ArrayList;
import java.util.List;

public class Exercise {

    private String imagePath;
    private String name;
    private String description;
    private List<ExerciseSet> sets;

    // ⭐ REQUIRED FOR JACKSON
    public Exercise() {
        this.name = "";
        this.description = "";
        this.imagePath = null;
        this.sets = new ArrayList<>();
    }

    public Exercise(String name, String description, String imagePath) {
        assert name != null && !name.isEmpty();
        assert description != null;

        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.sets = new ArrayList<>();

        checkInvariant();
    }

    // For backwards compatibility
    public Exercise(String name, String description) {
        this(name, description, null);
    }

    // Adds a set to the exercise
    public void addSet(ExerciseSet set) {
        assert set != null;
        sets.add(set);
        assert sets.contains(set);
        checkInvariant();
    }

    // Removes a set from the exercise
    public void removeSet(ExerciseSet set) {
        assert set != null;
        sets.remove(set);
        assert !sets.contains(set);
        checkInvariant();
    }

    // GUI expects these names:
    public void addExerciseSet(ExerciseSet set) {
        addSet(set);
    }

    public void removeExerciseSet(ExerciseSet set) {
        removeSet(set);
    }

    public List<ExerciseSet> getSets() {
        return new ArrayList<>(sets);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // GUI needs this to allow editing description
    public void setDescription(String description) {
        this.description = description;
    }

    // Image path getter/setter
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    private void checkInvariant() {
        assert name != null;
        assert description != null;
        assert sets != null;
    }
    @Override
    public String toString() {
        return name;
    }

}
