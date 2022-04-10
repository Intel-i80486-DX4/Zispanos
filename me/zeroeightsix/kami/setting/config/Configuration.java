//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting.config;

import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.converter.*;
import java.util.*;
import java.nio.file.*;
import com.google.gson.*;
import java.io.*;
import me.zeroeightsix.kami.*;

public class Configuration
{
    public static JsonObject produceConfig() {
        return produceConfig(SettingsRegister.ROOT);
    }
    
    private static JsonObject produceConfig(final SettingsRegister register) {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<String, SettingsRegister> entry : register.registerHashMap.entrySet()) {
            object.add((String)entry.getKey(), (JsonElement)produceConfig(entry.getValue()));
        }
        for (final Map.Entry<String, Setting> entry2 : register.settingHashMap.entrySet()) {
            final Setting setting = entry2.getValue();
            if (!(setting instanceof Convertable)) {
                continue;
            }
            object.add((String)entry2.getKey(), (JsonElement)((Convertable)setting).converter().convert(setting.getValue()));
        }
        return object;
    }
    
    public static void saveConfiguration(final Path path) throws IOException {
        saveConfiguration(Files.newOutputStream(path, new OpenOption[0]));
    }
    
    public static void saveConfiguration(final OutputStream stream) throws IOException {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        final String json = gson.toJson((JsonElement)produceConfig());
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream));
        writer.write(json);
        writer.close();
    }
    
    public static void loadConfiguration(final Path path) throws IOException {
        final InputStream stream = Files.newInputStream(path, new OpenOption[0]);
        loadConfiguration(stream);
        stream.close();
    }
    
    public static void loadConfiguration(final InputStream stream) {
        try {
            loadConfiguration(new JsonParser().parse((Reader)new InputStreamReader(stream)).getAsJsonObject());
        }
        catch (IllegalStateException e) {
            KamiMod.log.error("KAMI Config malformed: resetting.");
            loadConfiguration(new JsonObject());
        }
    }
    
    public static void loadConfiguration(final JsonObject input) {
        loadConfiguration(SettingsRegister.ROOT, input);
    }
    
    private static void loadConfiguration(final SettingsRegister register, final JsonObject input) {
        for (final Map.Entry<String, JsonElement> entry : input.entrySet()) {
            final String key = entry.getKey();
            final JsonElement element = entry.getValue();
            if (register.registerHashMap.containsKey(key)) {
                loadConfiguration(register.subregister(key), element.getAsJsonObject());
            }
            else {
                final Setting setting = register.getSetting(key);
                if (setting == null) {
                    continue;
                }
                setting.setValue(((Convertable)setting).converter().reverse().convert((Object)element));
            }
        }
    }
}
