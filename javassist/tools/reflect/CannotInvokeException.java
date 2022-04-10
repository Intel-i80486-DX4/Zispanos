//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.reflect;

import java.lang.reflect.*;

public class CannotInvokeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    private Throwable err;
    
    public Throwable getReason() {
        return this.err;
    }
    
    public CannotInvokeException(final String reason) {
        super(reason);
        this.err = null;
    }
    
    public CannotInvokeException(final InvocationTargetException e) {
        super("by " + e.getTargetException().toString());
        this.err = null;
        this.err = e.getTargetException();
    }
    
    public CannotInvokeException(final IllegalAccessException e) {
        super("by " + e.toString());
        this.err = null;
        this.err = e;
    }
    
    public CannotInvokeException(final ClassNotFoundException e) {
        super("by " + e.toString());
        this.err = null;
        this.err = e;
    }
}
