package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.craftcore.craftcore.core.shematic.SchematicManager;

public class GetSchematics {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("getschematics")
                        .executes(GetSchematics::execute));
    }

    private static int execute(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        SchematicManager.getLoadedSchematics().forEach((name, schematic) -> {
            source.sendFeedback(() -> Text.literal("Schematic: " + name), false);
        });
        return 1;
    }
}