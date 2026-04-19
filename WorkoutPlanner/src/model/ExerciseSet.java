package model;

/**
 * This class represents a single set during an exercise. It
 * contains basic information about the set with both
 * setters and getters to access and modify that information.
 */
public class ExerciseSet {
    private int reps;          // The number of repetitions to complete
    private float weight;      // The weights to use for the set
    private int completedReps; // The number of completed repetitions

    /**
     * A basic constructor.
     * @param reps The number of repetitions to complete
     * @param weight The weights to use for the set
     */
    public ExerciseSet(int reps, float weight) {
        this.reps = reps;
        this.weight = weight;
        completedReps = 0;
    }

    /**
     * A copy constructor. Generates a copy of another object of the
     * same type
     * @param original The object to copy
     */
    public ExerciseSet(ExerciseSet original) {
        this.reps = original.getReps();
        this.weight = original.getWeight();
        this.completedReps = original.getCompletedReps();
    }

    /**
     * No-arg constructor
     */
    public ExerciseSet() {}

    /**
     * Returns the number of repetitions to complete
     * @return the number of repetitions to complete
     */
    public int getReps() {
        return reps;
    }

    /**
     * Sets the number of repetitions to complete
     * @param reps the number of repetitions
     */
    public void setReps(int reps) {
        this.reps = reps;
    }

    /**
     * Returns the weights to use for the set
     * @return the weights to use
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Sets the weights to use for the set
     * @param weight the weights to use
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Returns the number of repetitions completed
     * @return the number of completed repetitions
     */
    public int getCompletedReps() {
        return completedReps;
    }

    /**
     * Sets the number of completed repetitions
     * @param completedReps the number of completed repetitions
     */
    public void setCompletedReps(int completedReps) {
        this.completedReps = completedReps;
    }
}
