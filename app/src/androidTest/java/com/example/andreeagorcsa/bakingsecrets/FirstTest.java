package com.example.andreeagorcsa.bakingsecrets;

import com.example.andreeagorcsa.bakingsecrets.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.idling.CountingIdlingResource;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(JUnit4.class)
public class FirstTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void registerIdlingResource() {

    }

    @Test
    public void testCheckRecipeName() {
        CountingIdlingResource componentIdlingResource = mActivityRule.getActivity().getIdlingResourceInTest();
        Espresso.registerIdlingResources(componentIdlingResource);
        onView(withRecyclerView(R.id.recycler_view_recipe).atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        Espresso.registerIdlingResources(componentIdlingResource);
        onView(withRecyclerView(R.id.recycler_view_recipe).atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));
        Espresso.registerIdlingResources(componentIdlingResource);
        onView(withRecyclerView(R.id.recycler_view_recipe).atPosition(2))
                .check(matches(hasDescendant(withText("Yellow Cake"))));
    }
}