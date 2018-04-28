package com.example.anurag.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerticalViewPager verticalViewPager = findViewById(R.id.pager);
        verticalViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private Fragment mCurrentFragment;

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }
        //...
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return FragmentViewPager.newInstance("Title Start", R.drawable.ic_launcher_background);
                case 1:
                    return FragmentViewPager.newInstance("Rock", R.drawable.rock);
                case 2:
                    return FragmentViewPager.newInstance("Paper", R.drawable.paper);
                case 3:
                    return FragmentViewPager.newInstance("Scissors", R.drawable.scissors);
                case 4:
                    return FragmentViewPager.newInstance("Title End", R.drawable.ic_launcher_background);
                default:
                    return FragmentViewPager.newInstance("Default Title", R.drawable.rock);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
