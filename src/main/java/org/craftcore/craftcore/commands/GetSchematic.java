package org.craftcore.craftcore.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.craftcore.craftcore.core.Position.PositionToStringParser;
import org.craftcore.craftcore.core.shematic.SchematicManager;

import java.util.concurrent.CompletableFuture;

public class GetSchematic {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("getschematics")
                        .then(
                                CommandManager.argument("schematicCustomName", StringArgumentType.string())
                                        .suggests(GetSchematic::getCustomNameSuggestions)
                                        .executes(context -> execute(context, StringArgumentType.getString(context, "schematicCustomName")))));

    }

    private static CompletableFuture<Suggestions> getCustomNameSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        SchematicManager.schematicInfos.values().forEach(schematicInfo -> builder.suggest(schematicInfo.customName));
        return builder.buildFuture();
    }

    private static int execute(CommandContext<ServerCommandSource> context, String schematicCustomName) {
        ServerCommandSource source = context.getSource();
        SchematicManager.SchematicInfo schematicInfo = SchematicManager.schematicInfos.values().stream()
                .filter(info -> info.customName.equals(schematicCustomName))
                .findFirst()
                .orElse(null);
        if (schematicInfo != null) {
            String schematicName = schematicInfo.name;
            String schematicId = schematicInfo.id.toString();
            String schematicPosition = PositionToStringParser.parsePosition(schematicInfo.position);
            String type = schematicInfo.type.toString();
            source.sendFeedback(() -> Text.literal("Schematic Name: " + schematicName + " Schematic UUID: " + schematicId + " Schematic Custom Name: " + schematicCustomName + " Schematic Position: " + schematicPosition + "Type: " + type), false);
        } else {
            source.sendError(Text.literal("No schematic found with custom name: " + schematicCustomName));
        }
        return 1;
    }
}