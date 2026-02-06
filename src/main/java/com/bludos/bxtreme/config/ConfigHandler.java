package com.bludos.bxtreme.config;

import com.bludos.bxtreme.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/bxtreme_config.json");
    
    public BXtremeConfig config;
    private long lastSaveTime = 0;
    private static final long SAVE_COOLDOWN = 2000; // 2 seconds between saves
    
    public ConfigHandler() {
        this.config = new BXtremeConfig();
    }
    
    public void load() {
        if (!CONFIG_FILE.exists()) {
            save();
            Main.LOGGER.info("Created default BXtreme config file");
            return;
        }
        
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, BXtremeConfig.class);
            Main.LOGGER.info("Loaded BXtreme config successfully");
        } catch (IOException e) {
            Main.LOGGER.error("Failed to load config, using defaults", e);
            config = new BXtremeConfig();
        }
    }
    
    public void save() {
        // Prevent spam saving - only save once every 2 seconds
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSaveTime < SAVE_COOLDOWN) {
            return; // Too soon, skip
        }
        lastSaveTime = currentTime;
        
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(config, writer);
            }
            Main.LOGGER.info("Saved BXtreme config");
        } catch (IOException e) {
            Main.LOGGER.error("Failed to save config", e);
        }
    }
    
    public BXtremeConfig get() {
        return config;
    }
}
