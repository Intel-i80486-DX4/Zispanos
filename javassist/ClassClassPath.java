//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist;

import java.io.*;
import java.net.*;

public class ClassClassPath implements ClassPath
{
    private Class<?> thisClass;
    
    public ClassClassPath(final Class<?> c) {
        this.thisClass = c;
    }
    
    ClassClassPath() {
        this(Object.class);
    }
    
    @Override
    public InputStream openClassfile(final String classname) throws NotFoundException {
        final String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResourceAsStream(filename);
    }
    
    @Override
    public URL find(final String classname) {
        final String filename = '/' + classname.replace('.', '/') + ".class";
        return this.thisClass.getResource(filename);
    }
    
    @Override
    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}
