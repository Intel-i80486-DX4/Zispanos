//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.syntax.parsers;

import net.minecraft.block.*;
import net.minecraft.util.*;
import me.zeroeightsix.kami.command.syntax.*;
import java.util.*;

public class BlockParser extends AbstractParser
{
    private static HashMap<String, Block> blockNames;
    
    public BlockParser() {
        if (!BlockParser.blockNames.isEmpty()) {
            return;
        }
        for (final ResourceLocation resourceLocation : Block.REGISTRY.getKeys()) {
            BlockParser.blockNames.put(resourceLocation.toString().replace("minecraft:", "").replace("_", ""), (Block)Block.REGISTRY.getObject((Object)resourceLocation));
        }
    }
    
    @Override
    public String getChunk(final SyntaxChunk[] chunks, final SyntaxChunk thisChunk, final String[] values, final String chunkValue) {
        try {
            if (chunkValue == null) {
                return (thisChunk.isHeadless() ? "" : thisChunk.getHead()) + (thisChunk.isNecessary() ? "<" : "[") + thisChunk.getType() + (thisChunk.isNecessary() ? ">" : "]");
            }
            final HashMap<String, Block> possibilities = new HashMap<String, Block>();
            for (final String s : BlockParser.blockNames.keySet()) {
                if (s.toLowerCase().startsWith(chunkValue.toLowerCase().replace("minecraft:", "").replace("_", ""))) {
                    possibilities.put(s, BlockParser.blockNames.get(s));
                }
            }
            if (possibilities.isEmpty()) {
                return "";
            }
            final TreeMap<String, Block> p = new TreeMap<String, Block>(possibilities);
            final Map.Entry<String, Block> e = p.firstEntry();
            return e.getKey().substring(chunkValue.length());
        }
        catch (Exception e2) {
            return "";
        }
    }
    
    public static Block getBlockFromName(final String name) {
        if (!BlockParser.blockNames.containsKey(name)) {
            return null;
        }
        return BlockParser.blockNames.get(name);
    }
    
    public static Object getKeyFromValue(final Map hm, final Object value) {
        for (final Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
    
    public static String getNameFromBlock(final Block b) {
        if (!BlockParser.blockNames.containsValue(b)) {
            return null;
        }
        return (String)getKeyFromValue(BlockParser.blockNames, b);
    }
    
    static {
        BlockParser.blockNames = new HashMap<String, Block>();
    }
}
