package application;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Objects;

public class FilterPane extends GridPane {
    public ComboBox<String> comboBox = new ComboBox<>();
    public TextField textField = new TextField();

    public FilterPane() {
        super();
        this.setHgap(10);

        this.add(new Label("Filter by:"), 0, 0);

        textField.setDisable(true);
        textField.setOnKeyTyped(e -> {
            ExtractionModule.filterInput = textField.getText();
        });
        this.add(textField, 2, 0);

        comboBox.getItems().addAll("None", "Type", "Class", "Id");
        comboBox.getSelectionModel().select(0);
        comboBox.setOnAction(e -> {
            textField.setDisable(Objects.equals(comboBox.getValue(), "None"));
            ExtractionModule.selectedFilter = comboBox.getValue();
        });
        this.add(comboBox, 1, 0);
    }
}
