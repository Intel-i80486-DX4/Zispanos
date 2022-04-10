//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class BinExpr extends Expr
{
    private static final long serialVersionUID = 1L;
    
    private BinExpr(final int op, final ASTree _head, final ASTList _tail) {
        super(op, _head, _tail);
    }
    
    public static BinExpr makeBin(final int op, final ASTree oprand1, final ASTree oprand2) {
        return new BinExpr(op, oprand1, new ASTList(oprand2));
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atBinExpr(this);
    }
}
