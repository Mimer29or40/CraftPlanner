package mimer29or40.craftPlanner.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

    public Recipe() {}

    public Recipe(IRecipeWrapper recipe, IRecipeCategory category)
    {
        this.category = category.getUid();

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
        for (InputItem inputItem : this.inputItems)
        { inputs.add(inputItem.toJson()); }
        recipe.add("inputs", inputs);

        JsonArray outputs = new JsonArray();
        for (OutputItem outputItem : this.outputItems)
        { outputs.add(outputItem.toJson()); }
        recipe.add("outputs", outputs);

        JsonArray fluidInputs = new JsonArray();
        for (InputFluid inputFluid : this.inputFluids)
        { fluidInputs.add(inputFluid.toJson()); }
        recipe.add("fluidInputs", fluidInputs);

        JsonArray fluidOutputs = new JsonArray();
        for (OutputFluid outputFluid : this.outputFluids)
        { fluidOutputs.add(outputFluid.toJson()); }
        recipe.add("fluidOutputs", fluidOutputs);

        return recipe;
    }

    public Recipe fromJson(JsonObject recipeObject)
    {
        this.name = recipeObject.get("name").getAsString();
        this.category = recipeObject.get("category").getAsString();
        this.id = recipeObject.get("id").getAsString();

        JsonArray inputs = recipeObject.getAsJsonArray("inputs");
        if (!inputs.isJsonNull())
        {
            for (JsonElement inputItem : inputs)
            { this.inputItems.add(new InputItem().fromJson(inputItem.getAsJsonObject())); }
        }

        JsonArray outputs = recipeObject.getAsJsonArray("outputs");
        if (!outputs.isJsonNull())
        {
            for (JsonElement outputItem : outputs)
            { this.outputItems.add(new OutputItem().fromJson(outputItem.getAsJsonObject())); }
        }

        JsonArray fluidInputs = recipeObject.getAsJsonArray("fluidInputs");
        if (!fluidInputs.isJsonNull())
        {
            for (JsonElement fluidInput : fluidInputs)
            { this.inputFluids.add(new InputFluid().fromJson(fluidInput.getAsJsonObject())); }
        }

        JsonArray fluidOutputs = recipeObject.getAsJsonArray("fluidOutputs");
        if (!fluidOutputs.isJsonNull())
        {
            for (JsonElement fluidOutput : fluidOutputs)
            { this.outputFluids.add(new OutputFluid().fromJson(fluidOutput.getAsJsonObject())); }
        }

        return this;
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
