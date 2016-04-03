package mimer29or40.craftPlanner.common;

import mezz.jei.Internal;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class OutputItem
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;
    public float  probability;

    public OutputItem(ItemStack itemStack, int slot, int amount, float probability)
    {
        this.name = itemStack.getDisplayName();
        this.id = Internal.getStackHelper().getUniqueIdentifierForStack(itemStack, StackHelper.UidMode.NORMAL);
        this.slot = slot;
        this.amount = amount;
        this.probability = probability;
    }

    @Override
    public String toString()
    {
        return String.format("InputItem:[name: %s,id: %s,slot: %s,amount: %s, probability: %s]",
                             this.name, this.id, this.slot, this.amount, this.probability);
    }
}
