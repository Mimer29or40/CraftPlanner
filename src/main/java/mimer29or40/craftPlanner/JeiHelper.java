package mimer29or40.craftPlanner;

import mezz.jei.Internal;
import mezz.jei.ItemRegistry;
import mezz.jei.RecipeRegistry;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class JeiHelper
{
    public static final RecipeRegistry recipeRegistry = Internal.getRuntime().getRecipeRegistry();
    public static final ItemRegistry   itemRegistry   = Internal.getItemRegistry();
    // TODO Fluid Support
    public static final StackHelper    stackHelper    = Internal.getStackHelper();

    public static String getUniqueName(ItemStack itemStack)
    {
        return stackHelper.getUniqueIdentifierForStack(itemStack);
    }
}
