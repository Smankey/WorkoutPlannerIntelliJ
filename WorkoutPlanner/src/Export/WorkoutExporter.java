package Export;

import model.Workout;
import java.io.File;
import java.util.List;

public interface WorkoutExporter {
    void export(List<Workout> workouts, File file) throws Exception;
}
