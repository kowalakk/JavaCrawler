package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import static application.SaveModule.destinationFile;

public class FilePane extends GridPane {
    public Button saveButton;
    public Label label = new Label("");
    public FilePane() {
        super();
        this.setHgap(10);

        this.add(new Label("Save to file:"), 0, 0);

        this.add(label, 2, 0);

        Button chooseFileButton = new Button("Choose file");
        chooseFileButton.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser ();
                fileChooser.setTitle("Save to file");
                SaveModule.destinationFile = fileChooser.showSaveDialog(null);
                if (SaveModule.destinationFile != null){
                    saveButton.setDisable(false);
                    label.setText(SaveModule.destinationFile.getName());
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(chooseFileButton, 1, 0);

        saveButton = new Button("Save");
        saveButton.setDisable(SaveModule.destinationFile == null);
        this.add(saveButton, 3, 0);
    }
}
