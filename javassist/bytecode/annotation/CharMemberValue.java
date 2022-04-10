//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.annotation;

import javassist.bytecode.*;
import javassist.*;
import java.lang.reflect.*;
import java.io.*;

public class CharMemberValue extends MemberValue
{
    int valueIndex;
    
    public CharMemberValue(final int index, final ConstPool cp) {
        super('C', cp);
        this.valueIndex = index;
    }
    
    public CharMemberValue(final char c, final ConstPool cp) {
        super('C', cp);
        this.setValue(c);
    }
    
    public CharMemberValue(final ConstPool cp) {
        super('C', cp);
        this.setValue('\0');
    }
    
    Object getValue(final ClassLoader cl, final ClassPool cp, final Method m) {
        return this.getValue();
    }
    
    Class<?> getType(final ClassLoader cl) {
        return Character.TYPE;
    }
    
    public char getValue() {
        return (char)this.cp.getIntegerInfo(this.valueIndex);
    }
    
    public void setValue(final char newValue) {
        this.valueIndex = this.cp.addIntegerInfo((int)newValue);
    }
    
    public String toString() {
        return Character.toString(this.getValue());
    }
    
    public void write(final AnnotationsWriter writer) throws IOException {
        writer.constValueIndex(this.getValue());
    }
    
    public void accept(final MemberValueVisitor visitor) {
        visitor.visitCharMemberValue(this);
    }
}
