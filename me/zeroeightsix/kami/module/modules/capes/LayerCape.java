//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.capes;

import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.entity.*;

public class LayerCape implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    
    public LayerCape(final RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }
    
    public void doRenderLayer(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final ResourceLocation rl = Capes.getCapeResource(player);
        final ItemStack itemstack = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        if (!player.hasPlayerInfo() || player.isInvisible() || !player.isWearing(EnumPlayerModelParts.CAPE) || itemstack.getItem() == Items.ELYTRA || rl == null) {
            return;
        }
        float f9 = 0.14f;
        float f10 = 0.0f;
        if (player.isSneaking()) {
            f9 = 0.1f;
            f10 = 0.09f;
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.playerRenderer.bindTexture(rl);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, f10, f9);
        final double d0 = player.prevChasingPosX + (player.chasingPosX - player.prevChasingPosX) * new Float(partialTicks) - (player.prevPosX + (player.posX - player.prevPosX) * new Float(partialTicks));
        final double d2 = player.prevChasingPosY + (player.chasingPosY - player.prevChasingPosY) * new Float(partialTicks) - (player.prevPosY + (player.posY - player.prevPosY) * new Float(partialTicks));
        final double d3 = player.prevChasingPosZ + (player.chasingPosZ - player.prevChasingPosZ) * new Float(partialTicks) - (player.prevPosZ + (player.posZ - player.prevPosZ) * new Float(partialTicks));
        final float f11 = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        final double d4 = new Float(MathHelper.sin(f11 * 0.01745329f));
        final double d5 = new Float(-MathHelper.cos(f11 * 0.01745329f));
        float f12 = new Double(d2).floatValue() * 10.0f;
        f12 = MathHelper.clamp(f12, 3.0f, 32.0f);
        float f13 = new Double(d0 * d4 + d3 * d5).floatValue() * 100.0f;
        final float f14 = new Double(d0 * d5 - d3 * d4).floatValue() * 100.0f;
        if (f13 < 0.0f) {
            f13 = 0.0f;
        }
        final float f15 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
        f12 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6.0f) * 32.0f * f15;
        if (player.isSneaking()) {
            f12 += 20.0f;
        }
        GlStateManager.rotate(5.0f + f13 / 2.0f + f12, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f14 / 2.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(-f14 / 2.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
        this.playerRenderer.getMainModel().renderCape(0.0625f);
        GlStateManager.popMatrix();
    }
    
    public boolean shouldCombineTextures() {
        return false;
    }
}
