package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.Objects;

public class WebCrawler extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane root = new GridPane();
        root.setVgap(10);
        root.setHgap(20);

        // creating columns and rows
        ColumnConstraints column0 = new ColumnConstraints();
        ColumnConstraints column1 = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        ColumnConstraints column2 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        root.getColumnConstraints().addAll(column0, column1, column2);

        for (int i = 0; i < 4; i++) {
            RowConstraints row = new RowConstraints();
            root.getRowConstraints().add(row);
        }
        RowConstraints row4 = new RowConstraints();
        row4.setVgrow(Priority.ALWAYS);
        root.getRowConstraints().add(row4);
        root.getRowConstraints().add(new RowConstraints());

        // controls regarding URL input
        UrlPane urlPane = new UrlPane();
        root.add(urlPane, 1, 1);

        FlowPane flowPane = new FlowPane();
        flowPane.setVgap(10);
        flowPane.setHgap(40);
        // controls regarding filtering data
        FilterPane filterPane = new FilterPane();
        // controls regarding extracting data
        ExtractPane extractPane = new ExtractPane();
        // controls regarding saving data to file
        FilePane filePane = new FilePane();
        flowPane.getChildren().addAll(filterPane, extractPane, filePane);
        root.add(flowPane, 1, 2);

        // checkbox regarding importing previous settings
        CheckBox usePreviousSettingsCheckbox = new CheckBox("Use previous settings");
        usePreviousSettingsCheckbox.setOnAction(e -> {
            SaveModule.getSettingsFromFile(urlPane.textField, filterPane, extractPane, filePane);
        });
        root.add(usePreviousSettingsCheckbox, 1, 3);

        // area to show extracted data
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        root.add(textArea, 1, 4);

        // action for extracting data
        urlPane.button.setOnAction(e -> {
            try {
                String newUrl = urlPane.textField.getText();
                if (!Objects.equals(newUrl, ExtractionModule.url)) {
                    ExtractionModule.url = newUrl;
                    ExtractionModule.openUrl(); // open URL only if it changed
                }
                if (ExtractionModule.document == null)
                    textArea.setText("Requested URL cannot be reached");
                else
                    textArea.setText(ExtractionModule.filterDocument());
            } catch (IOException ex) {
                textArea.setText("Requested URL cannot be reached");
            }
        });

        // action for saving extracted data to file
        filePane.saveButton.setOnAction(e -> {
            SaveModule.saveDataToFile(textArea.getText());
            SaveModule.saveSettingsToFile();
        });

        Scene scene = new Scene(root, 500, 400);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ENTER) {
                    urlPane.button.fire();
                    ke.consume(); // <-- stops passing the event to next node
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(400);
        primaryStage.setTitle("Java Crawler");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}