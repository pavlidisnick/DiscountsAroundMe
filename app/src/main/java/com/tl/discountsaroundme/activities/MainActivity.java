package com.tl.discountsaroundme.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.fragments.DiscountsTab;
import com.tl.discountsaroundme.fragments.MapTab;
import com.tl.discountsaroundme.fragments.UserTab;
import com.tl.discountsaroundme.ui_controllers.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity {
    public static String USER_TYPE;
    public static String USER_ID;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        USER_TYPE = getIntent().getStringExtra("USER_TYPE");
        USER_ID = getIntent().getStringExtra("USER_ID");

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

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawerLayout = findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment discount = new DiscountsTab();
        private Fragment map = new MapTab();
        private Fragment userTab = new UserTab();

        private SectionsPagerAdapter(FragmentManager fm) {
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
                    return userTab;
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
                    return "USER TAB";
            }
            return null;
        }
    }
}