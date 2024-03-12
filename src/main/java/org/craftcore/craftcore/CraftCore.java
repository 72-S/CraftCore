package org.craftcore.craftcore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.craftcore.craftcore.commands.DeleteSchematic;
import org.craftcore.craftcore.commands.GetSchematic;
import org.craftcore.craftcore.commands.LoadSchematic;
import org.craftcore.craftcore.core.shematic.SchematicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CraftCore implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("CraftCore");
    @Override
    public void onInitialize() {
        LOGGER.info("CraftCore is initializing!");
        SchematicManager.initializeSchematicsFolder();
        registerCommands();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, isEarly) -> {
            LoadSchematic.register(dispatcher);
            GetSchematic.register(dispatcher);
            DeleteSchematic.register(dispatcher);
        });
    }
}