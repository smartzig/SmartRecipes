package recipes.smartzig.com.smartrecipes;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeTest {

    @Rule
    public ActivityTestRule<RecipesActivity>
            mActivityRule = new ActivityTestRule<>(RecipesActivity.class, false, true);

    @Test
    public void whenActivityIsLaunched_shouldDisplayInitialState() {
        onView(withId(R.id.recipe_main_recyclerview)).check(matches(isDisplayed()));

    }

    @Test
    public void whenRecipeListIsShowClickOnIt() {
        onView(withId(R.id.recipe_main_recyclerview)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.recipe_main_recyclerview))

                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.detail_toolbar))))
                .check(matches(withText("Nutella Pie")));


    }

    @Test
    public void whenRecipeListIsShowClickOnItAndAddWidget() {
        onView(withId(R.id.recipe_main_recyclerview)).check(matches(isDisplayed()));

        onView(ViewMatchers.withId(R.id.recipe_main_recyclerview))

                .perform(RecyclerViewActions.actionOnItemAtPosition(0,
                        click()));
        // Checks that the OrderActivity opens with the correct tea name displayed
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.detail_toolbar))))
                .check(matches(withText("Nutella Pie")));


        onView(withId(R.id.add_widget)).perform(click());
    }


}
