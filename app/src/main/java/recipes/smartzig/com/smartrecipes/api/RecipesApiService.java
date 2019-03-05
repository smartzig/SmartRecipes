package recipes.smartzig.com.smartrecipes.api;

import java.util.List;

import recipes.smartzig.com.smartrecipes.to.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

interface RecipesApiService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}