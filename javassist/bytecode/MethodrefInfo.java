//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.io.*;

class MethodrefInfo extends MemberrefInfo
{
    static final int tag = 10;
    
    public MethodrefInfo(final int cindex, final int ntindex, final int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }
    
    public MethodrefInfo(final DataInputStream in, final int thisIndex) throws IOException {
        super(in, thisIndex);
    }
    
    public int getTag() {
        return 10;
    }
    
    public String getTagName() {
        return "Method";
    }
    
    protected int copy2(final ConstPool dest, final int cindex, final int ntindex) {
        return dest.addMethodrefInfo(cindex, ntindex);
    }
}
