//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.util.*;
import java.io.*;

class ConstInfoPadding extends ConstInfo
{
    public ConstInfoPadding(final int i) {
        super(i);
    }
    
    public int getTag() {
        return 0;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        return dest.addConstInfoPadding();
    }
    
    public void write(final DataOutputStream out) throws IOException {
    }
    
    public void print(final PrintWriter out) {
        out.println("padding");
    }
}
