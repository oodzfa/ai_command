package com.oodzfa.ai_command.screen;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class OKScreen extends Screen {
    private final String response;
    private final CommandContext<ServerCommandSource> context;

    public OKScreen(String response, CommandContext<ServerCommandSource> context) {
        super(Text.translatable("text.ai_command.screen.confirm"));
        this.response = response;
        this.context = context;
    }

    public ButtonWidget button1;
    public ButtonWidget button2;

    @Override
    protected void init() {
        button1 = ButtonWidget.builder(Text.translatable("text.ai_command.screen.cancel"), button -> this.close())
                .dimensions(width / 2 - 205, 20, 200, 20)
                .build();
        button2 = ButtonWidget.builder(Text.translatable("text.ai_command.screen.ok"), button -> {
            context.getSource().getServer().getCommandManager().executeWithPrefix(context.getSource(), response);
            this.close();
        })
                .dimensions(width / 2 + 5, 20, 200, 20)
                .build();

        addDrawableChild(button1);
        addDrawableChild(button2);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x80000000);
        super.render(context, mouseX, mouseY, delta);
        final MultilineText multilineText = MultilineText.create(textRenderer,
                Text.translatable("text.ai_command.screen.confirm.message", response), width - 20);
        multilineText.drawWithShadow(context, 10, height / 4, 16, 0xffffff);
    }
}