package com.bludos.bxtreme;

import com.bludos.bxtreme.config.ConfigHandler;
import com.bludos.bxtreme.core.PerformanceMonitor;
import com.bludos.bxtreme.event.RenderEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("bxtreme")
public class Main {
    public static final String MOD_ID = "bxtreme";
    public static final Logger LOGGER = LogManager.getLogger();
    
    public static ConfigHandler config;
    public static PerformanceMonitor performanceMonitor;
    
    public Main() {
        LOGGER.info("BXtreme Performance Mod Initializing...");
        
        // Initialize config
        config = new ConfigHandler();
        config.load();
        
        // Register event handlers
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        
        LOGGER.info("BXtreme Performance Mod Loaded Successfully!");
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        performanceMonitor = new PerformanceMonitor();
        LOGGER.info("BXtreme Client Setup Complete!");
    }
}