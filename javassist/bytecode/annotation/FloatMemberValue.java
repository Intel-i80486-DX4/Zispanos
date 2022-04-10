//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class FloatMemberValue extends MemberValue
{
    int valueIndex;
    
    public FloatMemberValue(final int index, final ConstPool cp) {
        super('F', cp);
        this.valueIndex = index;
    }
    
    public FloatMemberValue(final float f, final ConstPool cp) {
        super('F', cp);
        this.setValue(f);
    }
    
    public FloatMemberValue(final ConstPool cp) {
        super('F', cp);
        this.setValue(0.0f);
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return Float.TYPE;
    }
    
    public float getValue() {
        return this.cp.getFloatInfo(this.valueIndex);
    }
    
    public void setValue(final float newValue) {
        this.valueIndex = this.cp.addFloatInfo(newValue);
    }
    
    public String toString() {
        return Float.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitFloatMemberValue(this);
    }
}
