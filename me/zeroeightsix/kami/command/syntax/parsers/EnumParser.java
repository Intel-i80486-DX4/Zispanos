//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.syntax.parsers;

import me.zeroeightsix.kami.command.syntax.*;
import java.util.*;

public class EnumParser extends AbstractParser
{
    String[] modes;
    
    public EnumParser(final String[] modes) {
        this.modes = modes;
    }
    
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        if (chunkValue == null) {
            String s = "";
            for (final String a : this.modes) {
                s = s + a + ":";
            }
            s = s.substring(0, s.length() - 1);
            return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + s + (thisChunk.isNecessary() ? ">" : "]");
        }
        final ArrayList<String> possibilities = new ArrayList<String>();
        for (final String s2 : this.modes) {
            if (s2.toLowerCase().startsWith(chunkValue.toLowerCase())) {
                possibilities.add(s2);
            }
        }
        if (possibilities.isEmpty()) {
            return "";
        }
        Collections.sort(possibilities);
        final String s3 = possibilities.get(0);
        return s3.substring(chunkValue.length());
    }
}
