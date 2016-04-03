package mimer29or40.craftPlanner.common;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class InputFluid
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;

    public InputFluid(FluidStack fluidStack, int slot, int amount)
    {
        this.name = fluidStack.getLocalizedName();
        this.id = FluidRegistry.getFluidName(fluidStack);
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
