//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.io.*;
import java.util.*;

public class SyntheticAttribute extends AttributeInfo
{
    public static final String tag = "Synthetic";
    
    SyntheticAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    public SyntheticAttribute(final ConstPool cp) {
        super(cp, "Synthetic", new byte[0]);
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        return new SyntheticAttribute(newCp);
    }
}
