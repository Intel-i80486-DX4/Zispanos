//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.chunk.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.util.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ VisGraph.class })
public class MixinVisGraph
{
    @Inject(method = { "getVisibleFacings" }, at = { @At("HEAD") }, cancellable = true)
    public void getVisibleFacings(final CallbackInfoReturnable<Set<EnumFacing>> callbackInfo) {
        if (ModuleManager.isModuleEnabled("Freecam")) {
            callbackInfo.setReturnValue((Object)EnumSet.allOf(EnumFacing.class));
        }
    }
}
