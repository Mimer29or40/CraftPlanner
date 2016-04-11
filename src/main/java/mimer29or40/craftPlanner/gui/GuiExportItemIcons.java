package mimer29or40.craftPlanner.gui;

import com.google.common.collect.ImmutableList;
import mimer29or40.craftPlanner.CraftPlanner;
import mimer29or40.craftPlanner.JeiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Arrays;

public class GuiExportItemIcons extends GuiScreen
{
    public static final int GUI_ID = 1;

    private final static int[] illegalChars = {34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                                               24, 25,
                                               26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 47};

    static
    {
        Arrays.sort(illegalChars);
    }

    private File dir;

    private int drawIndex;
    private int parseIndex;
    private int iconSize;
    private int borderSize;
    private int boxSize;

    private ImmutableList<ItemStack> itemStacks;

    public GuiExportItemIcons()
    {
        dir = new File(CraftPlanner.configDir, "itemIcons");
        if (dir.exists())
        {
            for (File file : dir.listFiles())
            { if (file.isFile() && !file.delete()) GuiExportScreen.addToDebugList(String.format("Could not delete file: %s", file.toString())); }
        }
        else if (!dir.mkdir()) GuiExportScreen.addToDebugList("Could not create itemIcons directory");

        iconSize = 64;
        borderSize = iconSize / 16;
        boxSize = iconSize + borderSize * 2;

        itemStacks = JeiHelper.itemRegistry.getItemList();
    }

    private void returnScreen()
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiExportScreen());
    }

    @Override
    protected void keyTyped(char c, int keycode) throws IOException
    {
        if (keycode == Keyboard.KEY_ESCAPE || keycode == Keyboard.KEY_BACK)
        {
            returnScreen();
            return;
        }
        super.keyTyped(c, keycode);
    }

    @Override
    public void drawScreen(int mousex, int mousey, float frame)
    {
        try
        {
            drawItems();
            exportItems();
        }
        catch (Exception e)
        {
            GuiExportScreen.addToDebugList(String.format("Error dumping item icons %s", e.getCause()));
        }
    }

    private void drawItems()
    {
        Minecraft mc = Minecraft.getMinecraft();
        Dimension d = new Dimension(mc.displayWidth, mc.displayHeight);

        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, d.width * 16D / iconSize, d.height * 16D / iconSize, 0, 1000, 3000);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.clearColor(0, 0, 0, 0);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        int rows = d.height / boxSize;
        int cols = d.width / boxSize;
        int fit = rows * cols;

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1, 1, 1, 1);

        for (int i = 0; drawIndex < itemStacks.size() && i < fit; drawIndex++, i++)
        {
            int x = i % cols * 18;
            int y = i / cols * 18;
            ItemStack itemStack = itemStacks.get(drawIndex);
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            if (itemStack != null && itemStack.getItem() != null && itemStack.getItem().getFontRenderer(itemStack) != null)
            {
                fontRenderer = itemStack.getItem().getFontRenderer(itemStack);
            }
            drawItem(x + 1, y + 1, itemStack, fontRenderer);
        }

        GL11.glFlush();
    }

    private static void drawItem(int i, int j, ItemStack itemstack, FontRenderer fontRenderer)
    {
        RenderItem drawItems = Minecraft.getMinecraft().getRenderItem();

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();

        float zLevel = drawItems.zLevel += 100F;
        try
        {
            drawItems.renderItemAndEffectIntoGUI(itemstack, i, j);
            drawItems.renderItemOverlays(fontRenderer, itemstack, i, j);
        }
        catch (Exception e)
        {
            drawItems.zLevel = zLevel;
            drawItems.renderItemIntoGUI(new ItemStack(Blocks.fire), i, j);
        }

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        drawItems.zLevel = zLevel - 100;
    }

    private void exportItems() throws IOException
    {
        BufferedImage img = screenshot();
        int rows = img.getHeight() / boxSize;
        int cols = img.getWidth() / boxSize;
        int fit = rows * cols;
        for (int i = 0; parseIndex < itemStacks.size() && i < fit; parseIndex++, i++)
        {
            int x = i % cols * boxSize;
            int y = i / cols * boxSize;
            exportImage(img.getSubimage(x + borderSize, y + borderSize, iconSize, iconSize), itemStacks.get(parseIndex));
        }

        if (parseIndex >= itemStacks.size())
        { returnScreen(); }
    }

    private static String cleanFileName(String name)
    {
        StringBuilder cleanName = new StringBuilder();
        for (int i = 0; i < name.length(); i++)
        {
            int c = (int) name.charAt(i);
            if (Arrays.binarySearch(illegalChars, c) < 0)
            { cleanName.append((char) c); }
            else
            { cleanName.append('_'); }
        }
        return cleanName.toString();
    }

    private void exportImage(BufferedImage img, ItemStack itemStack) throws IOException
    {
        String name = EnumChatFormatting.getTextWithoutFormattingCodes(JeiHelper.getUniqueName(itemStack));
//        name = cleanFileName(name);
        File file = new File(dir, name + ".png");
        for (int i = 2; file.exists(); i++)
        { file = new File(dir, name + '_' + i + ".png"); }
        ImageIO.write(img, "png", file);
        GuiExportScreen.addToDebugList(String.format("Image [%s] created", name));
    }

    private IntBuffer pixelBuffer;
    private int[]     pixelValues;

    private BufferedImage screenshot()
    {
        Framebuffer fb = Minecraft.getMinecraft().getFramebuffer();
        Minecraft mc = Minecraft.getMinecraft();
        Dimension mcSize = new Dimension(mc.displayWidth, mc.displayHeight);
        Dimension texSize = mcSize;

        if (OpenGlHelper.isFramebufferEnabled())
        { texSize = new Dimension(fb.framebufferTextureWidth, fb.framebufferTextureHeight); }

        int k = texSize.width * texSize.height;
        if (pixelBuffer == null || pixelBuffer.capacity() < k)
        {
            pixelBuffer = BufferUtils.createIntBuffer(k);
            pixelValues = new int[k];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled())
        {
            GlStateManager.bindTexture(fb.framebufferTexture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }
        else
        {
            GL11.glReadPixels(0, 0, texSize.width, texSize.height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        }

        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, texSize.width, texSize.height);

        BufferedImage img = new BufferedImage(mcSize.width, mcSize.height, BufferedImage.TYPE_INT_ARGB);
        if (OpenGlHelper.isFramebufferEnabled())
        {
            int yOff = texSize.height - mcSize.height;
            for (int y = 0; y < mcSize.height; ++y)
            {
                for (int x = 0; x < mcSize.width; ++x)
                { img.setRGB(x, y, pixelValues[(y + yOff) * texSize.width + x]); }
            }
        }
        else
        {
            img.setRGB(0, 0, texSize.width, height, pixelValues, 0, texSize.width);
        }

        return img;
    }
}
