//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.gen;

import java.util.*;
import org.spongepowered.asm.lib.tree.*;

public abstract class AccessorGenerator
{
    protected final AccessorInfo info;
    
    public AccessorGenerator(final AccessorInfo info) {
        this.info = info;
    }
    
    protected final MethodNode createMethod(final int maxLocals, final int maxStack) {
        final MethodNode method = this.info.getMethod();
        final MethodNode accessor = new MethodNode(327680, (method.access & 0xFFFFFBFF) | 0x1000, method.name, method.desc, null, null);
        (accessor.visibleAnnotations = new ArrayList<AnnotationNode>()).add(this.info.getAnnotation());
        accessor.maxLocals = maxLocals;
        accessor.maxStack = maxStack;
        return accessor;
    }
    
    public abstract MethodNode generate();
}
