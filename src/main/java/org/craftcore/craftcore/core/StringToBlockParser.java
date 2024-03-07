package org.craftcore.craftcore.core;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class StringToBlockParser {

    public static BlockState parseBlockState(String blockString, Supplier<Registry<Block>> blockRegistrySupplier) throws CommandSyntaxException {
        // Erstellt einen neuen StringReader für den übergebenen String
        StringReader reader = new StringReader(blockString);

        // Erstellt einen neuen BlockArgumentParser. Du musst anpassen, wie du die Registry erhältst.
        // Beachte, dass der Zugriff auf den Konstruktor nun über den Access Widener möglich ist.
        BlockArgumentParser parser = new BlockArgumentParser(blockRegistrySupplier.get(), reader, false);

        // Führt das Parsing durch. Stelle sicher, dass die Version von parse, die du aufrufen möchtest, öffentlich zugänglich ist oder erweitere den Zugriff entsprechend mit deinem Access Widener.
        parser.parse(false); // Stelle sicher, dass 'false' hier korrekt ist, basierend auf deiner Anforderung, ob Tags erlaubt sein sollen oder nicht.

        // Gibt den geparsten BlockState zurück. Stelle sicher, dass getBlockState öffentlich zugänglich ist, oder verwende Access Widener entsprechend.
        return parser.getBlockState();
    }
}
