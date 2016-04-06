package mimer29or40.craftPlanner.mod;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {

    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        keyHandler = new KeyHandler();
        MinecraftForge.EVENT_BUS.register(keyHandler);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new CommandCraftPlanner());
    }
}
