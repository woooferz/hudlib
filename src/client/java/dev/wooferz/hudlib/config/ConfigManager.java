package dev.wooferz.hudlib.config;

import com.google.gson.Gson;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import static dev.wooferz.hudlib.HudLibClient.LOGGER;

public class ConfigManager {
    private static ConfigManager instance = null;

    public Config config;
    boolean read = false;

    private ConfigManager() {

    }

    public Path fileLocation() {
        return FabricLoader.getInstance().getConfigDir().resolve("hudlib-base.json5");
    }

    public void read() {
        if (read) return;
        read = true;
        File config = fileLocation().toFile();
        try {
            Scanner configReader = new Scanner(config);
            StringBuilder jsonString = new StringBuilder();
            while (configReader.hasNextLine()) {
                jsonString.append(configReader.nextLine());
            }
            Gson gson = new Gson();
            this.config = gson.fromJson(jsonString.toString(), Config.class);


        } catch (FileNotFoundException e) {
            LOGGER.info("Config File Not Found!!");
            saveConfig();
        }
        //LOGGER.info("Thing: " + this.config.testField);
    }

    public void saveConfig() {
        File configFile = fileLocation().toFile();

        try {
            //noinspection ResultOfMethodCallIgnored
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            if (config == null) {
                config = new Config();
            }
            Gson gson = new Gson();
            String json = gson.toJson(config);

            writer.write(json);
            writer.close();


        } catch (IOException e) {
            LOGGER.error("Unexpected Error.");
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
}
