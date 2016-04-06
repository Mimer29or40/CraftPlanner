package mimer29or40.craftPlanner.mod;

import mimer29or40.craftPlanner.common.util.Log;
import mimer29or40.craftPlanner.mod.gui.GuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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

    public static File configDir;
    public static File recipeFile;

    @Mod.Instance(MOD_ID)
    public static CraftPlanner instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static ProxyCommon proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        event.getModMetadata().version = VERSION;
        configDir = new File(event.getModConfigurationDirectory(), "CraftingPlanner");
        recipeFile = new File(configDir, "Recipes.json");

        if (!configDir.exists() && !configDir.mkdir()) Log.error("Could not create config directory");
        try
        {
            Files.createFile(recipeFile.toPath());
        }
        catch (IOException e)
        {
            Log.error("An error occurred when creating Recipes.json [%s]", e.getMessage());
        }

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
//        for (String oreName : OreDictionary.getOreNames())
//        {
//            System.out.println(oreName);
//        }
    }
}
