//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.entity.*;
import net.minecraft.inventory.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.zeroeightsix.kami.module.modules.gui.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.monster.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ LayerArmorBase.class })
public abstract class MixinLayerArmorBase
{
    @Inject(method = { "renderArmorLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void onRenderArmorLayer(final EntityLivingBase entityLivingBaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final EntityEquipmentSlot slotIn, final CallbackInfo ci) {
        if (ArmourHide.INSTANCE.isEnabled()) {
            if (!ArmourHide.INSTANCE.player.getValue() && entityLivingBaseIn instanceof EntityPlayer) {
                if (!ArmourHide.shouldRenderPiece(slotIn)) {
                    ci.cancel();
                }
            }
            else if (!ArmourHide.INSTANCE.armourstand.getValue() && entityLivingBaseIn instanceof EntityArmorStand) {
                if (!ArmourHide.shouldRenderPiece(slotIn)) {
                    ci.cancel();
                }
            }
            else if (!ArmourHide.INSTANCE.mobs.getValue() && entityLivingBaseIn instanceof EntityMob && !ArmourHide.shouldRenderPiece(slotIn)) {
                ci.cancel();
            }
        }
    }
}
