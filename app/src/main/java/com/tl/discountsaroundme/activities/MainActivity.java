package com.tl.discountsaroundme.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tl.discountsaroundme.R;
import com.tl.discountsaroundme.fragments.DiscountsTab;
import com.tl.discountsaroundme.fragments.MapTab;
import com.tl.discountsaroundme.fragments.UserTab;
import com.tl.discountsaroundme.ui_controllers.ZoomOutPageTransformer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
                        switch (id) {
                            case R.id.menu_discounts:
                                mViewPager.setCurrentItem(0);
                                break;
                            case R.id.menu_map:
                                mViewPager.setCurrentItem(1);
                                break;
                            case R.id.menu_user_options:
                                mViewPager.setCurrentItem(2);
                                break;
                        }
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
            @SuppressLint("InflateParams")
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_exit_app, null);
            Button exitButton = (Button) linearLayout.getChildAt(2);
            exitButton.setOnClickListener(this);

            new AlertDialog.Builder(this).setView(linearLayout).create().show();
        }
    }

    @Override
    public void onClick(View v) {
        this.finishAffinity();
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
