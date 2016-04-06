package mimer29or40.craftPlanner.application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import mimer29or40.craftPlanner.common.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings
{
    private String     name;
    private JsonObject properties;
    private File       file;

    public Settings(String name)
    {
        this.name = name;
        this.properties = new JsonObject();
        this.file = new File("config/Crafting Planner", name);
        load();
    }

    public String getProperty(String key)
    {
        return this.properties.get(key).getAsString();
    }

    public String setProperty(String key, Object value)
    {
        this.properties.addProperty(key, value.toString());
        save();
        return value.toString();
    }

    private void save()
    {
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            FileWriter fileWriter = new FileWriter(this.file);
            fileWriter.write(gson.toJson(this.properties));

            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException e)
        {
            Log.error("Error occurred while saving config: %s", this.name);
        }
    }

    private void load()
    {
        createFileIfNeeded();
        try
        {
            JsonParser jsonParser = new JsonParser();

            JsonReader jsonReader = new JsonReader(new FileReader(this.file));

            this.properties = jsonParser.parse(jsonReader).getAsJsonObject();
        }
        catch (IOException e)
        {
            Log.error("Error occurred while loading config: %s", this.name);
        }
    }

    private void createFileIfNeeded()
    {
        try
        {
            if (!this.file.exists())
            {
                if (!this.file.createNewFile())
                { Log.error("Could not create config: %s", this.name); }
                else
                {
                    FileWriter writer = new FileWriter(this.file);
                    writer.write("{}");
                    writer.flush();
                    writer.close();
                }
            }
        }
        catch (IOException e)
        {
            Log.error("Could not create config: %s", this.name);
        }
    }
}
