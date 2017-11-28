package com.tl.discountsaroundme.discounts;

import java.util.ArrayList;
import java.util.List;

public class SuggestionMaker {

    /**
     * @param searchItems Array of items you want to search
     * @param searchQuery a String query to search from the list of items
     * @return List of suggestions based on the query passed as an arg
     */
    public List<String> getSuggestionsWithQuery(List<String> searchItems, String searchQuery) {
        List<String> suggestStrings = new ArrayList<>();

        for (String searchItem : searchItems) {
            if (searchItem.toUpperCase().contains(searchQuery.toUpperCase()))
                suggestStrings.add(searchItem);
        }

        return suggestStrings;
    }

    /**
     * Convert a list of strings to a list of suggestions
     *
     * @param suggestionStrings list of strings
     * @return Suggestion list
     */
    public List<SearchSuggest> stringsToSuggestions(List<String> suggestionStrings) {
        List<SearchSuggest> searchSuggestList = new ArrayList<>();

        for (String suggestionString : suggestionStrings)
            searchSuggestList.add(new SearchSuggest(suggestionString));

        return searchSuggestList;
    }
}
