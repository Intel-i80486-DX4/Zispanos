//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.injection.struct;

import org.spongepowered.asm.mixin.transformer.*;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.injection.code.*;
import org.spongepowered.asm.util.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.google.common.base.*;

public class CallbackInjectionInfo extends InjectionInfo
{
    protected CallbackInjectionInfo(final MixinTargetContext mixin, final MethodNode method, final AnnotationNode annotation) {
        super(mixin, method, annotation);
    }
    
    protected Injector parseInjector(final AnnotationNode injectAnnotation) {
        final boolean cancellable = (boolean)Annotations.getValue(injectAnnotation, "cancellable", (Object)Boolean.FALSE);
        final LocalCapture locals = (LocalCapture)Annotations.getValue(injectAnnotation, "locals", (Class)LocalCapture.class, (Enum)LocalCapture.NO_CAPTURE);
        final String identifier = (String)Annotations.getValue(injectAnnotation, "id", (Object)"");
        return new CallbackInjector(this, cancellable, locals, identifier);
    }
    
    public String getSliceId(final String id) {
        return Strings.nullToEmpty(id);
    }
}
