//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.reflect;

public class CannotCreateException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public CannotCreateException(final String s) {
        super(s);
    }
    
    public CannotCreateException(final Exception e) {
        super("by " + e.toString());
    }
}
