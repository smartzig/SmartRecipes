package recipes.smartzig.com.smartrecipes.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import recipes.smartzig.com.smartrecipes.database.Query;

public class Utils {

    private Query query;

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean checkCache() {
        return query.getRecipeList().size() != 0;
    }

}
