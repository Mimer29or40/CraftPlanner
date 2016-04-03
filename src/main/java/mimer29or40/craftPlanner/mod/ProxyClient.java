package mimer29or40.craftPlanner.mod;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new CommandRecipeDump());
    }
}
