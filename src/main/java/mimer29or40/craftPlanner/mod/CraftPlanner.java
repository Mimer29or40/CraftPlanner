package mimer29or40.craftPlanner.mod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

@Mod(
        name = CraftPlanner.MOD_NAME,
        modid = CraftPlanner.MOD_ID,
        version = CraftPlanner.VERSION,
        acceptedMinecraftVersions = "[1.8.9]",
        dependencies = CraftPlanner.DEPENDENCIES
)
public class CraftPlanner
{
    public static final String MOD_NAME = "Crafting Planner";
    public static final String MOD_ID   = "craftPlanner";

    public static final String VERSION = "1.8.9-0.0.0.1";

    public static final String DEPENDENCIES = "required-after:JEI@[2.28,)";

    public static final String CLIENT_PROXY = "mimer29or40.craftPlanner.mod.ProxyClient";
    public static final String COMMON_PROXY = "mimer29or40.craftPlanner.mod.ProxyCommon";

    @Mod.Instance(MOD_ID)
    public static CraftPlanner instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static ProxyCommon proxy;
    
    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        for (String oreName : OreDictionary.getOreNames())
        {
            System.out.println(oreName);
        }
    }
}
