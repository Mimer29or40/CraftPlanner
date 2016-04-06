package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class InputFluid
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;

    public InputFluid() {}

    public InputFluid(FluidStack fluidStack, int slot, int amount)
    {
        this.name = fluidStack.getLocalizedName();
        this.id = FluidRegistry.getFluidName(fluidStack);
        this.slot = slot;
        this.amount = amount;
    }

    public JsonObject toJson()
    {
        JsonObject inputFluid = new JsonObject();

        inputFluid.addProperty("name", this.name);
        inputFluid.addProperty("id", this.id);
        inputFluid.addProperty("slot", this.slot);
        inputFluid.addProperty("amount", this.amount);

        return inputFluid;
    }

    public InputFluid fromJson(JsonObject inputFluidObject)
    {
        this.name = inputFluidObject.get("name").getAsString();
        this.id = inputFluidObject.get("id").getAsString();
        this.slot = inputFluidObject.get("slot").getAsInt();
        this.amount = inputFluidObject.get("amount").getAsInt();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("InputItem:[name: %s,id: %s,slot: %s,amount: %s]",
                             this.name, this.id, this.slot, this.amount);
    }
}
