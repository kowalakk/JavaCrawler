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

        JLabel label = new JLabel("url:");
        label.setFont(myFont);
        label.setBounds(30, 25, 40, 40);
        this.add(label);

        JTextField urlField = new JTextField();
        urlField.setFont(myFont);
        urlField.setBounds(70, 25, 590, 40);
        this.add(urlField);

        JButton button = new JButton("Crawl");
        button.setFont(myFont);
        button.setBounds(670, 25, 100, 39);
        button.addActionListener(e -> {
            try {
                textArea.setText(readWebPage(urlField.getText()));
            } catch (IOException ex) {
                textArea.setText("Nie można otworzyć strony o podanym aresie");
                throw new RuntimeException(ex);
            }
        });
        this.add(button);
        this.getRootPane().setDefaultButton(button);

        textArea.setFont(myFont);
        textArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBounds(20, 75, 750, 475);
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
