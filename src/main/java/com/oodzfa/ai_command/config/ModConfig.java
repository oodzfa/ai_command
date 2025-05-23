package com.oodzfa.ai_command.config;

import com.oodzfa.ai_command.Main;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = Main.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("general")
    public String prompt = "从现在开始，你将作为一个 Minecraft 指令助手。你将提供准确的 Minecraft 1.20 单行指令，并且不包含任何解释。";
    @ConfigEntry.Category("general")
    public Long max_tokens = 1024L;
    @ConfigEntry.Category("general")
    public Double temperature = 0.0;
    @ConfigEntry.Category("general")
    public String[] blacklist = new String[] {};
    @ConfigEntry.Category("api")
    public String base_url = "https://api.openai.com/v1";
    @ConfigEntry.Category("api")
    public String api_key = "";
    @ConfigEntry.Category("api")
    public String model = "gpt-3.5-turbo";
}