package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * A class for loading a list of workouts from a JSON-file.
 */
public class WorkoutsReader {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    /**
     * Method for loading a list of workouts from a JSON-file. See <a href="https://github.com/FasterXML/jackson-databind/">the GitHub page</a>
     * on how to use it.
     *
     * @param file         the file to load the list of workouts from.
     * @param propertyName the property name in the JSON-file.
     * @return a list of workouts.
     */

    public static List<Workout> load(File file) throws IOException {
        JsonNode root = mapper.readTree(file);
        return mapper.treeToValue(root, new TypeReference<List<Workout>>() {
        });
    }
}


