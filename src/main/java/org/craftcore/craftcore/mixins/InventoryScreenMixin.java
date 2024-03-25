package org.craftcore.craftcore.mixins;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider{

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "init", at = @At("TAIL"))
protected void init(CallbackInfo info) {
    int buttonWidth = 20;
    int buttonHeight = 20;
    int xPosition = this.width / 2 + 90; // Platziert den Button direkt neben dem Inventar
    int yPosition = this.height / 2; // Platziert den Button direkt über dem "Destroy Items"-Button
    ButtonWidget button = ButtonWidget.builder(Text.of("S"), buttonClick -> {
        sortInventory();
        buttonClick.isHovered();

    })
    .position(xPosition, yPosition)
    .size(buttonWidth, buttonHeight)
    .build();

    this.addDrawableChild(button);
}

@Unique
private void sortInventory() {
    assert Objects.requireNonNull(this.client).player != null;
    assert this.client.player != null;
    PlayerInventory inv = this.client.player.getInventory();

    // Erstellt eine Liste aus dem Inhalt des Inventars
    List<ItemStack> items = new ArrayList<>();
    for (int i = 0; i < inv.size(); i++) {
        items.add(inv.getStack(i));
    }

    // Sortiert die Liste basierend auf dem Item-Namen
    items.sort(Comparator.comparing(item -> item.getItem().getName().getString()));

    // Setzt die sortierten Items zurück ins Inventar
    for (int i = 0; i < items.size(); i++) {
        inv.setStack(i, items.get(i));
    }
}
}