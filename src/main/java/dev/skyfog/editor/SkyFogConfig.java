package dev.skyfog.editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class SkyFogConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("skyfog-editor.json");

    public static boolean enabled = true;
    public static int skyColor = 0x7657D5;
    public static int fogColor = 0x8BC6E8;
    public static float fogStrength = 0.75f;

    private SkyFogConfig() {}

    public static void load() {
        if (!Files.exists(FILE)) return;
        try {
            Data data = GSON.fromJson(Files.readString(FILE), Data.class);
            if (data != null) {
                enabled = data.enabled;
                skyColor = data.skyColor & 0xFFFFFF;
                fogColor = data.fogColor & 0xFFFFFF;
                fogStrength = Math.max(0.0f, Math.min(1.0f, data.fogStrength));
            }
        } catch (Exception ignored) {
            // Keep safe defaults if the config is missing or damaged.
        }
    }

    public static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Data data = new Data();
            data.enabled = enabled;
            data.skyColor = skyColor;
            data.fogColor = fogColor;
            data.fogStrength = fogStrength;
            Files.writeString(FILE, GSON.toJson(data));
        } catch (IOException ignored) {
        }
    }

    private static final class Data {
        boolean enabled = true;
        int skyColor = 0x7657D5;
        int fogColor = 0x8BC6E8;
        float fogStrength = 0.75f;
    }
}
