package com.tl.discountsaroundme.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.auth.FirebaseAuth;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.AddDiscountsActivity;
import com.tl.discountsaroundme.activities.LoginActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.discounts.AddCategoryToLayout;
import com.tl.discountsaroundme.discounts.CategoryListener;
import com.tl.discountsaroundme.discounts.SearchSuggest;
import com.tl.discountsaroundme.discounts.SuggestListMaker;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.SearchHistory;
import com.tl.discountsaroundme.ui_controllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DiscountsTab extends Fragment {
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static int discountValue = 30;
    FloatingSearchView mSearchView;
    DrawerLayout mDrawerLayout;
    DiscountsManager discountsManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        setDrawer();

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

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                startVoiceRecognitionActivity();
            }
        });

        return rootView;
    }

    public void setDrawer() {
        mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);

        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        NavigationView nav = mDrawerLayout.findViewById(R.id.nav_view);

        //Add option "Add Discount" to drawer if customer type is owner
        if (MainActivity.USER_TYPE.equals("user")) {
            Menu menu = nav.getMenu();
            MenuItem target = menu.findItem(R.id.nav_insert_item);
            target.setVisible(false);
        }

        nav.bringToFront();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_insert_item) {
                    Intent addDiscount = new Intent(getContext(), AddDiscountsActivity.class);
                    startActivity(addDiscount);
                } else if (id == R.id.temp) {
                    Toast.makeText(getContext(), "temp", Toast.LENGTH_LONG).show();

                } else if (id == R.id.nav_info) {
                    Toast.makeText(getContext(), "info", Toast.LENGTH_LONG).show();
                } else if (id == R.id.nav_logout) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent login = new Intent(getContext(), LoginActivity.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                } else if (id == R.id.nav_profile) {
                    Intent UserProfileActivity = new Intent(getActivity(), com.tl.discountsaroundme.activities.UserProfileActivity.class);
                    startActivity(UserProfileActivity);
                }

                DrawerLayout drawer = mDrawerLayout.findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
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