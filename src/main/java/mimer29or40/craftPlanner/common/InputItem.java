package mimer29or40.craftPlanner.common;

import mezz.jei.Internal;
import mezz.jei.util.StackHelper;
import net.minecraft.item.ItemStack;

public class InputItem
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;

    public InputItem(ItemStack itemStack, int slot, int amount)
    {
        this.name = itemStack.getDisplayName();
        this.id = Internal.getStackHelper().getUniqueIdentifierForStack(itemStack, StackHelper.UidMode.NORMAL);
        this.slot = slot;
        this.amount = amount;
    }

    @Override
    public String toString()
    {
        return String.format("InputItem:[name: %s,id: %s,slot: %s,amount: %s]",
                             this.name, this.id, this.slot, this.amount);
    }
}
