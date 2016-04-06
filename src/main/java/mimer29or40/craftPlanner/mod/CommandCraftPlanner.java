package mimer29or40.craftPlanner.mod;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import mezz.jei.Internal;
import mezz.jei.RecipeRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mimer29or40.craftPlanner.application.ApplicationCraftPlanner;
import mimer29or40.craftPlanner.common.Recipe;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.io.FileWriter;
import java.util.*;

public class CommandCraftPlanner extends CommandBase
{
    private static final List<String>        subCommands            = new ArrayList<>();
    private static final Map<String, String> subCommandUsage        = new HashMap<>();
    private static final Map<String, String> subCommandDescriptions = new HashMap<>();

    private static final Map<String, String> recipeCategories       = new TreeMap<>();
    private static final List<String>        categoryBlacklist      = new ArrayList<>();

    static
    {
        subCommands.add("help");
        subCommands.add("list");
        subCommands.add("export");
        subCommands.add("start");

        subCommandUsage.put("help", " [Command] Alias: (h)");
        subCommandUsage.put("list", " Alias: (l)");
        subCommandUsage.put("export", " Alias: (e)");
        subCommandUsage.put("start", " Alias: (s)");

        subCommandDescriptions.put("help", "");
        subCommandDescriptions.put("list", " Lists the Recipe Categories");
        subCommandDescriptions.put("export", " Exports all recipes to Recipes.json in the base folder");
        subCommandDescriptions.put("start", " Starts the Craft Planner Application");

        categoryBlacklist.add("Fuel");
    }

    @Override
    public String getCommandName()
    {
        return "craft-planner";
    }

    @Override
    public List<String> getCommandAliases()
    {
        return Collections.singletonList("cp");
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender)
    {
        return "/craft-planner help";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, subCommands) :
               args.length == 2 && (args[0].equals("help") || args[0].equals("h")) ? getListOfStringsMatchingLastWord(args, subCommands) :
               new ArrayList<String>();
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            iCommandSender.addChatMessage(new ChatComponentText(getCommandUsage(iCommandSender)));
            return;
        }

        String subCommand = args[0];
        switch (subCommand)
        {
            case "help":
                help(iCommandSender, args);
                break;
            case "h":
                help(iCommandSender, args);
                break;
            case "list":
                listCategories(iCommandSender);
                break;
            case "l":
                listCategories(iCommandSender);
                break;
            case "export":
                exportRecipes(iCommandSender);
                break;
            case "e":
                exportRecipes(iCommandSender);
                break;
            case "start":
                ApplicationCraftPlanner.startApplication(true);
                break;
            default:
                iCommandSender.addChatMessage(new ChatComponentText("Command not recognized\n" + getCommandUsage(iCommandSender)));
        }
    }

    private Map<String, String> getRecipeCategories()
    {
        if (!recipeCategories.isEmpty())
            return recipeCategories;

        RecipeRegistry registry = Internal.getRuntime().getRecipeRegistry();
        for (IRecipeCategory recipeCategory : registry.getRecipeCategories())
        {
            String title = recipeCategory.getTitle();
            if (categoryBlacklist.contains(title))
                continue;
            String uID = recipeCategory.getUid();
            recipeCategories.put(title, uID);
        }
        return recipeCategories;
    }

    private void help(ICommandSender iCommandSender, String[] args)
    {
        String message;
        if (args.length < 2)
        {
            message = "Available Commands:\n";
            for (String subCommand : subCommands)
                message += "/" + subCommand + subCommandUsage.get(subCommand) + "\n";
            message = message.substring(0, message.length() - 1);
        }
        else
            message = "/" + args[1] + subCommandUsage.get(args[1]) + subCommandDescriptions.get(args[1]);

        iCommandSender.addChatMessage(new ChatComponentText(message));
    }

    private void listCategories(ICommandSender iCommandSender)
    {
        for (String category : getRecipeCategories().keySet())
        {
            iCommandSender.addChatMessage(new ChatComponentText(category));
        }
    }

    private void exportRecipes(ICommandSender iCommandSender)
    {
        RecipeRegistry registry = Internal.getRuntime().getRecipeRegistry();

        List<String> uIDs = new ArrayList<>();
        uIDs.addAll(getRecipeCategories().values());

        ImmutableList<IRecipeCategory> categories = registry.getRecipeCategories(uIDs);

        JsonArray recipeArray = new JsonArray();

        for (IRecipeCategory category : categories)
        {
            if (categoryBlacklist.contains(category.getTitle()))
                continue;

            for (Object recipeData : registry.getRecipes(category))
            {
                IRecipeHandler recipeHandler = registry.getRecipeHandler(recipeData.getClass());
                IRecipeWrapper recipeWrapper = recipeHandler.getRecipeWrapper(recipeData);

                Recipe recipe = new Recipe(recipeWrapper, category);

                recipeArray.add(recipe.toJson());
                iCommandSender.addChatMessage(new ChatComponentText(recipe.getCategory() + " recipe for " + recipe.getName() + " found"));
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
        iCommandSender.addChatMessage(new ChatComponentText(message));
    }
}
