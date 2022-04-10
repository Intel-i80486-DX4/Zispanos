//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.compiler.ast;

import javassist.compiler.*;

public class InstanceOfExpr extends CastExpr
{
    private static final long serialVersionUID = 1L;
    
    public InstanceOfExpr(final ASTList className, final int dim, final ASTree expr) {
        super(className, dim, expr);
    }
    
    public InstanceOfExpr(final int type, final int dim, final ASTree expr) {
        super(type, dim, expr);
    }
    
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }
    
    public void accept(final Visitor v) throws CompileError {
        v.atInstanceOfExpr(this);
    }
}
