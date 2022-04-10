//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class StringL extends ASTree
{
    private static final long serialVersionUID = 1L;
    protected String text;
    
    public StringL(final String t) {
        this.text = t;
    }
    
    public String get() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return "\"" + this.text + "\"";
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atStringL(this);
    }
}
