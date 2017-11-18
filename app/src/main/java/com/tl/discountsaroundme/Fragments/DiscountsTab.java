package com.tl.discountsaroundme.Fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.auth.FirebaseAuth;
import com.tl.discountsaroundme.AddCategoryToLayout;
import com.tl.discountsaroundme.CategoryListener;
import com.tl.discountsaroundme.Discounts.SearchSuggest;
import com.tl.discountsaroundme.Discounts.SuggestListMaker;
import com.tl.discountsaroundme.FirebaseData.DiscountsManager;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UiControllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DiscountsTab extends Fragment {
    public static int discountValue = 30;
    FloatingSearchView mSearchView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    DiscountsManager discountsManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.grid_layout, container, false);

        discountsManager = new DiscountsManager();

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

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, final TextView textView, final SearchSuggestion item, int itemPosition) {
                suggestionView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        discountsManager.getDiscountsByName(item.getBody());
                        mSearchView.setSearchBarTitle(item.getBody());
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
                // searchSuggestHistory = suggestListMaker.convertStringArrayToSuggest(discountsManager.getSuggestionsDiscounts(), newQuery);

                // mSearchView.swapSuggestions(mLastSuggestList);
            }

            @Override
            public void onFocusCleared() {
                mSearchView.setSearchBarTitle("");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                startVoiceRecognitionActivity();
            }
        });

        return rootView;
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            discountsManager.getDiscountsByName(matches.get(0));
            mSearchView.setSearchText(matches.get(0));
        }
    }
}
