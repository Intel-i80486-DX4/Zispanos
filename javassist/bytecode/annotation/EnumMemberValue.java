//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.*;
import java.lang.reflect.*;
import javassist.bytecode.*;
import java.io.*;

public class EnumMemberValue extends MemberValue
{
    int typeIndex;
    int valueIndex;
    
    public EnumMemberValue(final int type, final int value, final ConstPool cp) {
        super('e', cp);
        this.typeIndex = type;
        this.valueIndex = value;
    }
    
    public EnumMemberValue(final ConstPool cp) {
        super('e', cp);
        final int n = 0;
        this.valueIndex = n;
        this.typeIndex = n;
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) throws ClassNotFoundException {
        try {
            return this.getType(cl).getField(this.getValue()).get(null);
        }
        catch (NoSuchFieldException e) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
        catch (IllegalAccessException e2) {
            throw new ClassNotFoundException(this.getType() + "." + this.getValue());
        }
    }
    
    Class<?> getType(final ClassLoader cl) throws ClassNotFoundException {
        return (Class<?>)loadClass(cl, this.getType());
    }
    
    public String getType() {
        return Descriptor.toClassName(this.cp.getUtf8Info(this.typeIndex));
    }
    
    public void setType(final String typename) {
        this.typeIndex = this.cp.addUtf8Info(Descriptor.of(typename));
    }
    
    public String getValue() {
        return this.cp.getUtf8Info(this.valueIndex);
    }
    
    public void setValue(final String name) {
        this.valueIndex = this.cp.addUtf8Info(name);
    }
    
    public String toString() {
        return this.getType() + "." + this.getValue();
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.enumConstValue(this.cp.getUtf8Info(this.typeIndex), this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitEnumMemberValue(this);
    }
}
