package com.oodzfa.ai_command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import java.util.concurrent.CompletableFuture;

public final class AiCommand {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    private static ChatCompletionCreateParams.Builder paramsBuilder = null;
    private static Boolean completed = true;

    public static void clear_history() {
        paramsBuilder = ChatCompletionCreateParams.builder().addSystemMessage(config.system_prompt);
        Main.LOGGER.info("ai command history cleared!");
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("ai_command")
                    .requires(source -> source.hasPermissionLevel(2))
                    .then(CommandManager.argument("message", StringArgumentType.greedyString())
                            .executes(context -> {
                                if (!completed) {
                                    context.getSource().sendMessage(Text.translatable("text.ai_command.message.waiting"));
                                    return Command.SINGLE_SUCCESS;
                                }
                                if (StringArgumentType.getString(context, "message").equals("!clear")) {
                                    clear_history();
                                    context.getSource().sendMessage(Text.translatable("text.ai_command.message.cleared"));
                                    return Command.SINGLE_SUCCESS;
                                }
                                completed = false;
                                Main.LOGGER.info("user input: {}", StringArgumentType.getString(context, "message"));
                                paramsBuilder.model(config.model);
                                paramsBuilder.maxCompletionTokens(config.max_tokens);
                                paramsBuilder.temperature(config.temperature);
                                paramsBuilder.addUserMessage(StringArgumentType.getString(context, "message"));
                                ChatCompletionCreateParams params = paramsBuilder.build();
                                OpenAIClient client = new OpenAIOkHttpClient.Builder().baseUrl(config.base_url).apiKey(config.api_key).build();
                                CompletableFuture.runAsync(() -> {
                                    try {
                                        ChatCompletion chatCompletion = client.chat().completions().create(params);
                                        String response = chatCompletion.choices().get(0).message().content().orElse("error!");
                                        for (String keyword : config.blacklist) {
                                            if (response.contains(keyword)) {
                                                Main.LOGGER.error("block ai command response!");
                                                context.getSource().sendError(Text.translatable("text.ai_command.message.blacklist"));
                                                return;
                                            }
                                        }
                                        Main.LOGGER.info("ai command response: {}", response.replaceAll("\n", "\\n"));
                                        context.getSource().sendMessage(Text.translatable("text.ai_command.message.response", response));
                                        context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource(), response);
                                    } catch (Exception e) {
                                        context.getSource().sendError(Text.translatable("text.ai_command.message.exception", e.getMessage()));
                                        e.printStackTrace();
                                    } finally {
                                        completed = true;
                                    }
                                });
                                return Command.SINGLE_SUCCESS;
                            })
                    ));
        });
    }
}