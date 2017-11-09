package com.tl.discountsaroundme.Discounts;


import java.util.ArrayList;
import java.util.List;

public class SuggestListMaker {

    public List<SearchSuggest> convertStringsToSuggestions(ArrayList<String> searchSuggests, String searchQuery) {
        List<SearchSuggest> searchSuggestList = new ArrayList<>();
        for (String searchSuggest: searchSuggests) {
            if (searchSuggest.toUpperCase().contains(searchQuery.toUpperCase()))
                searchSuggestList.add(new SearchSuggest(searchSuggest));
        }
        return searchSuggestList;
    }
}
