//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.rmi;

import java.io.*;

public class RemoteRef implements Serializable
{
    private static final long serialVersionUID = 1L;
    public int oid;
    public String classname;
    
    public RemoteRef(final int i) {
        this.oid = i;
        this.classname = null;
    }
    
    public RemoteRef(final int i, final String name) {
        this.oid = i;
        this.classname = name;
    }
}
