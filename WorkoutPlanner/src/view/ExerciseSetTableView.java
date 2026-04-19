package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.Exercise;
import model.ExerciseSet;

/**
 * Class that presents the sets of an exercise in a table view.
 */
public class ExerciseSetTableView extends TableView<ExerciseSet> {

    private TableColumn<ExerciseSet, Integer> setRepsCol;
    private TableColumn<ExerciseSet, Float> setWeightCol;

    public ExerciseSetTableView() {
        super();
        setupTable();
    }

    /**
     * Helper-method for settig up the set table.
     */
    private void setupTable() {
        setRepsCol = new TableColumn<>("Repetitions");
        setWeightCol = new TableColumn<>("Weight (kg)");

        setRepsCol.setStyle( "-fx-alignment: CENTER;");
        setWeightCol.setStyle( "-fx-alignment: CENTER;");

        setRepsCol.setCellValueFactory(new PropertyValueFactory<ExerciseSet, Integer>("reps"));

        setRepsCol.setCellFactory(col ->
                new TextFieldTableCell<>(new IntegerStringConverter()) {
                    @Override
                    public void commitEdit(Integer value) {
                        super.commitEdit(value);
                    }
                });

        setRepsCol.setOnEditCommit(
                (TableColumn.CellEditEvent<ExerciseSet, Integer> t) -> {
                    ((ExerciseSet) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setReps(t.getNewValue());
                }
        );

        setWeightCol.setCellValueFactory(new PropertyValueFactory<ExerciseSet, Float>("weight"));
        setWeightCol.setCellFactory(col ->
                new TextFieldTableCell<>(new FloatStringConverter()) {
                    @Override
                    public void commitEdit(Float value) {
                        super.commitEdit(value);
                    }
                });
        setWeightCol.setOnEditCommit(
                (TableColumn.CellEditEvent<ExerciseSet, Float> t) -> {
                    ((ExerciseSet) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                    ).setWeight(t.getNewValue());
                }
        );

        this.setEditable(true);

        this.getColumns().addAll(setRepsCol, setWeightCol);
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Method for updating the tableview.
     * @param exercise the exercise that holds the sets to present.
     */
    public void display(Exercise exercise){
        ObservableList<ExerciseSet> exerciseSets = FXCollections.observableArrayList(exercise.getSets());
        this.setItems(exerciseSets);
    }
    public void clear() {
        this.getItems().clear();
    }

    public ExerciseSet getSelectedSet() {
        return getSelectionModel().getSelectedItem();
    }
}
