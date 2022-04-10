//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler;

import java.util.*;

public final class KeywordTable extends HashMap<String, Integer>
{
    private static final long serialVersionUID = 1L;
    
    public int lookup(final String name) {
        return this.containsKey(name) ? ((HashMap<K, Integer>)this).get(name) : -1;
    }
    
    public void append(final String name, final int t) {
        this.put(name, t);
    }
}
