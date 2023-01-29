import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class MainFrame extends JFrame {
    static Font myFont = new Font("Arial", Font.BOLD, 16);
    JTextArea textArea = new JTextArea();
    File destinationFile;
    String settingsFilePath = "src/settings.txt";

    MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setTitle("Web Crawler");
        this.setLayout(null);

        JLabel urlLabel = new JLabel("url:");
        urlLabel.setFont(myFont);
        urlLabel.setBounds(30, 25, 100, 40);
        this.add(urlLabel);

        JTextField urlField = new JTextField();
        urlField.setFont(myFont);
        urlField.setBounds(150, 25, 500, 40);
        this.add(urlField);

        JButton crawlButton = new JButton("Crawl");
        crawlButton.setFont(myFont);
        crawlButton.setBounds(670, 25, 100, 39);
        crawlButton.addActionListener(e -> {
            try {
                textArea.setText(readWebPage(urlField.getText()));
            } catch (IOException ex) {
                textArea.setText("Nie można otworzyć strony o podanym adresie");
                throw new RuntimeException(ex);
            }
        });
        this.add(crawlButton);
        this.getRootPane().setDefaultButton(crawlButton);

        JLabel saveToFileLabel = new JLabel("Save to file: ");
        saveToFileLabel.setFont(myFont);
        saveToFileLabel.setBounds(30, 75, 100, 39);
        this.add(saveToFileLabel);

        JButton saveToFileButton = new JButton("Choose file");
        saveToFileButton.setFont(myFont);
        saveToFileButton.setBounds(150, 75, 120, 39);
        saveToFileButton.addActionListener(e -> {
            try {
                JFileChooser saveToFileChooser = new JFileChooser();
                saveToFileChooser.showSaveDialog(null);
                destinationFile = saveToFileChooser.getSelectedFile();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(saveToFileButton);

        JLabel filterByLabel = new JLabel("Filter by: ");
        filterByLabel.setFont(myFont);
        filterByLabel.setBounds(30, 125, 120, 40);
        this.add(filterByLabel);


        JComboBox filterComboBox = new JComboBox(new String[] {"None", "Class name", "Id", "Type"});
        filterComboBox.setBounds(150, 125, 120, 40);
        filterComboBox.setFont(myFont);
        this.add(filterComboBox);

        JCheckBox usePreviousSettingsCheckbox = new JCheckBox();
        usePreviousSettingsCheckbox.setText("Use previous settings");
        usePreviousSettingsCheckbox.setBounds(30, 175, 150, 40);
        usePreviousSettingsCheckbox.addActionListener(e -> {
            try {
                if(usePreviousSettingsCheckbox.isSelected())
                    getSettingsFromFile();
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
        }});
        this.add(usePreviousSettingsCheckbox);

        JButton extractButton = new JButton("Extract");
        extractButton.setFont(myFont);
        extractButton.setBounds(650, 175, 120, 39);
        extractButton.addActionListener(e -> {
            try {
                if (destinationFile == null)
                {
                    JDialog errorDialog = new JDialog();
                    errorDialog.setLocationRelativeTo(this);
                    Container errorDialogContent = new JLabel("There's no file selected!");
                    errorDialog.setContentPane(errorDialogContent);
                    errorDialog.setSize(200, 100);


                    errorDialog.setVisible(true);

                }
                else {
                    String filterString = "";

                    saveSettingsToFile(urlField.getText(), filterComboBox.getSelectedItem().toString(), filterString);
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

    private void saveSettingsToFile(String url, String filter, String filterString) throws IOException  {
        try{
            FileWriter fileWriter = new FileWriter(settingsFilePath);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf("url: %s \n file: %s \n filter: %s \n filter_string: %s", url, this.destinationFile.getAbsolutePath(), filter, filterString);
            printWriter.close();
            fileWriter.close();
        }
        catch (Exception e){
            throw new IOException ();
        }
    }

    private void getSettingsFromFile() {
    }

    public static String readWebPage(String urltext) throws IOException {

        if (!urltext.startsWith("http"))
            urltext = "https://"+urltext;
        var url = new URL(urltext);
        try (var br = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String line;

            var sb = new StringBuilder();

            while ((line = br.readLine()) != null) {

                sb.append(line);
                sb.append(System.lineSeparator());
            }

            return sb.toString();
        }
    }

    public static void extractToFile(){

    }
}
