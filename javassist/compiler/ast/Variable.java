//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class Variable extends Symbol
{
    private static final long serialVersionUID = 1L;
    protected Declarator declarator;
    
    public Variable(final String sym, final Declarator d) {
        super(sym);
        this.declarator = d;
    }
    
    public Declarator getDeclarator() {
        return this.declarator;
    }
    
    @Override
    public String toString() {
        return this.identifier + ":" + this.declarator.getType();
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atVariable(this);
    }
}
