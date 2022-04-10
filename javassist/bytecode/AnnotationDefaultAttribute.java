//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode;

import java.util.*;
import javassist.bytecode.annotation.*;
import java.io.*;

public class AnnotationDefaultAttribute extends AttributeInfo
{
    public static final String tag = "AnnotationDefault";
    
    public AnnotationDefaultAttribute(final ConstPool cp, final byte[] info) {
        super(cp, "AnnotationDefault", info);
    }
    
    public AnnotationDefaultAttribute(final ConstPool cp) {
        this(cp, new byte[] { 0, 0 });
    }
    
    AnnotationDefaultAttribute(final ConstPool cp, final int n, final DataInputStream in) throws IOException {
        super(cp, n, in);
    }
    
    public AttributeInfo copy(final ConstPool newCp, final Map<String, String> classnames) {
        final AnnotationsAttribute.Copier copier = new AnnotationsAttribute.Copier(this.info, this.constPool, newCp, (Map)classnames);
        try {
            copier.memberValue(0);
            return new AnnotationDefaultAttribute(newCp, copier.close());
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    public MemberValue getDefaultValue() {
        try {
            return new AnnotationsAttribute.Parser(this.info, this.constPool).parseMemberValue();
        }
        catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    public void setDefaultValue(final MemberValue value) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final AnnotationsWriter writer = new AnnotationsWriter((OutputStream)output, this.constPool);
        try {
            value.write(writer);
            writer.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.set(output.toByteArray());
    }
    
    public String toString() {
        return this.getDefaultValue().toString();
    }
}
