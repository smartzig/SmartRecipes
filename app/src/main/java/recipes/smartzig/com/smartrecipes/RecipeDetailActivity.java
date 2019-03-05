package recipes.smartzig.com.smartrecipes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import recipes.smartzig.com.smartrecipes.adapter.RecipeDetailAdapter;
import recipes.smartzig.com.smartrecipes.database.DBHelper;
import recipes.smartzig.com.smartrecipes.database.Query;
import recipes.smartzig.com.smartrecipes.to.Recipe;
import recipes.smartzig.com.smartrecipes.utils.Constants;
import recipes.smartzig.com.smartrecipes.widget.RecipeWidget;

public class RecipeDetailActivity extends AppCompatActivity {


    @BindView(R.id.recipe_step_list_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    private Recipe recipe;
    private boolean twoPane;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        DBHelper db = new DBHelper(this);
        query = new Query(db);
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Constants.RECIPE_KEY)) {
            recipe = getIntent().getExtras().getParcelable(Constants.RECIPE_KEY);
            recipe.setIngredients(query.getIngredientList(recipe.getId()));
            recipe.setSteps(query.getStepList(recipe.getId()));

        } else {
            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getResources().getText(R.string.noData), Snackbar.LENGTH_LONG).show();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane) {
            if (savedInstanceState == null && !recipe.getSteps().isEmpty()) {
                openStepDetail(0);
            }
        }
        initView();
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecipeDetailAdapter(recipe, this::openStepDetail));
    }


    private void openStepDetail(int position) {
        if (twoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.STEP_SELECTED_KEY, recipe.getSteps().get(position));
            RecipeStepsFragment fragment = new RecipeStepsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepsDetailActivity.class);
            intent.putExtra(Constants.RECIPE_KEY, recipe);
            intent.putExtra(Constants.STEP_SELECTED_KEY, position);
            startActivity(intent);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.menu, menu);
        return true;
    }


  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_widget) {
            WidgetService.addWidget(this, recipe);
            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getResources().getText(R.string.addedHomeScreen), Snackbar.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_widget) {
            updateWidget();
            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getResources().getText(R.string.addedHomeScreen), Snackbar.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void updateWidget() {
        query.setWidgetData(recipe.getIngredients());
      /*  AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds2 = manager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        manager.notifyAppWidgetViewDataChanged(appWidgetIds2, R.id.appwidget_text);*/

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        //Now update all widgets
        RecipeWidget.updateWidgets(this, appWidgetManager, appWidgetIds);

    }
}
