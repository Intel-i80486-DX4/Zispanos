//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.syntax.parsers;

import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;

public class ValueParser extends AbstractParser
{
    int moduleIndex;
    
    public ValueParser(final int moduleIndex) {
        this.moduleIndex = moduleIndex;
    }
    
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (this.moduleIndex > values.length - 1 || chunkValue == null) {
            return this.getDefaultChunk(thisChunk);
        }
        final String module = values[this.moduleIndex];
        final Module m = ModuleManager.getModuleByName(module);
        if (m == null) {
            return "";
        }
        final HashMap<String, Setting> possibilities = new HashMap<String, Setting>();
        for (final Setting v : m.settingList) {
            if (v.getName().toLowerCase().startsWith(chunkValue.toLowerCase())) {
                possibilities.put(v.getName(), v);
            }
        }
        if (possibilities.isEmpty()) {
            return "";
        }
        final TreeMap<String, Setting> p = new TreeMap<String, Setting>(possibilities);
        final Setting aV = p.firstEntry().getValue();
        return aV.getName().substring(chunkValue.length());
    }
}
