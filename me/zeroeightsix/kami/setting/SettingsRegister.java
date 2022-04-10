//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.setting;

import me.zeroeightsix.kami.util.*;
import java.util.*;

public class SettingsRegister
{
    public static final SettingsRegister ROOT;
    public HashMap<String, SettingsRegister> registerHashMap;
    public HashMap<String, Setting> settingHashMap;
    
    public SettingsRegister() {
        this.registerHashMap = new HashMap<String, SettingsRegister>();
        this.settingHashMap = new HashMap<String, Setting>();
    }
    
    public SettingsRegister subregister(final String name) {
        if (this.registerHashMap.containsKey(name)) {
            return this.registerHashMap.get(name);
        }
        final SettingsRegister register = new SettingsRegister();
        this.registerHashMap.put(name, register);
        return register;
    }
    
    private void put(final String name, final Setting setting) {
        this.settingHashMap.put(name, setting);
    }
    
    public static void register(final String name, final Setting setting) {
        final Pair<String, SettingsRegister> pair = dig(name);
        ((SettingsRegister)pair.getValue()).put((String)pair.getKey(), setting);
    }
    
    public Setting getSetting(final String group) {
        return this.settingHashMap.get(group);
    }
    
    public static Setting get(final String group) {
        final Pair<String, SettingsRegister> pair = dig(group);
        return ((SettingsRegister)pair.getValue()).getSetting((String)pair.getKey());
    }
    
    private static Pair<String, SettingsRegister> dig(final String group) {
        SettingsRegister current = SettingsRegister.ROOT;
        final StringTokenizer tokenizer = new StringTokenizer(group, ".");
        String previousToken = null;
        while (tokenizer.hasMoreTokens()) {
            if (previousToken == null) {
                previousToken = tokenizer.nextToken();
            }
            else {
                final String token = tokenizer.nextToken();
                current = current.subregister(previousToken);
                previousToken = token;
            }
        }
        return (Pair<String, SettingsRegister>)new Pair((Object)((previousToken == null) ? "" : previousToken), (Object)current);
    }
    
    static {
        ROOT = new SettingsRegister();
    }
}
