package mimer29or40.craftPlanner.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mezz.jei.Internal;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Recipe
{
    private String                 name;
    private String                 id;
    private String                 category;

    private final List<InputItem>   inputItems   = new ArrayList<>();
    private final List<OutputItem>  outputItems  = new ArrayList<>();
    private final List<InputFluid>  inputFluids  = new ArrayList<>();
    private final List<OutputFluid> outputFluids = new ArrayList<>();

    public Recipe(IRecipeWrapper recipe, IRecipeCategory category)
    {
        this.category = category.getTitle();

        Object mainOutput = recipe.getOutputs().get(0);
        if (mainOutput instanceof ItemStack)
        {
            this.name = ((ItemStack) mainOutput).getDisplayName();
            this.id = Internal.getStackHelper().getUniqueIdentifierForStack((ItemStack) mainOutput, StackHelper.UidMode.NORMAL);
        }
        else
            this.name = this.id = mainOutput.toString();

        // TODO Handle OreDictionary values
        Object[] inputItems = recipe.getInputs().toArray();
        for (int i = 0; i < inputItems.length; i++)
            if (inputItems[i] instanceof ItemStack)
                setInputItem((ItemStack) inputItems[i], i, ((ItemStack) inputItems[i]).stackSize);
            else if (inputItems[i] instanceof List)
                for (Object subItem : (List) inputItems[i])
                    setInputItem((ItemStack) subItem, i, ((ItemStack) subItem).stackSize);

        // TODO Handle probable output ie. Secondary output from TE
        Object[] outputItems = recipe.getOutputs().toArray();
        for (int i = 0; i < outputItems.length; i++)
            if (outputItems[i] instanceof ItemStack)
                setOutputItem((ItemStack) outputItems[i], i, ((ItemStack) outputItems[i]).stackSize, 1.0f);
            else if (outputItems[i] instanceof List)
                for (Object subItem : (List) outputItems[i])
                setOutputItem((ItemStack) subItem, i, ((ItemStack) subItem).stackSize, 1.0f);

        // TODO Handle fluid inputs and outputs
//        Object mainFluidOutput = recipe.getFluidOutputs().get(0);
//        Object[] inputFluids = recipe.getFluidInputs().toArray();
//        Object[] outputFluids = recipe.getFluidOutputs().toArray();
    }

    public String getName()
    {
        return this.name;
    }

    public String getId()
    {
        return this.id;
    }

    public String getCategory()
    {
        return this.category;
    }

    private void setInputItem(ItemStack itemStack, int slot, int amount)
    {
        if (itemStack != null)
        {
            InputItem inputItem = new InputItem(itemStack, slot, amount);
            if (!inputItems.contains(inputItem))
                inputItems.add(inputItem);
        }
    }

    private void setOutputItem(ItemStack itemStack, int slot, int amount, float probability)
    {
        if (itemStack != null)
        {
            OutputItem outputItem = new OutputItem(itemStack, slot, amount, probability);
            if (!outputItems.contains(outputItem))
                outputItems.add(outputItem);
        }
    }

    public JsonObject toJson()
    {
        JsonObject recipe = new JsonObject();

        recipe.addProperty("name", this.name);
        recipe.addProperty("category", this.category);
        recipe.addProperty("id", this.id);

        JsonArray inputs = new JsonArray();
        for (InputItem inputItem : inputItems)
        {
            JsonObject input = new JsonObject();
            input.addProperty("name", inputItem.name);
            input.addProperty("id", inputItem.id);
            input.addProperty("slot", inputItem.slot);
            input.addProperty("amount", inputItem.amount);
            inputs.add(input);
        }
        recipe.add("inputs", inputs);

        JsonArray outputs = new JsonArray();
        for (OutputItem outputItem : outputItems)
        {
            JsonObject output = new JsonObject();
            output.addProperty("name", outputItem.name);
            output.addProperty("id", outputItem.id);
            output.addProperty("slot", outputItem.slot);
            output.addProperty("amount", outputItem.amount);
            output.addProperty("probability", outputItem.probability);
            outputs.add(output);
        }
        recipe.add("outputs", outputs);

        JsonArray fluidInputs = new JsonArray();
        for (InputFluid inputFluid : inputFluids)
        {
            JsonObject input = new JsonObject();
            input.addProperty("name", inputFluid.name);
            input.addProperty("id", inputFluid.id);
            input.addProperty("slot", inputFluid.slot);
            input.addProperty("amount", inputFluid.amount);
            fluidInputs.add(input);
        }
        recipe.add("fluidInputs", fluidInputs);

        JsonArray fluidOutputs = new JsonArray();
        for (OutputFluid outputFluid : outputFluids)
        {
            JsonObject output = new JsonObject();
            output.addProperty("name", outputFluid.name);
            output.addProperty("id", outputFluid.id);
            output.addProperty("slot", outputFluid.slot);
            output.addProperty("amount", outputFluid.amount);
            output.addProperty("probability", outputFluid.probability);
            fluidOutputs.add(output);
        }
        recipe.add("fluidOutputs", fluidOutputs);

        return recipe;
    }

    @Override
    public String toString()
    {
        return String.format("Recipe[name: %s, id: %s, category: %s]", this.name, this.id, this.category);
    }

    public enum RecipeGrid
    {
        THREE, TWO
    }
}
