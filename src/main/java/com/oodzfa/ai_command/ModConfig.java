package com.oodzfa.ai_command;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Main.MOD_ID)
public class ModConfig implements ConfigData {
    @ConfigEntry.Category("general")
    String system_prompt = "从现在开始，你将作为一个 Minecraft 指令助手。你将提供准确的 Minecraft 1.20.1 单行指令，并且不包含任何解释。";
    @ConfigEntry.Category("general")
    Long max_tokens = 1024L;
    @ConfigEntry.Category("general")
    Double temperature = 0.0;
    @ConfigEntry.Category("general")
    String[] blacklist = new String[]{};
    @ConfigEntry.Category("api")
    String base_url = "https://api.openai.com/v1";
    @ConfigEntry.Category("api")
    String api_key = "";
    @ConfigEntry.Category("api")
    String model = "gpt-3.5-turbo";
}