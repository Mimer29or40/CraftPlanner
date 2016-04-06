package mimer29or40.craftPlanner;

import mezz.jei.Internal;
import mezz.jei.RecipeRegistry;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class JeiHelper
{
    public static RecipeRegistry recipeRegistry = Internal.getRuntime().getRecipeRegistry();
    public static StackHelper    stackHelper    = Internal.getStackHelper();

    public static String getUniqueName(ItemStack itemStack)
    {
        return stackHelper.getUniqueIdentifierForStack(itemStack);
    }
}
