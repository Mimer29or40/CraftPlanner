package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import mezz.jei.Internal;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class RecipeItem
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;
    public float  probability;

    public RecipeItem() {}

    public RecipeItem(ItemStack itemStack, int slot, int amount)
    {
        this(itemStack, slot, amount, 1.0f);
    }

    public RecipeItem(ItemStack itemStack, int slot, int amount, float probability)
    {
        this.name = itemStack.getDisplayName();
        this.id = Internal.getStackHelper().getUniqueIdentifierForStack(itemStack, StackHelper.UidMode.NORMAL);
        this.slot = slot;
        this.amount = amount;
        this.probability = probability;
    }

    public JsonObject toJson()
    {
        JsonObject outputItem = new JsonObject();

        outputItem.addProperty("name", this.name);
        outputItem.addProperty("id", this.id);
        outputItem.addProperty("slot", this.slot);
        outputItem.addProperty("amount", this.amount);
        outputItem.addProperty("probability", this.probability);

        return outputItem;
    }

    public RecipeItem fromJson(JsonObject outputItemObject)
    {
        this.name = outputItemObject.get("name").getAsString();
        this.id = outputItemObject.get("id").getAsString();
        this.slot = outputItemObject.get("slot").getAsInt();
        this.amount = outputItemObject.get("amount").getAsInt();
        this.probability = outputItemObject.get("probability").getAsFloat();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("Item [name: %s,id: %s,slot: %s,amount: %s, probability: %s]",
                             this.name, this.id, this.slot, this.amount, this.probability);
    }
}
