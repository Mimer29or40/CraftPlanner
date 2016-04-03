package mimer29or40.craftPlanner.common;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class OutputFluid
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;
    public float  probability;

    public OutputFluid(FluidStack fluidStack, int slot, int amount, float probability)
    {
        this.name = fluidStack.getLocalizedName();
        this.id = FluidRegistry.getFluidName(fluidStack);
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
