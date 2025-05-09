package com.oodzfa.ai_command;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final String MOD_ID = "ai_command";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
        AiCommand.register();
        ServerWorldEvents.LOAD.register((server, world) -> {
            if (world.getRegistryKey().getValue().getPath().equals("overworld")) {
                AiCommand.clear_history();
            }
        });
        LOGGER.info("ai command mod loaded!");
    }
}