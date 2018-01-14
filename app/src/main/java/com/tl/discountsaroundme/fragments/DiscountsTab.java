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

import com.arlib.floatingsearchview.FloatingSearchView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tl.discountsaroundme.BuildConfig;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.activities.AddDiscountsActivity;
import com.tl.discountsaroundme.activities.FeedbackActivity;
import com.tl.discountsaroundme.activities.MainActivity;
import com.tl.discountsaroundme.activities.MyDiscountsActivity;
import com.tl.discountsaroundme.activities.ReportBugActivity;
import com.tl.discountsaroundme.activities.ShoppingCartActivity;
import com.tl.discountsaroundme.discounts.AddCategoryToLayout;
import com.tl.discountsaroundme.discounts.FetchCategories;
import com.tl.discountsaroundme.discounts.Search;
import com.tl.discountsaroundme.firebase_data.DiscountsManager;
import com.tl.discountsaroundme.firebase_data.SearchHistory;
import com.tl.discountsaroundme.ui_controllers.GlideApp;
import com.tl.discountsaroundme.ui_controllers.ItemSpaceDecoration;
import com.tl.discountsaroundme.ui_controllers.ItemViewAdapter;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DiscountsTab extends Fragment {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static int discountValue = 30;
    DatabaseReference myRef;
    String uri;
    private DrawerLayout mDrawerLayout;
    private DiscountsManager discountsManager = new DiscountsManager();
    private Search search;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_discounts, container, false);

        RecyclerView mRecyclerView = rootView.findViewById(R.id.item_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        ItemViewAdapter adapter = new ItemViewAdapter(getActivity(), discountsManager.getShowingItems());
        discountsManager.setAdapter(adapter);
        mRecyclerView.setAdapter(adapter);

        //ItemDecoration for spacing between items
        decorate(mRecyclerView);

        final SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.pink,
                R.color.orange,
                R.color.colorPrice);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                discountsManager.getTopDiscounts(discountValue);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        LinearLayout linearLayout = rootView.findViewById(R.id.linear_layout);
        AddCategoryToLayout addCategoryToLayout = new AddCategoryToLayout(linearLayout, getActivity(), discountsManager);
        new FetchCategories(addCategoryToLayout);

        discountsManager.showTopDiscountsAndNotify(FirebaseDatabase.getInstance(), discountValue, MainActivity.USER_ID);

        // FloatingSearchView actions
        FloatingSearchView mSearchView = rootView.findViewById(R.id.floating_search_view);
        setSearchBar(mSearchView);

        setDrawer(mSearchView);

        return rootView;
    }

    private void setSearchBar(FloatingSearchView mSearchView) {
        SearchHistory searchHistory = new SearchHistory(MainActivity.USER_ID);

        search = new Search(mSearchView, searchHistory, discountsManager);
        mSearchView.setOnQueryChangeListener(search);
        mSearchView.setOnBindSuggestionCallback(search);
        mSearchView.setOnFocusChangeListener(search);
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                startVoiceRecognitionActivity();
            }
        });
    }

    private void decorate(RecyclerView recyclerView) {
        ItemSpaceDecoration decoration = new ItemSpaceDecoration(16);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(true);
    }

    public void setDrawer(FloatingSearchView mSearchView) {
        mDrawerLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.drawer_layout);

        mSearchView.attachNavigationDrawerToMenuButton(mDrawerLayout);

        NavigationView nav = mDrawerLayout.findViewById(R.id.nav_view);

        //Add option "Add Discount" to drawer if customer type is owner
        if (MainActivity.USER_TYPE.equals("user")) {
            Menu menu = nav.getMenu();
            MenuItem target = menu.findItem(R.id.nav_insert_item);
            MenuItem target2 = menu.findItem(R.id.nav_my_discounts);
            target.setVisible(false);
            target2.setVisible(false);
        }

        String versionName = "v" + BuildConfig.VERSION_NAME;
        Menu menu = nav.getMenu();
        MenuItem versionMenuItem = menu.findItem(R.id.version);
        versionMenuItem.setTitle(versionName);

        nav.bringToFront();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_insert_item:
                        Intent addDiscount = new Intent(getContext(), AddDiscountsActivity.class);
                        startActivity(addDiscount);
                        break;
                    case R.id.nav_my_discounts:
                        Intent MyDiscounts = new Intent(getContext(), MyDiscountsActivity.class);
                        startActivity(MyDiscounts);
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        getActivity().finish();
                        break;
                    case R.id.nav_profile:
                        Intent userProfileActivity = new Intent(getActivity(), com.tl.discountsaroundme.activities.UserProfileActivity.class);
                        startActivity(userProfileActivity);
                        break;
                    case R.id.nav_shopping_cart:
                        Intent shoppingCartActivity = new Intent(getActivity(), ShoppingCartActivity.class);
                        startActivity(shoppingCartActivity);
                        break;
                    case R.id.report_bug:
                        Intent reportBugActivity = new Intent(getActivity(), ReportBugActivity.class);
                        startActivity(reportBugActivity);
                        break;
                    case R.id.feedback:
                        Intent feedbackActivity = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(feedbackActivity);
                        break;
                }

                DrawerLayout drawer = mDrawerLayout.findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        drawerInformation();
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
            search.voiceSearch(matches);
        }
    }

    @SuppressWarnings("deprecation")
    public void drawerInformation() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                if (user != null) {
                    final String email = user.getEmail();

                    TextView ut = mDrawerLayout.findViewById(R.id.drawerUserType);
                    ut.setText(MainActivity.USER_TYPE);

                    TextView userEmail = mDrawerLayout.findViewById(R.id.drawerEmail);
                    userEmail.setText(email);

                    ImageView imageDrawer = mDrawerLayout.findViewById(R.id.imageViewDrawerUser);

                    myRef = FirebaseDatabase.getInstance().getReference("users");

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            uri = (String) dataSnapshot.child(user.getUid()).child("image").getValue();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    GlideApp.with(getContext())
                            .load(uri)
                            .circleCrop()
                            .into(imageDrawer);
                }
                super.onDrawerOpened(drawerView);
            }
        });
    }
}
