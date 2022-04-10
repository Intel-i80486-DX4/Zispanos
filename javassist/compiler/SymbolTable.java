//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler;

import java.util.*;
import javassist.compiler.ast.*;

public final class SymbolTable extends HashMap<String, Declarator>
{
    private static final long serialVersionUID = 1L;
    private SymbolTable parent;
    
    public SymbolTable() {
        this(null);
    }
    
    public SymbolTable(final SymbolTable p) {
        this.parent = p;
    }
    
    public SymbolTable getParent() {
        return this.parent;
    }
    
    public Declarator lookup(final String name) {
        final Declarator found = ((HashMap<K, Declarator>)this).get(name);
        if (found == null && this.parent != null) {
            return this.parent.lookup(name);
        }
        return found;
    }
    
    public void append(final String name, final Declarator value) {
        this.put(name, value);
    }
}
