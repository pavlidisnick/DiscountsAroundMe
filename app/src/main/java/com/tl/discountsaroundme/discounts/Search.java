package com.tl.discountsaroundme.discounts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.SearchHistory;

import java.util.ArrayList;
import java.util.List;

public class Search implements FloatingSearchView.OnQueryChangeListener,
        SearchSuggestionsAdapter.OnBindSuggestionCallback, FloatingSearchView.OnFocusChangeListener {
    private DiscountsManager discountsManager;
    private FloatingSearchView searchView;
    private SearchHistory searchHistory;

    public Search(FloatingSearchView searchView, SearchHistory searchHistory, DiscountsManager discountsManager) {
        this.discountsManager = discountsManager;
        this.searchView = searchView;
        this.searchHistory = searchHistory;
    }

    /**
     * @param matches String array filled with voice results
     */
    public void voiceSearch(ArrayList<String> matches) {
        discountsManager.getDiscountsByName(matches.get(0));
        searchView.setSearchText(matches.get(0));
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        SuggestionMaker suggestionMaker = new SuggestionMaker();
        List<String> suggestions = suggestionMaker.getSuggestionsWithQuery(discountsManager.getDiscountNames(), newQuery);
        searchView.swapSuggestions(suggestionMaker.stringsToSuggestions(suggestions));
    }

    @Override
    public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
        final String suggestion = item.getBody();

        if (searchHistory.getHistoryList().contains(suggestion))
            leftIcon.setImageDrawable(suggestionView.getContext().getResources().getDrawable(R.drawable.ic_history));

        suggestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchHistory.newSearchAdd(suggestion);
                discountsManager.getDiscountsByName(suggestion);
                searchView.setSearchBarTitle(suggestion);
                searchView.clearFocus();
            }
        });
    }

    @Override
    public void onFocus() {
        SuggestionMaker suggestionMaker = new SuggestionMaker();
        List<SearchSuggest> searchSuggestHistory;
        searchSuggestHistory = suggestionMaker.stringsToSuggestions(searchHistory.getHistoryList());

        searchView.swapSuggestions(searchSuggestHistory);
    }

    @Override
    public void onFocusCleared() {
    }
}
