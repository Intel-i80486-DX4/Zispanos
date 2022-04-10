//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.io.*;

class FieldrefInfo extends MemberrefInfo
{
    static final int tag = 9;
    
    public FieldrefInfo(final int cindex, final int ntindex, final int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }
    
    public FieldrefInfo(final DataInputStream in, final int thisIndex) throws IOException {
        super(in, thisIndex);
    }
    
    @Override
    public int getTag() {
        return 9;
    }
    
    @Override
    public String getTagName() {
        return "Field";
    }
    
    @Override
    protected int copy2(final ConstPool dest, final int cindex, final int ntindex) {
        return dest.addFieldrefInfo(cindex, ntindex);
    }
}
