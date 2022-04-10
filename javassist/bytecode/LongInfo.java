//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.util.*;
import java.io.*;

class LongInfo extends ConstInfo
{
    static final int tag = 5;
    long value;
    
    public LongInfo(final long l, final int index) {
        super(index);
        this.value = l;
    }
    
    public LongInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.value = in.readLong();
    }
    
    public int hashCode() {
        return (int)(this.value ^ this.value >>> 32);
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof LongInfo && ((LongInfo)obj).value == this.value;
    }
    
    public int getTag() {
        return 5;
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        return dest.addLongInfo(this.value);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(5);
        out.writeLong(this.value);
    }
    
    public void print(final PrintWriter out) {
        out.print("Long ");
        out.println(this.value);
    }
}
