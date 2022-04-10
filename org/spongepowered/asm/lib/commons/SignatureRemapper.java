//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.lib.commons;

import org.spongepowered.asm.lib.signature.*;
import java.util.*;

public class SignatureRemapper extends SignatureVisitor
{
    private final SignatureVisitor v;
    private final Remapper remapper;
    private Stack<String> classNames;
    
    public SignatureRemapper(final SignatureVisitor v, final Remapper remapper) {
        this(327680, v, remapper);
    }
    
    protected SignatureRemapper(final int api, final SignatureVisitor v, final Remapper remapper) {
        super(api);
        this.classNames = new Stack<String>();
        this.v = v;
        this.remapper = remapper;
    }
    
    public void visitClassType(final String name) {
        this.classNames.push(name);
        this.v.visitClassType(this.remapper.mapType(name));
    }
    
    public void visitInnerClassType(final String name) {
        final String outerClassName = this.classNames.pop();
        final String className = outerClassName + '$' + name;
        this.classNames.push(className);
        final String remappedOuter = this.remapper.mapType(outerClassName) + '$';
        final String remappedName = this.remapper.mapType(className);
        final int index = remappedName.startsWith(remappedOuter) ? remappedOuter.length() : (remappedName.lastIndexOf(36) + 1);
        this.v.visitInnerClassType(remappedName.substring(index));
    }
    
    public void visitFormalTypeParameter(final String name) {
        this.v.visitFormalTypeParameter(name);
    }
    
    public void visitTypeVariable(final String name) {
        this.v.visitTypeVariable(name);
    }
    
    public SignatureVisitor visitArrayType() {
        this.v.visitArrayType();
        return this;
    }
    
    public void visitBaseType(final char descriptor) {
        this.v.visitBaseType(descriptor);
    }
    
    public SignatureVisitor visitClassBound() {
        this.v.visitClassBound();
        return this;
    }
    
    public SignatureVisitor visitExceptionType() {
        this.v.visitExceptionType();
        return this;
    }
    
    public SignatureVisitor visitInterface() {
        this.v.visitInterface();
        return this;
    }
    
    public SignatureVisitor visitInterfaceBound() {
        this.v.visitInterfaceBound();
        return this;
    }
    
    public SignatureVisitor visitParameterType() {
        this.v.visitParameterType();
        return this;
    }
    
    public SignatureVisitor visitReturnType() {
        this.v.visitReturnType();
        return this;
    }
    
    public SignatureVisitor visitSuperclass() {
        this.v.visitSuperclass();
        return this;
    }
    
    public void visitTypeArgument() {
        this.v.visitTypeArgument();
    }
    
    public SignatureVisitor visitTypeArgument(final char wildcard) {
        this.v.visitTypeArgument(wildcard);
        return this;
    }
    
    public void visitEnd() {
        this.v.visitEnd();
        this.classNames.pop();
    }
}
