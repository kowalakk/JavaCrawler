package application;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class UrlPane extends GridPane {
    public TextField textField = new TextField();
    public Button button = new Button("Extract");

    public UrlPane() {
        super();
        this.setHgap(10);

        ColumnConstraints column0 = new ColumnConstraints(25, 25, Double.MAX_VALUE);
        ColumnConstraints column1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(55, 55, Double.MAX_VALUE);
        this.getColumnConstraints().addAll(column0, column1, column2);

        this.add(new Label("URL:"), 0, 0);

        this.add(textField, 1, 0);

        this.add(button, 2, 0);
    }
}
