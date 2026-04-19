package view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

/**
 * A class that represents a dialog for entering title and description of a new workout.
 */
public class WorkoutInputDialog extends Dialog<String[]> {

    private TextField nameField, descriptionField;

    public WorkoutInputDialog(String title, String label1, String label2) {
        setTitle(title);

        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        getDialogPane().getStylesheets().add(getClass().getResource("/css/workout-planner.css").toExternalForm());

        GridPane grid = createGridPane(label1, label2);
        getDialogPane().setContent(grid);

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (!nameField.getText().isEmpty()) {
                    return new String[]{nameField.getText(), descriptionField.getText()};
                }
            }
            return null;
        });

        setOkBtnListener();

    }

    /**
     * Helper-method for setting up the layout of the dialog.
     * @param label1 The first label (name)
     * @param label2 The second label (description)
     * @return The GridPane for the dialog.
     */
    private GridPane createGridPane(String label1, String label2) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nameField = new TextField();
        nameField.setPromptText(label1);

        descriptionField = new TextField();
        descriptionField.setPromptText(label2);

        grid.add(new Label(label1 + ":"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label(label2 + ":"), 0, 1);
        grid.add(descriptionField, 1, 1);

        return grid;
    }

    /**
     * Helper-method for disabling the button if not all required fields are filled.
     */
    private void setOkBtnListener() {
        Button okBtn = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okBtn.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okBtn.setDisable(newValue.trim().isEmpty());
        });
    }
}
