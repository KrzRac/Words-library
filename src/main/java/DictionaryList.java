import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class DictionaryList {

    @JsonProperty("dictionary")
    private List<Dictionary> dictionaryList;


    public DictionaryList(List<Dictionary> dictionaryList) {
        this.dictionaryList = dictionaryList;
    }

    public DictionaryList() {}

    public List<Dictionary> getDictionaryList() {
        return dictionaryList;
    }


    public void addDictToList(Dictionary listOfWords) {
        List<Dictionary> listOfDictionaries = this.dictionaryList.stream()
                .filter(word -> word.getLanguage().equals(listOfWords.getLanguage()))
                .collect(Collectors.toList());
        if (listOfDictionaries.isEmpty()) {
            dictionaryList.add(listOfWords);
        } else {
            listOfDictionaries.get(0).getListOfWords().addAll(listOfWords.getListOfWords());
        }

    }
}
