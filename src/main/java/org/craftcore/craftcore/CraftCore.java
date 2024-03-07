package org.craftcore.craftcore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.craftcore.craftcore.commands.Load;
import org.craftcore.craftcore.commands.LoadShem;
import org.craftcore.craftcore.commands.PasteShem;
import org.craftcore.craftcore.core.ShematicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftCore implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("CraftCore");
    @Override
    public void onInitialize() {
        LOGGER.info("CraftCore is initializing!");
        ShematicManager.initializeShematicsFolder();
        registerCommands();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, isEarly) -> {
            LoadShem.register(dispatcher);
            PasteShem.register(dispatcher);
            Load.register(dispatcher);
        });
    }
}