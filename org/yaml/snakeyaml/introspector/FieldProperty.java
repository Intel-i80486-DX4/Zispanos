//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.introspector;

import java.lang.reflect.*;
import org.yaml.snakeyaml.error.*;
import java.util.*;
import java.lang.annotation.*;
import org.yaml.snakeyaml.util.*;

public class FieldProperty extends GenericProperty
{
    private final Field field;
    
    public FieldProperty(final Field field) {
        super(field.getName(), (Class)field.getType(), field.getGenericType());
        (this.field = field).setAccessible(true);
    }
    
    public void set(final Object object, final Object value) throws Exception {
        this.field.set(object, value);
    }
    
    public Object get(final Object object) {
        try {
            return this.field.get(object);
        }
        catch (Exception e) {
            throw new YAMLException("Unable to access field " + this.field.getName() + " on object " + object + " : " + e);
        }
    }
    
    public List<Annotation> getAnnotations() {
        return (List<Annotation>)ArrayUtils.toUnmodifiableList((Object[])this.field.getAnnotations());
    }
    
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        return this.field.getAnnotation(annotationType);
    }
}
