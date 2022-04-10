//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class DoubleMemberValue extends MemberValue
{
    int valueIndex;
    
    public DoubleMemberValue(final int index, final ConstPool cp) {
        super('D', cp);
        this.valueIndex = index;
    }
    
    public DoubleMemberValue(final double d, final ConstPool cp) {
        super('D', cp);
        this.setValue(d);
    }
    
    public DoubleMemberValue(final ConstPool cp) {
        super('D', cp);
        this.setValue(0.0);
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return Double.TYPE;
    }
    
    public double getValue() {
        return this.cp.getDoubleInfo(this.valueIndex);
    }
    
    public void setValue(final double newValue) {
        this.valueIndex = this.cp.addDoubleInfo(newValue);
    }
    
    public String toString() {
        return Double.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitDoubleMemberValue(this);
    }
}
