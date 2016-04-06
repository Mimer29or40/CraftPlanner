package mimer29or40.craftPlanner;

import mimer29or40.craftPlanner.gui.GuiExportScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeyHandler
{
    public KeyBinding keyOpenExportMenu = new KeyBinding("keybind.openExportMenu", Keyboard.KEY_O, "keys.categories.craftPlanner");

    public KeyHandler()
    {
        ClientRegistry.registerKeyBinding(keyOpenExportMenu);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event)
    {
        if (keyOpenExportMenu.isPressed())
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiExportScreen());
        }
    }
}
