package application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ExtractionModule {

    static String url;
    static Document document;
    static String selectedFilter = "None";
    static String filterInput;
    static boolean isExtractingText = false;
    static boolean isExtractingAttribute = false;
    static String attributeToExtract = "";

    public static void openUrl() throws IOException {

        if (url.startsWith("http"))
            document = (Document) Jsoup.connect(url).get();
        else if (url.startsWith("www")) {
            url = "https://" + url;
            document = (Document) Jsoup.connect(url).get();
        } else if (url.startsWith("file:///")) {
            File input = new File(url.substring(8));
            document = (Document) Jsoup.parse(input, "UTF-8");
        } else {
            File input = new File(url);
            document = (Document) Jsoup.parse(input, "UTF-8");
        }
    }

    public static String filterDocument() {
        return parseElements(getElements());
    }

    private static Elements getElements() {
        return switch (selectedFilter) {
            case "Class" -> Objects.requireNonNull(document.getElementsByClass(filterInput));
            case "Id" -> Objects.requireNonNull(document.getElementById(filterInput)).children();
            case "Type" -> document.select(filterInput);
            default -> document.children();
        };
    }

    private static String parseElements(Elements elements) {
        if (!isExtractingText && !isExtractingAttribute)
            return elements.toString();

        StringBuilder builder = new StringBuilder();

        for (Element element : elements) {
            if (isExtractingText) {
                builder.append(element.text());
                builder.append('\n');
            }
            if (isExtractingAttribute && !attributeToExtract.isEmpty() && element.attributes().hasDeclaredValueForKey(attributeToExtract)) {
                builder.append(attributeToExtract);
                builder.append(" = ");
                builder.append(element.attr(attributeToExtract));
                builder.append('\n');
            }

            recursiveParse(element.children(), builder);

        }
        return builder.toString();
    }

    private static void recursiveParse(Elements elements, StringBuilder builder) {

        for (Element element : elements) {
            if (isExtractingText) {
                builder.append(element.text());
                builder.append('\n');
            }
            if (isExtractingAttribute && !attributeToExtract.isEmpty() && element.attributes().hasDeclaredValueForKey(attributeToExtract)) {
                builder.append(attributeToExtract);
                builder.append(" = ");
                builder.append(element.attr(attributeToExtract));
                builder.append('\n');
            }
            recursiveParse(element.children(), builder);
        }
    }


}
