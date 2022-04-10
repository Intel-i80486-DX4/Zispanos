//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.io.*;
import java.util.*;

public class NestHostAttribute extends AttributeInfo
{
    public static final String tag = "NestHost";
    
    NestHostAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    private NestHostAttribute(final ConstPool cp, final int hostIndex) {
        super(cp, "NestHost", new byte[2]);
        ByteArray.write16bit(hostIndex, this.get(), 0);
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        final int hostIndex = ByteArray.readU16bit(this.get(), 0);
        final int newHostIndex = this.getConstPool().copy(hostIndex, newCp, (Map)classnames);
        return new NestHostAttribute(newCp, newHostIndex);
    }
    
    public int hostClassIndex() {
        return ByteArray.readU16bit(this.info, 0);
    }
}
