package com.bludos.bxtreme.gui;

import com.bludos.bxtreme.Main;
import com.bludos.bxtreme.config.BXtremeConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BXtremeVideoSettingsScreen extends Screen {
    
    private final Screen lastScreen;
    private BXtremeConfig config;
    
    // Preset buttons
    private static final String[] PRESETS = {"ULTRA LOW", "LOW", "MEDIUM", "HIGH", "ULTRA"};
    private int currentPreset = 0;
    
    public BXtremeVideoSettingsScreen(Screen lastScreen) {
        super(Component.literal("BXtreme Video Settings"));
        this.lastScreen = lastScreen;
        this.config = Main.config.get();
    }
    
    @Override
    protected void init() {
        super.init();
        
        int y = 40;
        int spacing = 25;
        int buttonWidth = 200;
        int centerX = this.width / 2 - buttonWidth / 2;
        
        // TITLE
        addRenderableWidget(Button.builder(
            Component.literal("§l§nBXTREME RENDERER SETTINGS"),
            btn -> {}
        ).bounds(centerX, 10, buttonWidth, 20).build());
        
        // PRESET SELECTOR
        addRenderableWidget(Button.builder(
            Component.literal("Preset: " + PRESETS[currentPreset]),
            btn -> {
                currentPreset = (currentPreset + 1) % PRESETS.length;
                applyPreset(currentPreset);
                btn.setMessage(Component.literal("Preset: " + PRESETS[currentPreset]));
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // RENDER DISTANCE
        addRenderableWidget(Button.builder(
            Component.literal("Render Distance: " + Minecraft.getInstance().options.renderDistance().get()),
            btn -> {
                int current = Minecraft.getInstance().options.renderDistance().get();
                int next = (current % 16) + 2;
                Minecraft.getInstance().options.renderDistance().set(next);
                btn.setMessage(Component.literal("Render Distance: " + next));
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // ENTITY DISTANCE
        addRenderableWidget(Button.builder(
            Component.literal("Entity Distance: " + config.maxEntityRenderDistance),
            btn -> {
                config.maxEntityRenderDistance = (config.maxEntityRenderDistance % 64) + 8;
                btn.setMessage(Component.literal("Entity Distance: " + config.maxEntityRenderDistance));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // PARTICLE LIMIT
        addRenderableWidget(Button.builder(
            Component.literal("Particle Limit: " + config.particleLimit),
            btn -> {
                int[] limits = {10, 50, 100, 500, 1000, 2000};
                int current = 0;
                for (int i = 0; i < limits.length; i++) {
                    if (limits[i] == config.particleLimit) {
                        current = i;
                        break;
                    }
                }
                current = (current + 1) % limits.length;
                config.particleLimit = limits[current];
                btn.setMessage(Component.literal("Particle Limit: " + config.particleLimit));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // CHUNK UPDATE BUDGET
        addRenderableWidget(Button.builder(
            Component.literal("Chunk Updates/Frame: " + config.chunkUpdateBudget),
            btn -> {
                config.chunkUpdateBudget = (config.chunkUpdateBudget % 5) + 1;
                btn.setMessage(Component.literal("Chunk Updates/Frame: " + config.chunkUpdateBudget));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // ULTRA LOW QUALITY MODE
        addRenderableWidget(Button.builder(
            Component.literal("Ultra Low Quality: " + (config.ultraLowQualityMode ? "ON" : "OFF")),
            btn -> {
                config.ultraLowQualityMode = !config.ultraLowQualityMode;
                btn.setMessage(Component.literal("Ultra Low Quality: " + (config.ultraLowQualityMode ? "ON" : "OFF")));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // GREEDY MESHING
        addRenderableWidget(Button.builder(
            Component.literal("Greedy Meshing: " + (config.simplifyBlockModels ? "ON" : "OFF")),
            btn -> {
                config.simplifyBlockModels = !config.simplifyBlockModels;
                btn.setMessage(Component.literal("Greedy Meshing: " + (config.simplifyBlockModels ? "ON" : "OFF")));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // ASYNC CHUNK BUILDING
        addRenderableWidget(Button.builder(
            Component.literal("Async Chunks: " + (config.asyncChunkBuilding ? "ON" : "OFF")),
            btn -> {
                config.asyncChunkBuilding = !config.asyncChunkBuilding;
                btn.setMessage(Component.literal("Async Chunks: " + (config.asyncChunkBuilding ? "ON" : "OFF")));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // FPS OVERLAY
        addRenderableWidget(Button.builder(
            Component.literal("FPS Overlay: " + (config.showFPSOverlay ? "ON" : "OFF")),
            btn -> {
                config.showFPSOverlay = !config.showFPSOverlay;
                btn.setMessage(Component.literal("FPS Overlay: " + (config.showFPSOverlay ? "ON" : "OFF")));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing;
        
        // DETAILED STATS
        addRenderableWidget(Button.builder(
            Component.literal("Detailed Stats: " + (config.showDetailedStats ? "ON" : "OFF")),
            btn -> {
                config.showDetailedStats = !config.showDetailedStats;
                btn.setMessage(Component.literal("Detailed Stats: " + (config.showDetailedStats ? "ON" : "OFF")));
                Main.config.save();
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
        y += spacing + 10;
        
        // DONE BUTTON
        addRenderableWidget(Button.builder(
            Component.literal("Done"),
            btn -> {
                Main.config.save();
                this.minecraft.setScreen(lastScreen);
            }
        ).bounds(centerX, y, buttonWidth, 20).build());
    }
    
    private void applyPreset(int preset) {
        switch (preset) {
            case 0: // ULTRA LOW
                config.maxEntityRenderDistance = 16;
                config.particleLimit = 10;
                config.chunkUpdateBudget = 1;
                config.ultraLowQualityMode = true;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                Minecraft.getInstance().options.renderDistance().set(2);
                break;
            case 1: // LOW
                config.maxEntityRenderDistance = 24;
                config.particleLimit = 50;
                config.chunkUpdateBudget = 2;
                config.ultraLowQualityMode = true;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                Minecraft.getInstance().options.renderDistance().set(4);
                break;
            case 2: // MEDIUM
                config.maxEntityRenderDistance = 32;
                config.particleLimit = 100;
                config.chunkUpdateBudget = 3;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = true;
                config.asyncChunkBuilding = true;
                Minecraft.getInstance().options.renderDistance().set(6);
                break;
            case 3: // HIGH
                config.maxEntityRenderDistance = 48;
                config.particleLimit = 500;
                config.chunkUpdateBudget = 4;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = false;
                config.asyncChunkBuilding = true;
                Minecraft.getInstance().options.renderDistance().set(8);
                break;
            case 4: // ULTRA
                config.maxEntityRenderDistance = 64;
                config.particleLimit = 2000;
                config.chunkUpdateBudget = 5;
                config.ultraLowQualityMode = false;
                config.simplifyBlockModels = false;
                config.asyncChunkBuilding = true;
                Minecraft.getInstance().options.renderDistance().set(12);
                break;
        }
        Main.config.save();
        this.rebuildWidgets();
    }
    
@Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics); // FIXED: Only GuiGraphics parameter
        super.render(graphics, mouseX, mouseY, partialTick);
        
        // Draw FPS in corner
        if (Main.performanceMonitor != null) {
            int fps = Main.performanceMonitor.getFPS();
            graphics.drawString(this.font, "FPS: " + fps, 10, this.height - 20, 0xFFFFFF);
        }
    }
    
    @Override
    public void onClose() {
        Main.config.save();
        this.minecraft.setScreen(lastScreen);
    }
}