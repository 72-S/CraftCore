package org.craftcore.craftcore.core.GUI;

import java.io.*;
import java.nio.file.*;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

public class SettingsManager {

    private static final File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "settings.json");

    private int currentMode = 0;

    public int getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
        saveSettings();
    }

    public void saveSettings() {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        try {
            Files.write(file.toPath(), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
    if (!file.exists()) {
        try {
            file.createNewFile();
            SettingsManager settings = new SettingsManager();
            settings.saveSettings(); // Speichern Sie die Standardwerte in der neu erstellten Datei
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return; // Wenn die Datei aus irgendeinem Grund nicht erstellt werden kann, geben Sie einfach neue Standardwerte zurück
        }
    }

    try {
        String json = new String(Files.readAllBytes(file.toPath()));
        Gson gson = new Gson();
        gson.fromJson(json, SettingsManager.class);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}