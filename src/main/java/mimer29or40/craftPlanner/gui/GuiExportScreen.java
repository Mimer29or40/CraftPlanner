package mimer29or40.craftPlanner.gui;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mimer29or40.craftPlanner.CraftPlanner;
import mimer29or40.craftPlanner.JeiHelper;
import mimer29or40.craftPlanner.model.Recipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GuiExportScreen extends GuiScreen
{
    public static final int GUI_ID = 0;

    private static final List<String> debugWindowLog = new ArrayList<>();
    private              int          offset         = 0;

    private static final Map<String, String> recipeCategories  = new TreeMap<>();
    private static final List<String>        categoryBlacklist = new ArrayList<>();

    private static final int dividingLine = 145;
    private static final int border       = 5;

    static
    {
        categoryBlacklist.add("Fuel");
    }

    public GuiExportScreen() { }

    @Override
    public void initGui()
    {
        super.initGui();

        buttonList.add(new GuiButton(0, border, border, dividingLine - border, 20, "Export Item Images"));
        buttonList.add(new GuiButton(1, border, 25 + border, dividingLine - border, 20, "List Available Categories"));
        buttonList.add(new GuiButton(2, border, 50 + border, dividingLine - border, 20, "Export Recipes"));
    }

    @Override
    protected void actionPerformed(GuiButton guiButton)
    {
        switch (guiButton.id)
        {
            case 0:
                Minecraft.getMinecraft().displayGuiScreen(new GuiExportItemIcons());
                break;
            case 1:
                listCategories();
                break;
            case 2:
                exportRecipes();
                break;
        }
    }

    private void returnScreen()
    {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }

    public static void addToDebugList(String text)
    {
        debugWindowLog.add(text);
    }

    public void scroll(int amount)
    {
        this.offset += amount;

        if (this.offset > debugWindowLog.size() - 19)
        {
            this.offset = debugWindowLog.size() - 19;
        }

        if (this.offset <= 0)
        {
            this.offset = 0;
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        final int x = Mouse.getEventX() * width / mc.displayWidth;
        final int y = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        if (dividingLine + border < x && x < width - border && border < y && y < height - border)
        {
            int scrollDelta = Mouse.getEventDWheel();
            if (scrollDelta < 0) { scroll(-1); }
            else if (scrollDelta > 0) scroll(1);
        }
        super.handleMouseInput();
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
    public void drawScreen(int mouseX, int mouseY, float frame)
    {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, frame);

        drawDebugWindow();
    }

    private void drawDebugWindow()
    {
        drawRect(dividingLine + border, border, width - border, height - border, Integer.MIN_VALUE);

        int lineNumber = 0;
        for (int i = debugWindowLog.size() - 1 - offset; i >= 0; i--)
        {
            drawString(fontRendererObj, debugWindowLog.get(i), dividingLine + border + 2, height - 15 - 12 * (debugWindowLog.size() - 1 - i - offset),
                       -1);
            lineNumber++;
            if (lineNumber >= 19) break;
        }
    }

    private Map<String, String> getRecipeCategories()
    {
        if (!recipeCategories.isEmpty())
        { return recipeCategories; }

        for (IRecipeCategory recipeCategory : JeiHelper.recipeRegistry.getRecipeCategories())
        {
            String title = recipeCategory.getTitle();
            if (categoryBlacklist.contains(title))
            { continue; }
            String uID = recipeCategory.getUid();
            recipeCategories.put(title, uID);
        }
        return recipeCategories;
    }

    private void listCategories()
    {
        for (String category : getRecipeCategories().keySet())
        {
            debugWindowLog.add(category);
        }
    }

    private void exportRecipes()
    {
        List<String> uIDs = new ArrayList<>();
        uIDs.addAll(getRecipeCategories().values());

        ImmutableList<IRecipeCategory> categories = JeiHelper.recipeRegistry.getRecipeCategories(uIDs);

        JsonArray recipeArray = new JsonArray();

        for (IRecipeCategory category : categories)
        {
            if (categoryBlacklist.contains(category.getTitle()))
            { continue; }

            for (Object recipeData : JeiHelper.recipeRegistry.getRecipes(category))
            {
                IRecipeHandler recipeHandler = JeiHelper.recipeRegistry.getRecipeHandler(recipeData.getClass());
                IRecipeWrapper recipeWrapper = recipeHandler.getRecipeWrapper(recipeData);

                Recipe recipe = new Recipe(recipeWrapper, category);

                recipeArray.add(recipe.toJson());

                debugWindowLog.add(recipe.getName() + " recipe created");
            }
        }

        String message;
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter file = new FileWriter(CraftPlanner.recipeFile);
            file.write(gson.toJson(recipeArray));
            file.flush();
            file.close();
            message = "Recipes exported successfully";
        }
        catch (Exception e)
        {
            e.printStackTrace();
            message = "An error occurred writing recipes";
        }
        debugWindowLog.add(message);
    }
}
