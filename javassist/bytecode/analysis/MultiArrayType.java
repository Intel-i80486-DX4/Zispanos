//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.bytecode.analysis;

import javassist.*;

public class MultiArrayType extends Type
{
    private MultiType component;
    private int dims;
    
    public MultiArrayType(final MultiType component, final int dims) {
        super((CtClass)null);
        this.component = component;
        this.dims = dims;
    }
    
    public CtClass getCtClass() {
        final CtClass clazz = this.component.getCtClass();
        if (clazz == null) {
            return null;
        }
        ClassPool pool = clazz.getClassPool();
        if (pool == null) {
            pool = ClassPool.getDefault();
        }
        final String name = this.arrayName(clazz.getName(), this.dims);
        try {
            return pool.get(name);
        }
        catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    boolean popChanged() {
        return this.component.popChanged();
    }
    
    public int getDimensions() {
        return this.dims;
    }
    
    public Type getComponent() {
        return (Type)((this.dims == 1) ? this.component : new MultiArrayType(this.component, this.dims - 1));
    }
    
    public int getSize() {
        return 1;
    }
    
    public boolean isArray() {
        return true;
    }
    
    public boolean isAssignableFrom(final Type type) {
        throw new UnsupportedOperationException("Not implemented");
    }
    
    public boolean isReference() {
        return true;
    }
    
    public boolean isAssignableTo(final Type type) {
        if (eq(type.getCtClass(), Type.OBJECT.getCtClass())) {
            return true;
        }
        if (eq(type.getCtClass(), Type.CLONEABLE.getCtClass())) {
            return true;
        }
        if (eq(type.getCtClass(), Type.SERIALIZABLE.getCtClass())) {
            return true;
        }
        if (!type.isArray()) {
            return false;
        }
        final Type typeRoot = this.getRootComponent(type);
        final int typeDims = type.getDimensions();
        if (typeDims > this.dims) {
            return false;
        }
        if (typeDims < this.dims) {
            return eq(typeRoot.getCtClass(), Type.OBJECT.getCtClass()) || eq(typeRoot.getCtClass(), Type.CLONEABLE.getCtClass()) || eq(typeRoot.getCtClass(), Type.SERIALIZABLE.getCtClass());
        }
        return this.component.isAssignableTo(typeRoot);
    }
    
    public int hashCode() {
        return this.component.hashCode() + this.dims;
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof MultiArrayType)) {
            return false;
        }
        final MultiArrayType multi = (MultiArrayType)o;
        return this.component.equals((Object)multi.component) && this.dims == multi.dims;
    }
    
    public String toString() {
        return this.arrayName(this.component.toString(), this.dims);
    }
}
