package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * A class that represents a dialog for entering info about a new exercise.
 */
public class ExerciseInputDialog extends Dialog<String[]> {

    private TextField nameField, descriptionField, imagePathField;

    public ExerciseInputDialog(String title, String label1, String label2, String label3) {
        setTitle(title);

        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        getDialogPane().getStylesheets().add(getClass().getResource("/css/workout-planner.css").toExternalForm());

        GridPane grid = createGridPane(label1, label2, label3);
        getDialogPane().setContent(grid);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (!nameField.getText().isEmpty() && !descriptionField.getText().isEmpty()) {
                    return new String[]{nameField.getText(), descriptionField.getText(), imagePathField.getText()};
                }
            }
            return null;
        });

        setOkBtnListener();

    }

    /**
     * Helper-method for creating the layout of the dialog.
     * @param label1 the first label
     * @param label2 the second label
     * @param label3 the third label
     * @return the GridPane.
     */
    private GridPane createGridPane(String label1, String label2, String label3) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nameField = new TextField();
        nameField.setPromptText(label1);

        descriptionField = new TextField();
        descriptionField.setPromptText(label2);

        imagePathField = new TextField();
        imagePathField.setPromptText(label3 + " (optional)");

        grid.add(new Label(label1 + ":"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(label2 + ":"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label(label3 + ":"), 0, 2);
        grid.add(imagePathField, 1, 2);

        return grid;
    }

    /**
     * Helper-method for blocking the ok-button if not all required fields are filled.
     */
    private void setOkBtnListener() {
        Button okBtn = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okBtn.setDisable(newValue.trim().isEmpty() || descriptionField.getText().trim().isEmpty());
        });
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            okBtn.setDisable(newValue.trim().isEmpty() || nameField.getText().trim().isEmpty());
        });
    }
}
