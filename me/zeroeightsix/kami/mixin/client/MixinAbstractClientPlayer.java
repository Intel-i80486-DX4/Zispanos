//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.util.*;
import me.zeroeightsix.kami.module.modules.capes.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ AbstractClientPlayer.class })
public class MixinAbstractClientPlayer
{
    @Inject(method = { "getLocationCape" }, at = { @At("RETURN") }, cancellable = true)
    public void getCape(final CallbackInfoReturnable<ResourceLocation> callbackInfo) {
        if (Capes.INSTANCE == null) {
            return;
        }
        if (!Capes.INSTANCE.overrideOtherCapes.getValue() && callbackInfo.getReturnValue() != null) {
            return;
        }
        final ResourceLocation kamiCape = Capes.getCapeResource((AbstractClientPlayer)this);
        if (kamiCape != null) {
            callbackInfo.setReturnValue((Object)kamiCape);
        }
    }
}
