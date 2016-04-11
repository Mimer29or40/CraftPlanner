package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import mimer29or40.craftPlanner.JeiHelper;
import net.minecraft.item.ItemStack;

public class ItemWrapper
{
    private String name;
    private String id;

    public ItemWrapper() {}

    public ItemWrapper(ItemStack itemStack)
    {
        this.name = itemStack.getDisplayName();
        this.id = JeiHelper.getUniqueName(itemStack);
    }

    public String getName()
    {
        return this.name;
    }

    public String getId()
    {
        return this.id;
    }

    public JsonObject toJson()
    {
        JsonObject item = new JsonObject();

        item.addProperty("name", this.name);
        item.addProperty("id", this.id);

        return item;
    }

    public ItemWrapper fromJson(JsonObject recipeObject)
    {
        this.name = recipeObject.get("name").getAsString();
        this.id = recipeObject.get("id").getAsString();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("Item [name: %s, id: %s]", this.name, this.id);
    }
}
