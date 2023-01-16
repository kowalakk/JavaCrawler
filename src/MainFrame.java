import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainFrame extends JFrame {
    static Font myFont = new Font("Arial", Font.BOLD, 16);
    JTextArea textArea = new JTextArea();

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
        urlField.setBounds(120, 25, 530, 40);
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
        saveToFileButton.setBounds(120, 75, 150, 39);
        saveToFileButton.addActionListener(e -> {
            try {
                JFileChooser saveToFileChooser = new JFileChooser();
                saveToFileChooser.showSaveDialog(null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        this.add(saveToFileButton);

        JLabel filterByLabel = new JLabel("Filter by: ");
        filterByLabel.setFont(myFont);
        filterByLabel.setBounds(30, 125, 100, 40);
        this.add(filterByLabel);


        JComboBox filterComboBox = new JComboBox(new String[] {"Class name", "Id", "Type"});
        filterComboBox.setBounds(120, 125, 100, 40);
        this.add(filterComboBox);



        textArea.setFont(myFont);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(20, 175, 750, 475);
//        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(scroll);

        this.setVisible(true);
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
}
