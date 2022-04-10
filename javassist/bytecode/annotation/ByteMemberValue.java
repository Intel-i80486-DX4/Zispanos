//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class ByteMemberValue extends MemberValue
{
    int valueIndex;
    
    public ByteMemberValue(final int index, final ConstPool cp) {
        super('B', cp);
        this.valueIndex = index;
    }
    
    public ByteMemberValue(final byte b, final ConstPool cp) {
        super('B', cp);
        this.setValue(b);
    }
    
    public ByteMemberValue(final ConstPool cp) {
        super('B', cp);
        this.setValue((byte)0);
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return Byte.TYPE;
    }
    
    public byte getValue() {
        return (byte)this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final byte newValue) {
        this.valueIndex = this.cp.addIntegerInfo((int)newValue);
    }
    
    public String toString() {
        return Byte.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitByteMemberValue(this);
    }
}
