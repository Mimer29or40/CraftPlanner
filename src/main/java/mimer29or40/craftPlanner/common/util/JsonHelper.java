package mimer29or40.craftPlanner.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.FileWriter;

public class JsonHelper
{
    public static void writeToFile(String fileName, JsonElement jsonElement) throws Exception
    {
        if (!fileName.contains(".json"))
            fileName += ".json";

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        FileWriter file = new FileWriter(fileName);
        file.write(gson.toJson(jsonElement));
        file.flush();
        file.close();
    }
}
