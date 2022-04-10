//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.util.*;
import java.io.*;

class FloatInfo extends ConstInfo
{
    static final int tag = 4;
    float value;
    
    public FloatInfo(final float f, final int index) {
        super(index);
        this.value = f;
    }
    
    public FloatInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.value = in.readFloat();
    }
    
    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof FloatInfo && ((FloatInfo)obj).value == this.value;
    }
    
    public int getTag() {
        return 4;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        return dest.addFloatInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(4);
        out.writeFloat(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Float ");
        out.println(this.value);
    }
}
