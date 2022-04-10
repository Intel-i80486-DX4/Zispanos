//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.util.*;
import java.io.*;

class ModuleInfo extends ConstInfo
{
    static final int tag = 19;
    int name;
    
    public ModuleInfo(final int moduleName, final int index) {
        super(index);
        this.name = moduleName;
    }
    
    public ModuleInfo(final DataInputStream in, final int index) throws IOException {
        super(index);
        this.name = in.readUnsignedShort();
    }
    
    public int hashCode() {
        return this.name;
    }
    
    public boolean equals(final Object obj) {
        return obj instanceof ModuleInfo && ((ModuleInfo)obj).name == this.name;
    }
    
    public int getTag() {
        return 19;
    }
    
    public String getModuleName(final ConstPool cp) {
        return cp.getUtf8Info(this.name);
    }
    
    public int copy(final ConstPool src, final ConstPool dest, final Map<String, String> map) {
        final String moduleName = src.getUtf8Info(this.name);
        final int newName = dest.addUtf8Info(moduleName);
        return dest.addModuleInfo(newName);
    }
    
    public void write(final DataOutputStream out) throws IOException {
        out.writeByte(19);
        out.writeShort(this.name);
    }
    
    public void print(final PrintWriter out) {
        out.print("Module #");
        out.println(this.name);
    }
}
