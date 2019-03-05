package recipes.smartzig.com.smartrecipes.api;

import java.util.List;

import recipes.smartzig.com.smartrecipes.to.Recipe;

public interface RecipesApiCallback<T> {

    void onResponse(List<Recipe> result);

    void onCancel();

}