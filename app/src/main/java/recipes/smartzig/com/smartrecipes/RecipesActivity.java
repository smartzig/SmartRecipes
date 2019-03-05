package recipes.smartzig.com.smartrecipes;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import recipes.smartzig.com.smartrecipes.api.RecipesApiCallback;
import recipes.smartzig.com.smartrecipes.api.RecipesApiManager;
import recipes.smartzig.com.smartrecipes.database.DBHelper;
import recipes.smartzig.com.smartrecipes.database.Query;
import recipes.smartzig.com.smartrecipes.to.Recipe;
import recipes.smartzig.com.smartrecipes.utils.Utils;

public class RecipesActivity extends AppCompatActivity {

    private Query query;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        db = new DBHelper(this);
        query = new Query(db);

        if (Utils.isConnected(this)) {
            loadData();
        } else {
            //error
        }
        initFragment();
    }


    private void initFragment() {
        RecipesFragment fragment = new RecipesFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.recipe_main_fragment, fragment);
        transaction.commit();
    }


    private void loadData() {

        RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
            @Override
            public void onResponse(List<Recipe> result) {
                if (result != null) {
                    DBHelper db = new DBHelper(RecipesActivity.this);
                    Query query = new Query(db);
                    query.cache(result);
                }

            }


            @Override
            public void onCancel() {
            }
        });

    }


}
