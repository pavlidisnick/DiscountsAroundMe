package com.tl.discountsaroundme.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.tl.discountsaroundme.Activities.MainActivity;
import com.tl.discountsaroundme.AddCategoryToLayout;
import com.tl.discountsaroundme.Discounts.SearchSuggest;
import com.tl.discountsaroundme.Discounts.SuggestListMaker;
import com.tl.discountsaroundme.FirebaseData.DiscountsManager;
import com.tl.discountsaroundme.FirebaseData.SearchHistory;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UiControllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;
import com.tl.discountsaroundme.CategoryListener;

import java.util.List;
import java.util.Queue;

public class DiscountsTab extends Fragment {
    public static int discountValue = 30;
    FloatingSearchView mSearchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid_layout, container, false);

        final DiscountsManager discountsManager = new DiscountsManager();

        final RecyclerView mRecyclerView = rootView.findViewById(R.id.item_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        final ItemViewAdapter adapter = new ItemViewAdapter(getActivity(), discountsManager.getDiscountItems());
        discountsManager.setAdapter(adapter);
        mRecyclerView.setAdapter(adapter);
        //ItemDecoration for spacing between items
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setHasFixedSize(true);
        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                discountsManager.clearTopDiscounts();
                discountsManager.getTopDiscounts();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayout linearLayout = rootView.findViewById(R.id.linear_layout);
        AddCategoryToLayout addCategoryToLayout = new AddCategoryToLayout(linearLayout, getActivity());
        new CategoryListener(addCategoryToLayout, discountsManager);

        discountsManager.getTopDiscounts();

        mSearchView = rootView.findViewById(R.id.floating_search_view);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                SuggestListMaker suggestListMaker = new SuggestListMaker();
                List<SearchSuggest> searchSuggestList;
                searchSuggestList = suggestListMaker.convertStringsToSuggestions(discountsManager.getSuggestionsDiscounts(), newQuery);

                //pass them on to the search view
                mSearchView.swapSuggestions(searchSuggestList);
            }
        });

        final SearchHistory searchHistory = new SearchHistory(MainActivity.USER_ID);

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, final TextView textView, final SearchSuggestion item, int itemPosition) {
                final String suggestion = item.getBody();
                if (searchHistory.getHistoryList().contains(suggestion))
                    leftIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_history));

                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchHistory.newSearchAdd(suggestion);
                        discountsManager.getDiscountsByName(suggestion);
                        mSearchView.setSearchBarTitle(suggestion);
                        mSearchView.clearFocus();
                    }
                });
            }

        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                SuggestListMaker suggestListMaker = new SuggestListMaker();
                List<SearchSuggest> searchSuggestHistory;
                searchSuggestHistory = suggestListMaker.convertStringsToSuggestions(searchHistory.getHistoryList());

                mSearchView.swapSuggestions(searchSuggestHistory);
            }

            @Override
            public void onFocusCleared() {
            }
        });

        return rootView;
    }
}
