//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class ShortMemberValue extends MemberValue
{
    int valueIndex;
    
    public ShortMemberValue(final int index, final ConstPool cp) {
        super('S', cp);
        this.valueIndex = index;
    }
    
    public ShortMemberValue(final short s, final ConstPool cp) {
        super('S', cp);
        this.setValue(s);
    }
    
    public ShortMemberValue(final ConstPool cp) {
        super('S', cp);
        this.setValue((short)0);
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return Short.TYPE;
    }
    
    public short getValue() {
        return (short)this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final short newValue) {
        this.valueIndex = this.cp.addIntegerInfo((int)newValue);
    }
    
    public String toString() {
        return Short.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitShortMemberValue(this);
    }
}
