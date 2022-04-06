import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class WordToSearchTest {

    DictionaryList testDict;
    Dictionary testDictionary;
    Dictionary testDictionary2;
    HashSet<String> testListOfWords;
    String testLang;
    HashSet<String> testListOfWords2;
    String testLang2;
    List<Dictionary> testListOfGlossaries;

    @Before
    public void setUp() {
        testListOfWords = new HashSet<>(Arrays.asList("ability", "abandon", "absolutely", "accident", "accuse", "program"));
        testLang = "English";
        testListOfWords2 = new HashSet<>(Arrays.asList("pies", "kot", "piłka", "okno", "dom", "program"));
        testLang2 = "Polish";
        testDictionary = new Dictionary(testListOfWords, testLang);
        testDictionary2 = new Dictionary(testListOfWords2, testLang2);
        testListOfGlossaries = new ArrayList<>();
        testListOfGlossaries.add(testDictionary);
        testListOfGlossaries.add(testDictionary2);
        testDict = new DictionaryList(testListOfGlossaries);
    }

    public List<String> findLanguage(String word) {
        List<String> result = new ArrayList<>();
        AtomicBoolean checkIfWordIsInDatabase = new AtomicBoolean(false);
        testDict.getDictionaryList().stream()
                .forEach(elem -> elem.getListOfWords().stream()
                    .forEach(w -> {
                        if (w.equals(word)) {
                            checkIfWordIsInDatabase.set(true);
                            result.add(elem.getLanguage());
                        }
                }));
        if (!checkIfWordIsInDatabase.get()) {
            result.add("Tego słowa nie ma w bazie");
        }
        return result;
    }

    @Test //word is in database in 1 language
    public void shouldWriteLanguage() {
        List<String> listOfLang = findLanguage("ability");
        assertEquals("English", listOfLang.get(0));
    }

    @Test //word is in database in 2 languages
    public void shouldWriteTwoLanguages() {
        List<String> listOfLang2 = findLanguage("program");
        assertTrue(listOfLang2.get(0).equals("English") && listOfLang2.get(1).equals("Polish"));
    }

    @Test // word is not in database
    public void shouldSayThatWordIsNotInDatabase() {
        List<String> listOfLang3 = findLanguage("angry");
        assertEquals("Tego słowa nie ma w bazie", listOfLang3.get(0));
    }
}

