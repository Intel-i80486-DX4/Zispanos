//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class StringMemberValue extends MemberValue
{
    int valueIndex;
    
    public StringMemberValue(final int index, final ConstPool cp) {
        super('s', cp);
        this.valueIndex = index;
    }
    
    public StringMemberValue(final String str, final ConstPool cp) {
        super('s', cp);
        this.setValue(str);
    }
    
    public StringMemberValue(final ConstPool cp) {
        super('s', cp);
        this.setValue("");
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return String.class;
    }
    
    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }
    
    public void setValue(final String newValue) {
        this.valueIndex = this.cp.addUtf8Info(newValue);
    }
    
    public String toString() {
        return "\"" + this.getValue() + "\"";
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitStringMemberValue(this);
    }
}
