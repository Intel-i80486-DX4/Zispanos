//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class FieldDecl extends ASTList
{
    private static final long serialVersionUID = 1L;
    
    public FieldDecl(final ASTree _head, final ASTList _tail) {
        super(_head, _tail);
    }
    
    public ASTList getModifiers() {
        return (ASTList)this.getLeft();
    }
    
    public Declarator getDeclarator() {
        return (Declarator)this.tail().head();
    }
    
    public ASTree getInit() {
        return this.sublist(2).head();
    }
    
    @Override
    public void accept(final Visitor v) throws CompileError {
        v.atFieldDecl(this);
    }
}
