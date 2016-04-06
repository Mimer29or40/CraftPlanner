package mimer29or40.craftPlanner.application;

import mimer29or40.craftPlanner.common.Recipe;
import mimer29or40.craftPlanner.common.util.Log;

import java.util.HashMap;
import java.util.Map;

public class DataCache
{

    public static final Map<String, Recipe> recipes = new HashMap<>();

    public static void loadRecipes()
    {
        Log.info("Loading Cached Recipes...");
        clear();
    }

    private static void clear()
    {
        recipes.clear();

    }

    private DataCache() {}
}
