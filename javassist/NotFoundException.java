//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist;

public class NotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public NotFoundException(final String msg) {
        super(msg);
    }
    
    public NotFoundException(final String msg, final Exception e) {
        super(msg + " because of " + e.toString());
    }
}
