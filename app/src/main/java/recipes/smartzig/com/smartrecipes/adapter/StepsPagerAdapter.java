package recipes.smartzig.com.smartrecipes.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import recipes.smartzig.com.smartrecipes.R;
import recipes.smartzig.com.smartrecipes.RecipeStepsFragment;
import recipes.smartzig.com.smartrecipes.to.Step;
import recipes.smartzig.com.smartrecipes.utils.Constants;

public class StepsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Step> steps;

    public StepsPagerAdapter(Context context, List<Step> steps, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEP_SELECTED_KEY, steps.get(position));
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO: 7/2/2018 FIx the title and Corresponding tab
        return String.format(String.valueOf(context.getResources().getText(R.string.step_title)), position + 1);
    }

    @Override

    public int getCount() {
        return steps.size();
    }
}
