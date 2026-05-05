package Export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Workout;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonWorkoutExporter implements WorkoutExporter {

    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public void export(List<Workout> workouts, File file) throws IOException {
        mapper.writeValue(file, workouts);
    }
}
