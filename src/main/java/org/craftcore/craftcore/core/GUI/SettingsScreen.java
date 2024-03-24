package org.craftcore.craftcore.core.GUI;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    private final Screen parent;

    public SettingsScreen(Screen parent) {
        super(Text.translatable("options.craftcore.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 20;
        int buttonHeight = 20;
        int xPosition = this.width / 2 + 90; // Platziert den Button direkt neben dem Inventar
        int yPosition = this.height / 2; // Platziert den Button direkt über dem "Destroy Items"-Button
        ButtonWidget button = ButtonWidget.builder(Text.of("TEST"), buttonClick -> {
                    buttonClick.active = false;

                })
                .position(xPosition, yPosition)
                .size(buttonWidth, buttonHeight)
                .build();

        this.addDrawableChild(button);
    }
}
