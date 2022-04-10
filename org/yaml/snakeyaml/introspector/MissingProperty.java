//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.introspector;

import java.lang.annotation.*;
import java.util.*;

public class MissingProperty extends Property
{
    public MissingProperty(final String name) {
        super(name, (Class)Object.class);
    }
    
    public Class<?>[] getActualTypeArguments() {
        return (Class<?>[])new Class[0];
    }
    
    public void set(final Object object, final Object value) throws Exception {
    }
    
    public Object get(final Object object) {
        return object;
    }
    
    public List<Annotation> getAnnotations() {
        return Collections.emptyList();
    }
    
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return null;
    }
}
