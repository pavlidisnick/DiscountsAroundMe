package com.tl.discountsaroundme.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tl.discountsaroundme.Fragments.DiscountsTab;
import com.tl.discountsaroundme.Fragments.UserTab;
import com.tl.discountsaroundme.Fragments.MapTab;
import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.UiControllers.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;

    private Button btMap;
    private Button btSearch;
    private EditText etItemSearch;
    private ArrayList<String> listDiscountItems = new ArrayList<>();
    private ArrayList<String> listSearchItems = new ArrayList<>();
    private ArrayAdapter<String> adapter,searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseReference mDbRefDiscounts = FirebaseDatabase.getInstance().getReference("/shops/1/items");
        btMap = findViewById(R.id.btMap);
        btSearch = findViewById(R.id.btSearch);
        TextView tvWelcome =  findViewById(R.id.tvWelcomeMessage);
        TextView tvTopDiscounts =  findViewById(R.id.tvTopDiscounts);
        etItemSearch =  findViewById(R.id.etItemSearch);
        ListView lvDiscountsList = findViewById(R.id.lvDiscounts);
        ListView lvSearchList = findViewById(R.id.lvItemsSearched);
        btMap.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        //Get the username from the login activity And set it on the welcome Page
        Bundle extras = getIntent().getExtras();
            if (extras != null){
                String value = extras.getString("Username");
                tvWelcome.setText("Welcome "+ value);
            }
        //Create the list adapters  and set it on the list views
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listDiscountItems);
        lvDiscountsList.setAdapter(adapter);
        searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listSearchItems);
        lvSearchList.setAdapter(searchAdapter);

        //Todays Top Discounts
        mDbRefDiscounts.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String price = dataSnapshot.child("price").getValue(String.class);
                    String discount = dataSnapshot.child("discount").getValue(String.class);
                listDiscountItems.add(name + "   "+ price + "  " + discount);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(2);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        if (id == R.id.menu_discounts)
                            mViewPager.setCurrentItem(0);
                        else if (id == R.id.menu_map)
                            mViewPager.setCurrentItem(1);
                        else if (id == R.id.menu_user_options)
                            mViewPager.setCurrentItem(2);
                        return true;
                    }
                });
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment discount = new DiscountsTab();
        private Fragment map = new MapTab();
        private Fragment main = new UserTab();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return discount;
                case 1:
                    return map;
                case 2:
                    return main;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DISCOUNTS";
                case 1:
                    return "MAP";
                case 2:
                    return "MAIN";
            }
            return null;
        }
    }
}
