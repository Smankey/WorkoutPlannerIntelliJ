package model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.io.IOException;
import java.util.List;

// loading a list of workouts from a JSON-file.
public class WorkoutsReader {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    // loading a list of workouts from a JSON-file.

    public static List<Workout> load(File file) throws IOException {
        JsonNode root = mapper.readTree(file);
        return mapper.treeToValue(root, new TypeReference<List<Workout>>() {
        });
    }
}


