import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class Dictionary {

    @JsonProperty("words")
    private HashSet<String> listOfWords;

    @JsonProperty("language")
    private String language;

    public Dictionary(HashSet<String> listOfWords, String language) {
        this.listOfWords = listOfWords;
        this.language = language;
    }

    public Dictionary() {}

    public static boolean isEmptyObj(Dictionary list2) {
        return list2.getListOfWords().isEmpty() || list2.getLanguage().trim().length() == 0;
    }


    public HashSet<String> getListOfWords() {
        return listOfWords;
    }

    public String getLanguage() {
        return language;
    }
}
