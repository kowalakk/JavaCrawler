import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class MainFrame extends JFrame {
    static Font myFont = new Font("Arial", Font.BOLD, 16);
    JTextArea textArea = new JTextArea();
    File destinationFile;
    String settingsFilePath = "src/settings.txt";
    JTextField filterTextBox;
    String[] parseFilters = new String[]{"None", "Class name", "Id", "Type"};
    int selectedFilter;
    JTextField urlField;
    JComboBox<String> filterComboBox;
    JLabel saveToFileFileNameLabel;

    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 800);
        this.setTitle("Web Crawler");
        this.setLayout(null);

        JLabel urlLabel = new JLabel("url:");
        urlLabel.setFont(myFont);
        urlLabel.setBounds(30, 25, 100, 40);
        this.add(urlLabel);

        urlField = new JTextField();
        urlField.setFont(myFont);
        urlField.setBounds(150, 25, 500, 40);
        this.add(urlField);

        JButton crawlButton = new JButton("Crawl");
        crawlButton.setFont(myFont);
        crawlButton.setBounds(670, 25, 100, 39);
        crawlButton.addActionListener(e -> {
            try {
                textArea.setText(parseDocument(readWebPage(urlField.getText())));
            } catch (IOException ex) {
                textArea.setText("Requested address cannot be reached");
                throw new RuntimeException(ex);
            }
        });
        this.add(crawlButton);
        this.getRootPane().setDefaultButton(crawlButton);

        JLabel saveToFileLabel = new JLabel("Save to file: ");
        saveToFileLabel.setFont(myFont);
        saveToFileLabel.setBounds(30, 75, 100, 39);
        this.add(saveToFileLabel);

        saveToFileFileNameLabel = new JLabel("");
        saveToFileFileNameLabel.setBounds(300, 75, 120, 40);
        this.add(saveToFileFileNameLabel);

        JButton saveToFileButton = new JButton("Choose file");
        saveToFileButton.setFont(myFont);
        saveToFileButton.setBounds(150, 75, 120, 39);
        saveToFileButton.addActionListener(e -> {
            try {
                JFileChooser saveToFileChooser = new JFileChooser();
                saveToFileChooser.showSaveDialog(null);
                destinationFile = saveToFileChooser.getSelectedFile();
                saveToFileFileNameLabel.setText(destinationFile.getName());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(saveToFileButton);



        JLabel filterByLabel = new JLabel("Filter by: ");
        filterByLabel.setFont(myFont);
        filterByLabel.setBounds(30, 125, 120, 40);
        this.add(filterByLabel);

        filterTextBox = new JTextField();
        filterTextBox.setBounds(300, 125, 120, 40);
        filterTextBox.setFont(myFont);
        filterTextBox.setVisible(false);
        this.add(filterTextBox);

        filterComboBox = new JComboBox<>(parseFilters);
        filterComboBox.setBounds(150, 125, 120, 40);
        filterComboBox.setFont(myFont);
        filterComboBox.addActionListener(e -> {
            selectedFilter = filterComboBox.getSelectedIndex();
            filterTextBox.setVisible(selectedFilter != 0);
        });
        this.add(filterComboBox);

        JCheckBox usePreviousSettingsCheckbox = new JCheckBox();
        usePreviousSettingsCheckbox.setText("Use previous settings");
        usePreviousSettingsCheckbox.setBounds(30, 175, 150, 40);
        usePreviousSettingsCheckbox.addActionListener(e -> {
            try {
                if (usePreviousSettingsCheckbox.isSelected())
                    getSettingsFromFile();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(usePreviousSettingsCheckbox);

        JButton extractButton = new JButton("Extract");
        extractButton.setFont(myFont);
        extractButton.setBounds(650, 175, 120, 39);
        extractButton.addActionListener(e -> {
            try {
                if (destinationFile == null) {
                    JDialog errorDialog = new JDialog();
                    errorDialog.setLocationRelativeTo(this);
                    Container errorDialogContent = new JLabel("There's no file selected!");
                    errorDialog.setContentPane(errorDialogContent);
                    errorDialog.setSize(200, 100);


                    errorDialog.setVisible(true);

                } else {
                    saveSettingsToFile(urlField.getText(), filterComboBox.getSelectedItem().toString(), filterTextBox.getText());
                    extractToFile();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(extractButton);


        textArea.setFont(myFont);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(20, 225, 750, 475);
//        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll);

        this.setVisible(true);
    }

    private void saveSettingsToFile(String url, String filter, String filterString) throws IOException {
        try {
            FileWriter fileWriter = new FileWriter(settingsFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf("url %s \nfile %s \nfilter %s \nfilter_string %s", url, this.destinationFile.getAbsolutePath(), filter, filterString);
            printWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            throw new IOException();
        }
    }

    private void getSettingsFromFile() throws IOException {
        try{
            var br = new BufferedReader(new FileReader(settingsFilePath));
            String line;
            while ((line = br.readLine()) != null){
                String[] args = line.split(" ");
                switch (args[0]){
                    case "url":
                        urlField.setText(args[1]);
                        break;
                    case "file":
                        String filename = args[1];
                        for(int i = 2; i < args.length; i++){   // odtworzenie nazwy pliku zawierającej spacje
                            filename += " ";
                            filename += args[i];
                        }
                        destinationFile = new File(filename);
                        saveToFileFileNameLabel.setText(destinationFile.getName());
                        break;
                    case "filter":
                        filterComboBox.setSelectedIndex(new ArrayList<String>(List.of(parseFilters)).indexOf(args[1]));
                        break;
                    case "filter_string":
                        if(args.length > 1)
                            filterTextBox.setText(args[1]);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            throw new IOException();
        }
    }

        private String parseDocument (Document document){
            switch (selectedFilter) {
                case 1:
                    return Objects.requireNonNull(document.getElementsByClass(filterTextBox.getText())).toString();
                case 2:
                    return Objects.requireNonNull(document.getElementById(filterTextBox.getText())).toString();
                case 3:  // Type
                    return document.select(filterTextBox.getText()).toString();
                default: // None
                    return document.toString();
            }
        }

        public static Document readWebPage (String urltext) throws IOException {

            if (!urltext.startsWith("http") && !urltext.startsWith("file://"))
                urltext = "https://" + urltext;

            return Jsoup.connect(urltext).get();
        }

        public static void extractToFile () {

        }
    }
