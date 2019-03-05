package recipes.smartzig.com.smartrecipes.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import recipes.smartzig.com.smartrecipes.to.Ingredient;
import recipes.smartzig.com.smartrecipes.to.Recipe;
import recipes.smartzig.com.smartrecipes.to.Step;

public class Query {

    private DBHelper dbHelper;

    public Query(DBHelper db) {
        this.dbHelper = db;
    }

    public void cache(List<Recipe> recipes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        deleteCache(db);

        for (Recipe recipe : recipes) {
            ContentValues rValues = new ContentValues();
            rValues.put(DBHelper.COLUMN_ID, recipe.getId());
            rValues.put(DBHelper.COLUMN_NAME, recipe.getName());
            rValues.put(DBHelper.COLUMN_IMAGE, recipe.getImage());
            rValues.put(DBHelper.COLUMN_SERVINGS, recipe.getServings());

            db.insert(DBHelper.TABLE_RECIPE, null, rValues);

            for (Ingredient ingredient : recipe.getIngredients()) {
                ContentValues iValues = new ContentValues();
                iValues.put(DBHelper.COLUMN_QUANTITY, ingredient.getQuantity());
                iValues.put(DBHelper.COLUMN_MEASURE, ingredient.getMeasure());
                iValues.put(DBHelper.COLUMN_INGREDIENT, ingredient.getIngredient());
                iValues.put(DBHelper.COLUMN_RECIPE_ID, recipe.getId());
                db.insert(DBHelper.TABLE_INGREDIENT, null, iValues);
            }

            for (Step step : recipe.getSteps()) {
                ContentValues sValues = new ContentValues();
                sValues.put(DBHelper.COLUMN_ID, step.getId());
                sValues.put(DBHelper.COLUMN_SHORT_DESC, step.getShortDescription());
                sValues.put(DBHelper.COLUMN_DESC, step.getDescription());
                sValues.put(DBHelper.COLUMN_VIDEO_URL, step.getVideoURL());
                sValues.put(DBHelper.COLUMN_THUMBNAIL_URL, step.getThumbnailURL());
                sValues.put(DBHelper.COLUMN_RECIPE_ID, recipe.getId());
                db.insert(DBHelper.TABLE_STEP, null, sValues);
            }
        }
    }

    private void deleteCache(SQLiteDatabase db) {
        db.delete(DBHelper.TABLE_RECIPE, null, null);
        db.delete(DBHelper.TABLE_INGREDIENT, null, null);
        db.delete(DBHelper.TABLE_STEP, null, null);
    }

    public ArrayList<Recipe> getRecipeList() {
        ArrayList<Recipe> list = new ArrayList<>();
        Cursor mCursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " +
                DBHelper.TABLE_RECIPE, null);

        if (mCursor.moveToFirst()) {
            do {
                Recipe entry = new Recipe(
                        mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_ID)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_NAME)),
                        mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_SERVINGS)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_IMAGE))
                );

                list.add(entry);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return list;
    }

    public ArrayList<Ingredient> getIngredientList(int recipeId) {
        ArrayList<Ingredient> list = new ArrayList<>();
        Cursor mCursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TABLE_INGREDIENT
                + " WHERE " + DBHelper.COLUMN_RECIPE_ID + " = ?", new String[]{Integer.toString(recipeId)});

        if (mCursor.moveToFirst()) {
            do {
                Ingredient entry = new Ingredient(
                        mCursor.getFloat(mCursor.getColumnIndex(DBHelper.COLUMN_QUANTITY)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_MEASURE)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_INGREDIENT))
                );

                list.add(entry);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return list;
    }

    public ArrayList<Step> getStepList(int recipeId) {
        ArrayList<Step> list = new ArrayList<>();
        Cursor mCursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DBHelper.TABLE_STEP
                + " WHERE " + DBHelper.COLUMN_RECIPE_ID + " = ?", new String[]{Integer.toString(recipeId)});

        if (mCursor.moveToFirst()) {
            do {
                Step entry = new Step(
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_VIDEO_URL)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_DESC)),
                        mCursor.getInt(mCursor.getColumnIndex(DBHelper.COLUMN_ID)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_SHORT_DESC)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_THUMBNAIL_URL))

                );

                list.add(entry);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return list;
    }

    public ArrayList<Ingredient> getWidgetData() {
        ArrayList<Ingredient> list = new ArrayList<>();
        Cursor mCursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " +
                DBHelper.TABLE_WIDGET, null);

        if (mCursor.moveToFirst()) {
            do {
                Ingredient entry = new Ingredient(
                        mCursor.getFloat(mCursor.getColumnIndex(DBHelper.COLUMN_QUANTITY)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_MEASURE)),
                        mCursor.getString(mCursor.getColumnIndex(DBHelper.COLUMN_INGREDIENT))
                );
                list.add(entry);
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        return list;
    }

    public void setWidgetData(ArrayList<Ingredient> ingredients) {
        for (int i = 0; i < ingredients.size(); i++) {
            ContentValues iValues = new ContentValues();
            iValues.put(DBHelper.COLUMN_ID, i);
            iValues.put(DBHelper.COLUMN_QUANTITY, ingredients.get(i).getQuantity());
            iValues.put(DBHelper.COLUMN_MEASURE, ingredients.get(i).getMeasure());
            iValues.put(DBHelper.COLUMN_INGREDIENT, ingredients.get(i).getIngredient());
            dbHelper.getWritableDatabase().replace(DBHelper.TABLE_WIDGET, null, iValues);
        }
    }
}
