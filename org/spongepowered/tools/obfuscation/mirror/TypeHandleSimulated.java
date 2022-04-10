//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.tools.obfuscation.mirror;

import javax.lang.model.element.*;
import java.lang.annotation.*;
import org.spongepowered.asm.mixin.injection.struct.*;
import org.spongepowered.asm.obfuscation.mapping.common.*;
import org.spongepowered.asm.util.*;
import javax.lang.model.type.*;
import java.util.*;

public class TypeHandleSimulated extends TypeHandle
{
    private final TypeElement simulatedType;
    
    public TypeHandleSimulated(final String name, final TypeMirror type) {
        this(TypeUtils.getPackage(type), name, type);
    }
    
    public TypeHandleSimulated(final PackageElement pkg, final String name, final TypeMirror type) {
        super(pkg, name);
        this.simulatedType = (TypeElement)((DeclaredType)type).asElement();
    }
    
    @Override
    protected TypeElement getTargetElement() {
        return this.simulatedType;
    }
    
    @Override
    public boolean isPublic() {
        return true;
    }
    
    @Override
    public boolean isImaginary() {
        return false;
    }
    
    @Override
    public boolean isSimulated() {
        return true;
    }
    
    @Override
    public AnnotationHandle getAnnotation(final Class<? extends Annotation> annotationClass) {
        return null;
    }
    
    @Override
    public TypeHandle getSuperclass() {
        return null;
    }
    
    @Override
    public String findDescriptor(final MemberInfo memberInfo) {
        return (memberInfo != null) ? memberInfo.desc : null;
    }
    
    @Override
    public FieldHandle findField(final String name, final String type, final boolean caseSensitive) {
        return new FieldHandle((String)null, name, type);
    }
    
    @Override
    public MethodHandle findMethod(final String name, final String desc, final boolean caseSensitive) {
        return new MethodHandle(null, name, desc);
    }
    
    @Override
    public MappingMethod getMappingMethod(final String name, final String desc) {
        final String signature = new SignaturePrinter(name, desc).setFullyQualified(true).toDescriptor();
        final String rawSignature = TypeUtils.stripGenerics(signature);
        final MethodHandle method = findMethodRecursive(this, name, signature, rawSignature, true);
        return (method != null) ? method.asMapping(true) : super.getMappingMethod(name, desc);
    }
    
    private static MethodHandle findMethodRecursive(final TypeHandle target, final String name, final String signature, final String rawSignature, final boolean matchCase) {
        final TypeElement elem = target.getTargetElement();
        if (elem == null) {
            return null;
        }
        MethodHandle method = TypeHandle.findMethod(target, name, signature, rawSignature, matchCase);
        if (method != null) {
            return method;
        }
        for (final TypeMirror iface : elem.getInterfaces()) {
            method = findMethodRecursive(iface, name, signature, rawSignature, matchCase);
            if (method != null) {
                return method;
            }
        }
        final TypeMirror superClass = elem.getSuperclass();
        if (superClass == null || superClass.getKind() == TypeKind.NONE) {
            return null;
        }
        return findMethodRecursive(superClass, name, signature, rawSignature, matchCase);
    }
    
    private static MethodHandle findMethodRecursive(final TypeMirror target, final String name, final String signature, final String rawSignature, final boolean matchCase) {
        if (!(target instanceof DeclaredType)) {
            return null;
        }
        final TypeElement element = (TypeElement)((DeclaredType)target).asElement();
        return findMethodRecursive(new TypeHandle(element), name, signature, rawSignature, matchCase);
    }
}
