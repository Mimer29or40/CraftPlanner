package mimer29or40.craftPlanner.model;

import com.google.gson.JsonObject;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class OutputFluid
{
    public String name;
    public String id;
    public int    slot;
    public int    amount;
    public float  probability;

    public OutputFluid() {}

    public OutputFluid(FluidStack fluidStack, int slot, int amount, float probability)
    {
        this.name = fluidStack.getLocalizedName();
        this.id = FluidRegistry.getFluidName(fluidStack);
        this.slot = slot;
        this.amount = amount;
        this.probability = probability;
    }

    public JsonObject toJson()
    {
        JsonObject outputFluid = new JsonObject();

        outputFluid.addProperty("name", this.name);
        outputFluid.addProperty("id", this.id);
        outputFluid.addProperty("slot", this.slot);
        outputFluid.addProperty("amount", this.amount);
        outputFluid.addProperty("probability", this.probability);

        return outputFluid;
    }

    public OutputFluid fromJson(JsonObject outputFluidObject)
    {
        this.name = outputFluidObject.get("name").getAsString();
        this.id = outputFluidObject.get("id").getAsString();
        this.slot = outputFluidObject.get("slot").getAsInt();
        this.amount = outputFluidObject.get("amount").getAsInt();
        this.probability = outputFluidObject.get("probability").getAsFloat();

        return this;
    }

    @Override
    public String toString()
    {
        return String.format("InputItem:[name: %s,id: %s,slot: %s,amount: %s, probability: %s]",
                             this.name, this.id, this.slot, this.amount, this.probability);
    }
}
