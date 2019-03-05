package recipes.smartzig.com.smartrecipes.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import recipes.smartzig.com.smartrecipes.R;
import recipes.smartzig.com.smartrecipes.listener.OnItemClickListener;
import recipes.smartzig.com.smartrecipes.to.Ingredient;
import recipes.smartzig.com.smartrecipes.to.Recipe;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "RecipeDetailAdapter";
    private Recipe recipe;
    private OnItemClickListener listener;

    private int posit = -1;

    public RecipeDetailAdapter(Recipe recipe, OnItemClickListener listener) {
        this.recipe = recipe;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredients_content, parent, false);
            return new IngredientViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_detail_step_list_item, parent, false);
            return new StepsViewHolder(view);
        }
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof IngredientViewHolder) {
            IngredientViewHolder viewHolder = (IngredientViewHolder) holder;
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Ingredient ingredient = recipe.getIngredients().get(i);
                builder.append(String.format(Locale.getDefault(), ". %s (%.1f %s)",
                        ingredient.getIngredient(), ingredient.getQuantity(), ingredient.getMeasure()));
                builder.append("\n");
            }

            viewHolder.ingredients.setText(builder.toString());
        } else if (holder instanceof StepsViewHolder) {
            StepsViewHolder viewHolder = (StepsViewHolder) holder;

            viewHolder.step_number.setText(String.valueOf(holder.getAdapterPosition() - 1));
            viewHolder.step_name.setText(recipe.getSteps().get(holder.getAdapterPosition() - 1).getShortDescription());

            holder.itemView.setOnClickListener(view -> listener.onItemClick(holder.getAdapterPosition() - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.steps_number)
        TextView step_number;
        @BindView(R.id.steps_name)
        TextView step_name;

        public StepsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredients_txt)
        TextView ingredients;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
