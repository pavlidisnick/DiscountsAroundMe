package com.tl.discountsaroundme.discounts;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SuggestionMakerTest {
    private SuggestionMaker suggestionMaker = new SuggestionMaker();

    @Test
    public void getSuggestionsWithQuery1() throws Exception {
        List<String> stringList = new ArrayList<String>() {{
            add("abc");
            add("cde");
            add("qwerty");
        }};

        List<String> returnedList = suggestionMaker.getSuggestionsWithQuery(stringList, "c");
        assert returnedList.containsAll(new ArrayList<String>() {{
            add("abc");
            add("cde");
        }});
    }

    @Test
    public void getSuggestionsWithQuery2() throws Exception {
        List<String> stringList = new ArrayList<String>() {{
            add("abc");
            add("cde");
            add("qwerty");
        }};

        List<String> returnedList = suggestionMaker.getSuggestionsWithQuery(stringList, "c");
        assert !returnedList.containsAll(new ArrayList<String>() {{
            add("abc");
            add("qwerty");
        }});
    }

    @Test
    public void stringsToSuggestions() throws Exception {
        List<String> stringList = new ArrayList<String>() {{
            add("abc");
            add("cde");
            add("qwerty");
        }};

        assert suggestionMaker.stringsToSuggestions(stringList).get(0).getClass().equals(SearchSuggest.class);
    }

}