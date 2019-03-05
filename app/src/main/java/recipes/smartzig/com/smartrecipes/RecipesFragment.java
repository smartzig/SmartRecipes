package recipes.smartzig.com.smartrecipes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import recipes.smartzig.com.smartrecipes.adapter.RecipesAdapter;
import recipes.smartzig.com.smartrecipes.database.DBHelper;
import recipes.smartzig.com.smartrecipes.database.Query;
import recipes.smartzig.com.smartrecipes.to.Recipe;
import recipes.smartzig.com.smartrecipes.utils.Constants;
import recipes.smartzig.com.smartrecipes.utils.Utils;

public class RecipesFragment extends Fragment {

    @BindView(R.id.recipe_main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout refreshLayout;

    private Query query;
    private DBHelper db;

    private List<Recipe> recipesList;

    private BroadcastReceiver networkChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (recipesList == null) {
                loadRecipes();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recipe_list, container, false);
        ButterKnife.bind(this, view);

        db = new DBHelper(getContext());
        query = new Query(db);

        refreshLayout.setOnRefreshListener(this::loadRecipes);

        initView();
        return view;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);

        boolean twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }


    private void loadRecipes() {


        if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
            refreshLayout.setRefreshing(true);
            recipesList = query.getRecipeList();
            recyclerView.setAdapter(new RecipesAdapter(getContext(), recipesList, position -> {
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                intent.putExtra(Constants.RECIPE_KEY, recipesList.get(position));
                startActivity(intent);
            }));
            refreshLayout.setRefreshing(false);
        } else {
            refreshLayout.setRefreshing(false);
            Snackbar.make(Objects.requireNonNull(getView()), getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    /*private void loadRecipes(){
        if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
            refreshLayout.setRefreshing(true);
            RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
                @Override
                public void onResponse(List<Recipe> result) {
                    if (result != null) {
                        refreshLayout.setRefreshing(false);
                        recipesList = result;
                        recyclerView.setAdapter(new RecipesAdapter(getContext(), recipesList, position -> {
                            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                            intent.putExtra(Constants.RECIPE_KEY, recipesList.get(position));
                            startActivity(intent);
                        }));
                        // FIXME: 7/5/2018 Come back
                        // baseApplication.setIdleState(false);
                    }
                }

                @Override
                public void onCancel() {
                    Snackbar.make(Objects.requireNonNull(getView()), getResources().getText(R.string.request_cancel), Snackbar.LENGTH_LONG).show();
                    refreshLayout.setRefreshing(false);
                }
            });
        } else {
            refreshLayout.setRefreshing(false);
            Snackbar.make(Objects.requireNonNull(getView()), getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).show();
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(networkChange, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(networkChange);
    }
}
