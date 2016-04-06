package mimer29or40.craftPlanner.gui;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case GuiExportItemIcons.GUI_ID:
                return new GuiExportItemIcons();
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (world instanceof WorldClient)
        {
            switch (ID)
            {
                case GuiExportItemIcons.GUI_ID:
                    return new GuiExportItemIcons();
            }
        }
        return null;
    }
}
