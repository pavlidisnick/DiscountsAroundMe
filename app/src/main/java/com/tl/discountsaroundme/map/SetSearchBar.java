package com.tl.discountsaroundme.map;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.tl.discountsaroundme.discounts.SuggestionMaker;
import com.tl.discountsaroundme.entities.Store;
import com.tl.discountsaroundme.firebase_data.StoreManager;

import java.util.List;

public class SetSearchBar implements FloatingSearchView.OnQueryChangeListener, SearchSuggestionsAdapter.OnBindSuggestionCallback {
    private FloatingSearchView mSearchView;
    private StoreManager storeManager;
    private MarkerHelper markerHelper;

    public SetSearchBar(FloatingSearchView mSearchView, StoreManager storeManager, MarkerHelper markerHelper) {
        this.mSearchView = mSearchView;
        this.storeManager = storeManager;
        this.markerHelper = markerHelper;
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        SuggestionMaker suggestionMaker = new SuggestionMaker();
        List<String> suggestions = suggestionMaker.getSuggestionsWithQuery(storeManager.getStoreStrings(), newQuery);

        mSearchView.swapSuggestions(suggestionMaker.stringsToSuggestions(suggestions));
    }

    @Override
    public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, final SearchSuggestion item, int itemPosition) {
        suggestionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Store store = storeManager.getStoreFromName(item.getBody());
                if (store != null) {
                    markerHelper.addStoreMarker(store);
                    markerHelper.animateCamera(store);
                }
                mSearchView.setSearchBarTitle(item.getBody());
                mSearchView.clearFocus();
            }
        });
    }
}
