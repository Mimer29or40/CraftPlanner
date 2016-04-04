package mimer29or40.craftPlanner.mod;

import mimer29or40.craftPlanner.mod.util.Log;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;
import java.io.IOException;

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

    public static File configurationDir;
    public static File configFile;

    public static File recipeFile;

    @Mod.Instance(MOD_ID)
    public static CraftPlanner instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        configurationDir = new File(event.getModConfigurationDirectory(), MOD_NAME);
        if (!configurationDir.exists())
        {
            try
            {
                if (!configurationDir.mkdir())
                {
                    Log.error("Could not create config directory %s", configurationDir);
                    return;
                }
            }
            catch (SecurityException e)
            {
                Log.error("Could not create config directory %s %s", configurationDir, e);
                return;
            }
        }

        configFile = new File(configurationDir, "craftPlanner.cfg");
        if (!configFile.exists())
        {
            try
            {
                if (!configFile.createNewFile())
                {
                    Log.error("Could not create config file %s", configFile);
                }
            }
            catch (IOException e)
            {
                Log.error("Could not create config file %s", configFile);
            }
        }

        recipeFile = new File(configurationDir, "CachedRecipes.json");
        if (!recipeFile.exists())
        {
            try
            {
                if (!recipeFile.createNewFile())
                {
                    Log.error("Could not create recipe file %s", recipeFile);
                }
            }
            catch (IOException e)
            {
                Log.error("Could not create recipe file %s", recipeFile);
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        for (String oreName : OreDictionary.getOreNames())
        {
            System.out.println(oreName);
        }
    }
}
