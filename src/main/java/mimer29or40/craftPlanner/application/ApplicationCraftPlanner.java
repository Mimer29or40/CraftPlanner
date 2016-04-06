package mimer29or40.craftPlanner.application;

import javafx.application.Application;
import javafx.stage.Stage;
import mimer29or40.craftPlanner.common.util.Log;

import javax.swing.*;
import java.io.File;

public class ApplicationCraftPlanner extends Application
{
    public static Stage mainStage;

    public static Settings settings;

    public static boolean runFromMinecraft = true;

    public static void startApplication(boolean runFrom)
    {
        runFromMinecraft = runFrom;
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        settings = new Settings("config.json");

        File currentDir = new File("");
        settings.setProperty("currentDir", currentDir.getAbsolutePath());

        settings.setProperty("configDir", currentDir.getAbsolutePath() + "/config/Crafting Planner");

        if (runFromMinecraft)
        { settings.setProperty("minecraftDir", currentDir.getAbsolutePath()); }
        else
        {
            settings.setProperty("minecraftDir", "");
            while (settings.getProperty("minecraftDir").isEmpty())
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.showDialog(null, "Select Minecraft Directory");

                File file = chooser.getSelectedFile();
                if (file != null) settings.setProperty("minecraftDir", file.getAbsolutePath());
            }
        }

        DataCache.loadRecipes();

        mainStage = primaryStage;

        try
        {
//            FXMLLoader loader = new FXMLLoader();
//            RecipesGui

        }
        catch (Exception e)
        {
            Log.error("An error occured when launching application: [%s]", e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        startApplication(false);
    }
}
