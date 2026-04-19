package model;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class with a static method for loading images to the exercise image-map.
 */
public class ImageLoader {

    /**
     * Method for reading the exercise images from the image library.
     * @return A Map with the exercise name as the key and the image object as the value.
     */
    public static Map<String, Image> loadExerciseImages(List<Exercise> exercises) {
        Map<String, Image> imageMap = new HashMap<>();
        for (Exercise exercise : exercises) {
            if (exercise.getImagePath() != null) {
                try {
                    Image image = new Image(exercise.getImagePath());
                    imageMap.put(exercise.getName(), image);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("incorrect imagePath for exercise: " + exercise.getName());
                }
            }
        }
        return imageMap;
    }
}
