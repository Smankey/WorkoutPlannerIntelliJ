package Export;

import model.Workout;
import model.Exercise;
import model.ExerciseSet;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class HtmlWorkoutExporter implements WorkoutExporter {

    @Override
    public void export(List<Workout> workouts, File file) throws Exception {
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h1>Workout Programs</h1>");

        for (Workout w : workouts) {
            html.append("<h2>").append(w.getTitle()).append("</h2>");
            html.append("<p>").append(w.getDescription()).append("</p>");

            html.append("<ul>");
            for (Exercise e : w.getExercises()) {
                html.append("<li>")
                        .append("<b>").append(e.getName()).append("</b>: ")
                        .append(e.getDescription())
                        .append("<ul>");

                for (ExerciseSet set : e.getSets()) {
                    html.append("<li>")
                            .append(set.getReps()).append(" reps @ ")
                            .append(set.getWeight()).append(" kg")
                            .append("</li>");
                }

                html.append("</ul></li>");
            }
            html.append("</ul>");
        }

        html.append("</body></html>");

        Files.writeString(file.toPath(), html.toString());
    }
}
