package org.craftcore.craftcore.mixins;


import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.craftcore.craftcore.core.GUI.SettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class ScreenButtonMixin extends Screen {

    protected ScreenButtonMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        int xPosition = this.width / 2 + 5;
        int yPosition = this.height / 6 + 12;
        ButtonWidget button = ButtonWidget.builder(Text.of("CraftCore Options"), buttonClick -> {
            this.client.setScreen(new SettingsScreen(this));

                })
                .position(xPosition, yPosition)
                .build();

        this.addDrawableChild(button);}

}
