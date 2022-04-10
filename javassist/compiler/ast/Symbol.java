//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class Symbol extends ASTree
{
    private static final long serialVersionUID = 1L;
    protected String identifier;
    
    public Symbol(final String sym) {
        this.identifier = sym;
    }
    
    public String get() {
        return this.identifier;
    }
    
    public String toString() {
        return this.identifier;
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atSymbol(this);
    }
}
