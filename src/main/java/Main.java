import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) throws IOException, DocumentException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Chcesz dodać własne słowa i język? Jeśli tak, wpisz y, w przeciwnym wypadku wpisz " +
                "n i nacisnij Enter");
        String yesOrNo = scan.next();
        scan.nextLine();
        String newWords = "";
        String newLang = "";
        if (yesOrNo.equals("y")) {
            System.out.println("Wpisz w jednej linii nowe słowa oddzielone spacją");
            newWords = scan.nextLine();
            System.out.println("Wpisz język, z którego podane wyżej słowa pochodzą: ");
            newLang = scan.nextLine();
        }
        String[] newWordsArray = newWords.split(" ");
        HashSet<String> newWordsSet = new HashSet<>();

        Collections.addAll(newWordsSet, newWordsArray);
        ObjectMapper jsonMapper = new ObjectMapper();
        DictionaryList dictionaryList = jsonMapper.readValue(new File("src/main/resources/jsonFile.json"), DictionaryList.class);
        //String prettyJson = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dictionaryList);

        Dictionary newDictionary = new Dictionary(newWordsSet, newLang);
        if (!Dictionary.isEmptyObj(newDictionary)) {
            dictionaryList.addDictToList(newDictionary);
        }
        //System.out.println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dictionaryList));
        jsonMapper.writeValue(new File("src/main/resources/jsonFile.json"), dictionaryList);

        Map<String, List<String>> resultMap = new HashMap<>();
        HashSet<String> setOfWordsToSearch = new HashSet<>();
        int counter = 0;

        while (true) {

            AtomicBoolean checkIfWordIsInDatabase = new AtomicBoolean(false);
            System.out.println("Wpisz słowo lub naciśnij / i Enter żeby wyjść");
            String wordToSearch = scan.next();
            if (wordToSearch.equals("/")) {
                System.out.println("Statystyki zostały zapisane w pliku pdf");
                break;
            }
            counter++;
            setOfWordsToSearch.add(wordToSearch);

            dictionaryList.getDictionaryList().stream()
                    .forEach(elem -> elem.getListOfWords().stream()
                            .forEach(word -> {
                                if (word.equals(wordToSearch)) {
                                    System.out.println(elem.getLanguage());
                                    List<String> languages = resultMap.get(wordToSearch);
                                    checkIfWordIsInDatabase.set(true);
                                    if (languages != null) {
                                        if (!setOfWordsToSearch.contains(word) || !languages.contains(elem.getLanguage())) {
                                            languages.add(elem.getLanguage());
                                        }
                                    } else {
                                        List<String> newLanguages = new ArrayList<>();
                                        newLanguages.add(elem.getLanguage());
                                        resultMap.put(word, newLanguages);
                                    }
                                }
                            }));
            if (!checkIfWordIsInDatabase.get()) {
                System.out.println("Tego słowa nie ma w bazie");
            }
        }

        ////////////// Create PDF File

        Document pdfDocument = new Document(PageSize.A4, 20, 20, 20, 20);

        PdfWriter.getInstance(pdfDocument, new FileOutputStream("biblioteka.pdf"));
        pdfDocument.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);

        Paragraph p3 = new Paragraph("Podano lacznie slow: " + counter, font);
        pdfDocument.add(p3);
        Paragraph p2 = new Paragraph("Znaleziono slow w bazie: " + resultMap.entrySet().size(), font);
        pdfDocument.add(p2);
        pdfDocument.add(Chunk.NEWLINE);
        // Create a set that contains the same elements as the hashmap in order to iterate over them
        for (Map.Entry<String, List<String>> set : resultMap.entrySet()) {
            Paragraph p = new Paragraph(set.getKey() + ": " + set.getValue(), font);
            pdfDocument.add(p);
        }

        pdfDocument.close();

    }

}

