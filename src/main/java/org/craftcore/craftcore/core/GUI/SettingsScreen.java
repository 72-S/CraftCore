package org.craftcore.craftcore.core.GUI;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SettingsScreen extends Screen {
    private final Screen parent;
    private SettingsManager settingsManager;

    public SettingsScreen(Screen parent) {
        super(Text.translatable("options.craftcore.title"));
        this.parent = parent;
        this.settingsManager = settingsManager;
    }

    @Override
    protected void init() {
        super.init();

        int xPosition = this.width / 2;
        int yPosition = this.height / 6;
        int doneYPosition = this.height - 35;

        // Create a final array to hold the button
        final ButtonWidget[] buttonHolder = new ButtonWidget[1];

        // Create the button
        buttonHolder[0] = ButtonWidget.builder(getModeText(), buttonClick -> {
                    // Update the mode and the button label when it's clicked
                    settingsManager.setCurrentMode((settingsManager.getCurrentMode() + 1) % 3);
                    buttonHolder[0].setMessage(getModeText());
                })
                .position(xPosition, yPosition)
                .build();

        ButtonWidget DoneButton = ButtonWidget.builder(Text.of("Done"), buttonClick -> {
                    this.client.setScreen(this.parent);
                })
                .position(xPosition - 80 , doneYPosition )
                .build();

        this.addDrawableChild(DoneButton);
        this.addDrawableChild(buttonHolder[0]);
    }

    // Methode zum Abrufen des Textes für den aktuellen Modus
    private Text getModeText() {
        return switch (settingsManager.getCurrentMode()) {
            case 0 -> Text.of("Mode 1");
            case 1 -> Text.of("Mode 2");
            case 2 -> Text.of("Mode 3");
            default -> Text.of("Unknown Mode");
        };
    }
}
