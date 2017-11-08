package com.tl.discountsaroundme.Discounts;

import android.os.Parcel;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;


public class SearchSuggest implements SearchSuggestion{
    private static Creator CREATOR;
    private String searchBody;

    public SearchSuggest(String searchBody) {
        this.searchBody = searchBody;
    }

    @Override
    public String getBody() {
        return searchBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
