package application;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.*;

import static application.ExtractionModule.*;

public class SaveModule {
    public static String settingsFilePath = "src/settings.txt";
    public static File destinationFile;

    public static void saveDataToFile(String data) {
        if (destinationFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There is no selected file");
            alert.showAndWait();
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(destinationFile, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.write(data);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Data saved to file");
            alert.showAndWait();

            printWriter.close();
            fileWriter.close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to save data");
            alert.showAndWait();
        }
    }

    public static void saveSettingsToFile() {
        try {
            FileWriter fileWriter = new FileWriter(settingsFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            String isText = "";
            if (ExtractionModule.isExtractingText) isText = "y";
            String isAttr = "";
            if (ExtractionModule.isExtractingAttribute) isAttr = "y";
            printWriter.printf("url %s \nfile %s \nfilter %s \nfilter_string %s \ntext %s \nattribute %s \nattr_name %s",
                    ExtractionModule.url, destinationFile.getAbsolutePath(), ExtractionModule.selectedFilter, ExtractionModule.filterInput, isText, isAttr, ExtractionModule.attributeToExtract);
            printWriter.close();
            fileWriter.close();
        } catch (Exception ignored) {
        }
    }

    public static void getSettingsFromFile(TextField urlField,
                                           FilterPane filterPane,
                                           ExtractPane extractPane,
                                           FilePane filePane) {
        try {
            var br = new BufferedReader(new FileReader(settingsFilePath));
            String line;
            while ((line = br.readLine()) != null) {
                String[] args = line.split(" ");
                switch (args[0]) {
                    case "url" -> urlField.setText(args[1]);
                    case "file" -> {
                        StringBuilder filename = new StringBuilder(args[1]);
                        for (int i = 2; i < args.length; i++) {   // odtworzenie nazwy pliku zawierającej spacje
                            filename.append(" ");
                            filename.append(args[i]);
                        }
                        destinationFile = new File(filename.toString());
                        filePane.label.setText(destinationFile.getName());
                        filePane.saveButton.setDisable(false);
                    }
                    case "filter" -> {
                        ExtractionModule.selectedFilter = args[1];
                        filterPane.comboBox.setValue(args[1]);
                    }
                    case "filter_string" -> {
                        if (args.length > 1) {
                            ExtractionModule.filterInput = args[1];
                            filterPane.textField.setText(args[1]);
                        }
                    }
                    case "text" -> {
                        if (args.length > 1) {
                            ExtractionModule.isExtractingText = true;
                            extractPane.textCheckBox.setSelected(true);
                        }
                    }
                    case "attribute" -> {
                        if (args.length > 1) {
                            ExtractionModule.isExtractingAttribute = true;
                            extractPane.attributeCheckBox.setSelected(true);
                        }
                    }
                    case "attr_name" -> {
                        if (args.length > 1) {
                            ExtractionModule.attributeToExtract = args[1];
                            extractPane.textField.setDisable(false);
                            extractPane.textField.setText(args[1]);
                        }
                    }
                    default -> {
                    }
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to read settings");
            alert.showAndWait();
        }
    }
}
