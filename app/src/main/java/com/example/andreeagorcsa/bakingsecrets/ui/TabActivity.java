package com.example.andreeagorcsa.bakingsecrets.ui;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.andreeagorcsa.bakingsecrets.R;
import com.example.andreeagorcsa.bakingsecrets.fragments.IngredientFragment;
import com.example.andreeagorcsa.bakingsecrets.fragments.StepFragment;
import com.example.andreeagorcsa.bakingsecrets.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabActivity extends AppCompatActivity {

    public static final String LOG_TAG = TabActivity.class.getSimpleName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        ButterKnife.bind(this);

        // gets the parcel from the intent, sent in MainActivity
        Recipe recipe = getIntent().getParcelableExtra(MainActivity.RECIPE_KEY);

        // gets the recipeName out of the recipe object
        String recipeName = recipe.getRecipeName();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // the toolbar displays the name of the previously clicked recipe
            toolbar.setTitle(recipeName);
            // sets up navigation on the action bar
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // creates the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, R.color.butter_cream),
                ContextCompat.getColor(this, R.color.chocolate_brown));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /**
     * up navigation
     *
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * creates a new fragment according to the tab position
         *
         * @param position
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new IngredientFragment();
                    break;
                case 1:
                    fragment = new StepFragment();
                    break;
            }
            return fragment;
        }

        /**
         * @return the number of tabs
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @param position
         * @return the name of each tab, according to the tab position
         */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Ingredients";
                case 1:
                    return "Steps";
            }
            return null;
        }
    }
}
