package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.File;
import java.util.List;

/**
 * Class with static methods for handling loading and saving exercises from and to the exercise library JSON-file.
 */
public class ExerciseLibraryManager {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method for reading the exercises from a list in a json-file. See <a href="https://github.com/FasterXML/jackson-databind/">the GitHub page</a>
     * on how to use it.
     * @return A list of Exercise objects read from the file.
     */
    public static List<Exercise> loadExercises() throws IOException {
        JsonNode root =  mapper.readTree(new File("exerciseLibrary/defaultExercises.json"));
        return mapper.treeToValue(root.get("exerciseLibrary"), new TypeReference<List<Exercise>>() {});
    }

    /**
     * Method for saving the current exercise library to JSON format. See <a href="https://github.com/FasterXML/jackson-databind/">the GitHub page</a>
     * on how to use it.
     */
    public static void saveExercises(List<Exercise> exercises) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.putPOJO("exerciseLibrary", exercises);
        mapper.writeValue(new File("exerciseLibrary/defaultExercises.json"), root);
    }
}
