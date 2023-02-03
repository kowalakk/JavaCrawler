package application;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class ExtractPane extends GridPane {
    public CheckBox textCheckBox = new CheckBox("Text");
    public CheckBox attributeCheckBox = new CheckBox("Attribute");
    public TextField textField = new TextField();

    public ExtractPane() {
        super();
        this.setHgap(10);

        this.add(new Label("Extract:"), 0, 0);

        textCheckBox.setOnAction(e -> ExtractionModule.isExtractingText = textCheckBox.isSelected());
        this.add(textCheckBox, 1, 0);

        attributeCheckBox.setOnAction(e -> {
            textField.setDisable(!attributeCheckBox.isSelected());
            ExtractionModule.isExtractingAttribute = attributeCheckBox.isSelected();
        });
        this.add(attributeCheckBox, 2, 0);

        textField.setDisable(true);
        textField.setOnKeyTyped(e -> {
            ExtractionModule.attributeToExtract = textField.getText();
        });
        this.add(textField, 3, 0);
    }
}
