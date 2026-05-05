package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.File;
import java.util.List;

//loading and saving exercises
public class ExerciseLibraryManager {

    private static final ObjectMapper mapper = new ObjectMapper();

    // reading the exercises from a list in a json-file.
    public static List<Exercise> loadExercises() throws IOException {
        JsonNode root =  mapper.readTree(new File("exerciseLibrary/defaultExercises.json"));
        return mapper.treeToValue(root.get("exerciseLibrary"), new TypeReference<List<Exercise>>() {});
    }

    // saving the current exercise library to JSON format.
    public static void saveExercises(List<Exercise> exercises) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.putPOJO("exerciseLibrary", exercises);
        mapper.writeValue(new File("exerciseLibrary/defaultExercises.json"), root);
    }
}
