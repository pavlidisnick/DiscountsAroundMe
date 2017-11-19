package com.tl.discountsaroundme.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.tl.discountsaroundme.Activities.AddDiscounts;
import com.tl.discountsaroundme.Activities.Login;
import com.tl.discountsaroundme.AddCategoryToLayout;
import com.tl.discountsaroundme.Discounts.SearchSuggest;
import com.tl.discountsaroundme.Discounts.SuggestListMaker;
import com.tl.discountsaroundme.FirebaseData.DiscountsManager;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UiControllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.UiControllers.ItemViewAdapter;
import com.tl.discountsaroundme.CategoryListener;

import java.util.List;

public class DiscountsTab extends Fragment {
    public static int discountValue = 30;
    FloatingSearchView mSearchView;
    DrawerLayout mDrawerLayout;

    public static String userType;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        setDrawer();


        System.out.println("discounts" +userType);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //TextView text = (TextView) view.findViewById(R.id.textViewUserType);
        //text.setText("user");
        //text.getText();
        //System.out.println(text.getText()+"get text");

        View v = View.inflate(getContext(),R.layout.nav_header_main,null);
        View innerView = v.findViewById(R.id.textViewUserType);
        TextView txt = innerView.findViewById(R.id.textViewUserType);
        txt.setText("user");


        super.onViewCreated(view, savedInstanceState);
    }

    public void setDrawer(){
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        //Add option "Add Discount" to drawer if customer type is owner
        if (userType=="owner"){ //change it later to "user"
            NavigationView navigationView = mDrawerLayout.findViewById(R.id.nav_view);
            Menu menu =navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_insert_item);
            target.setVisible(false);
        }


        NavigationView nav = mDrawerLayout.findViewById(R.id.nav_view);
        nav.bringToFront();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_insert_item) {
                    Intent addDiscount = new Intent(getContext(), AddDiscounts.class);
                    startActivity(addDiscount);
                } else if (id == R.id.temp) {
                    Toast.makeText(getContext(), "temp", Toast.LENGTH_LONG).show();

                } else if (id == R.id.nav_info) {
                    Toast.makeText(getContext(), "info", Toast.LENGTH_LONG).show();
                } else if (id == R.id.nav_logout) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent login = new Intent(getContext(), Login.class);
                    login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login);
                }else if (id == R.id.nav_profile){
                    Toast.makeText(getContext(), "profile", Toast.LENGTH_LONG).show();
                }


                DrawerLayout drawer = (DrawerLayout) mDrawerLayout.findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
}
