package io.dianto.baking.app.view.activity;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.dianto.baking.app.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BakingAppTest {

    @Rule
    public ActivityTestRule<SplashActivity> mActivityTestRule = new ActivityTestRule<>(SplashActivity.class);

    @Test
    public void bakingAppTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.adapter_recipes_title), withText("Nutella Pie"),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipes_cardview), 0), 1)))
                .check(matches(withText("Nutella Pie")));

        onView(allOf(withId(R.id.adapter_recipes_ingredient), withText("9 ingredients"),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipes_cardview), 0), 2)))
                .check(matches(withText("9 ingredients")));

        onView(allOf(withId(R.id.adapter_recipes_step), withText("7 steps"),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipes_cardview), 0), 3)))
                .check(matches(withText("7 steps")));

        onView(allOf(withId(R.id.main_recipes))).perform(actionOnItemAtPosition(0, click()));

        onView(allOf(withText("Ingredients :"), childAtPosition(childAtPosition(
                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 0),
                isDisplayed())).check(matches(withText("Ingredients :")));

        onView(allOf(withId(R.id.detail_ingredients),
                withText("- 2 CUP of Graham Cracker crumbs.\n" +
                        "- 6 TBLSP of unsalted butter, melted.\n" +
                        "- 0.5 CUP of granulated sugar.\n" +
                        "- 1.5 TSP of salt.\n" +
                        "- 5 TBLSP of vanilla.\n" +
                        "- 1 K of Nutella or other chocolate-hazelnut spread.\n" +
                        "- 500 G of Mascapone Cheese(room temperature).\n" +
                        "- 1 CUP of heavy cream(cold).\n" +
                        "- 4 OZ of cream cheese(softened).\n"),
                childAtPosition(childAtPosition(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class), 0), 1),
                isDisplayed())).check(matches(withText("- 2 CUP of Graham Cracker crumbs.\n" +
                "- 6 TBLSP of unsalted butter, melted.\n" +
                "- 0.5 CUP of granulated sugar.\n" +
                "- 1.5 TSP of salt.\n" +
                "- 5 TBLSP of vanilla.\n" +
                "- 1 K of Nutella or other chocolate-hazelnut spread.\n" +
                "- 500 G of Mascapone Cheese(room temperature).\n" +
                "- 1 CUP of heavy cream(cold).\n" +
                "- 4 OZ of cream cheese(softened).\n")));

        onView(allOf(withText("Steps :"),
                childAtPosition(childAtPosition(withId(R.id.detail_nsv), 0), 1),
                isDisplayed())).check(matches(withText("Steps :")));

        onView(allOf(withId(R.id.adapter_recipe_step_title), withText("Recipes Introduction"),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipe_step_cardview), 0), 1)))
                .check(matches(withText("Recipes Introduction")));

        onView(allOf(withId(R.id.adapter_recipe_step_title), withText("Starting prep"),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipe_step_cardview), 0), 1)))
                .check(matches(withText("Starting prep")));

        onView(allOf(withId(R.id.adapter_recipe_step_title), withText("Prep the cookie crust."),
                childAtPosition(childAtPosition(withId(R.id.adapter_recipe_step_cardview), 0), 1)))
                .check(matches(withText("Prep the cookie crust.")));

        onView(allOf(withId(R.id.detail_steps))).perform(actionOnItemAtPosition(0, click()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withText("Nutella Pie - Recipes Introduction")))
                .check(matches(withText("Nutella Pie - Recipes Introduction")));

        onView(allOf(withId(R.id.detail_step_instruction), withText("Recipes Introduction")))
                .check(matches(withText("Recipes Introduction")));

        onView(allOf(withId(R.id.detail_step_nav_next)))
                .check(matches(isDisplayed()));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"),
                withParent(withId(R.id.detail_step_navigation)))).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withText("Nutella Pie - Starting prep")))
                .check(matches(withText("Nutella Pie - Starting prep")));

        onView(allOf(withId(R.id.detail_step_instruction), withText("1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.")))
                .check(matches(withText("1. Preheat the oven to 350°F. Butter a 9\" deep dish pie pan.")));

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"), withParent(withId(R.id.detail_step_navigation)),
                isDisplayed())).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"),
                withParent(withId(R.id.detail_step_navigation)),
                isDisplayed())).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"),
                withParent(withId(R.id.detail_step_navigation)),
                isDisplayed())).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"),
                withParent(withId(R.id.detail_step_navigation)),
                isDisplayed())).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withId(R.id.detail_step_nav_next), withText("Next Steps"),
                withParent(withId(R.id.detail_step_navigation)),
                isDisplayed())).perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(allOf(withText("Nutella Pie - Finishing Steps")))
                .check(matches(withText("Nutella Pie - Finishing Steps")));

        onView(allOf(withId(R.id.detail_step_instruction),
                withText("6. Pour the filling into the prepared crust and smooth the top. " +
                        "Spread the whipped cream over the filling. " +
                        "Refrigerate the pie for at least 2 hours. " +
                        "Then it's ready to serve!"))).check(matches(
                withText("6. Pour the filling into the prepared crust and smooth the top. " +
                        "Spread the whipped cream over the filling. " +
                        "Refrigerate the pie for at least 2 hours. " +
                        "Then it's ready to serve!")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
