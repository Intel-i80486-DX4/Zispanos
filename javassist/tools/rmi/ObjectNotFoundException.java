//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.rmi;

public class ObjectNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ObjectNotFoundException(final String name) {
        super(name + " is not exported");
    }
    
    public ObjectNotFoundException(final String name, final Exception e) {
        super(name + " because of " + e.toString());
    }
}
