package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import mezz.jei.Internal;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class InputItem
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;

    public InputItem() {}

    public InputItem(ItemStack itemStack, int slot, int amount)
    {
        this.name = itemStack.getDisplayName();
        this.id = Internal.getStackHelper().getUniqueIdentifierForStack(itemStack, StackHelper.UidMode.NORMAL);
        this.slot = slot;
        this.amount = amount;
    }

    public JsonObject toJson()
    {
        JsonObject inputItem = new JsonObject();

        inputItem.addProperty("name", this.name);
        inputItem.addProperty("id", this.id);
        inputItem.addProperty("slot", this.slot);
        inputItem.addProperty("amount", this.amount);

        return inputItem;
    }

    public InputItem fromJson(JsonObject inputItemObject)
    {
        this.name = inputItemObject.get("name").getAsString();
        this.id = inputItemObject.get("id").getAsString();
        this.slot = inputItemObject.get("slot").getAsInt();
        this.amount = inputItemObject.get("amount").getAsInt();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("InputItem:[name: %s,id: %s,slot: %s,amount: %s]",
                             this.name, this.id, this.slot, this.amount);
    }
}
