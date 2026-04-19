package model;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

/**
 * A class for loading a list of workouts from a JSON-file.
 */
public class WorkoutsReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Method for loading a list of workouts from a JSON-file. See <a href="https://github.com/FasterXML/jackson-databind/">the GitHub page</a>
     * on how to use it.
     * @param file the file to load the list of workouts from.
     * @param propertyName the property name in the JSON-file.
     * @return a list of workouts.
     */

    public static List<Workout> load(File file, String propertyName) {
        JsonNode root = mapper.readTree(file);
        return mapper.treeToValue(root.get(propertyName), new TypeReference<List<Workout>>() {});
    }
}
