//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.spongepowered.asm.mixin.transformer;

import java.util.*;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.injection.struct.*;
import org.spongepowered.asm.mixin.transformer.throwables.*;
import org.spongepowered.asm.mixin.refmap.*;
import org.spongepowered.asm.mixin.injection.throwables.*;

class MixinApplicatorInterface extends MixinApplicatorStandard
{
    MixinApplicatorInterface(final TargetClassContext context) {
        super(context);
    }
    
    protected void applyInterfaces(final MixinTargetContext mixin) {
        for (final String interfaceName : mixin.getInterfaces()) {
            if (!this.targetClass.name.equals(interfaceName) && !this.targetClass.interfaces.contains(interfaceName)) {
                this.targetClass.interfaces.add(interfaceName);
                mixin.getTargetClassInfo().addInterface(interfaceName);
            }
        }
    }
    
    protected void applyFields(final MixinTargetContext mixin) {
        for (final Map.Entry<FieldNode, ClassInfo.Field> entry : mixin.getShadowFields()) {
            final FieldNode shadow = entry.getKey();
            this.logger.error("Ignoring redundant @Shadow field {}:{} in {}", new Object[] { shadow.name, shadow.desc, mixin });
        }
        this.mergeNewFields(mixin);
    }
    
    protected void applyInitialisers(final MixinTargetContext mixin) {
    }
    
    protected void prepareInjections(final MixinTargetContext mixin) {
        for (final MethodNode method : this.targetClass.methods) {
            try {
                final InjectionInfo injectInfo = InjectionInfo.parse(mixin, method);
                if (injectInfo != null) {
                    throw new InvalidInterfaceMixinException((IMixinContext)mixin, injectInfo + " is not supported on interface mixin method " + method.name);
                }
                continue;
            }
            catch (InvalidInjectionException ex) {
                final String description = (ex.getInjectionInfo() != null) ? ex.getInjectionInfo().toString() : "Injection";
                throw new InvalidInterfaceMixinException((IMixinContext)mixin, description + " is not supported in interface mixin");
            }
        }
    }
    
    protected void applyInjections(final MixinTargetContext mixin) {
    }
}
