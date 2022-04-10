//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import java.util.function.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import java.util.*;

@Info(name = "ESP", category = Category.RENDER, description = "Highlights entities")
public class ESP extends Module
{
    private Setting<ESPMode> mode;
    private Setting<Boolean> players;
    private Setting<Boolean> animals;
    private Setting<Boolean> mobs;
    private Setting<Boolean> renderInvis;
    
    public ESP() {
        this.mode = this.register(Settings.e("Mode", ESPMode.RECTANGLE));
        this.players = this.register(Settings.b("Players", true));
        this.animals = this.register(Settings.b("Animals", false));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.renderInvis = this.register(Settings.b("Invisible", false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Wrapper.getMinecraft().getRenderManager().options == null) {
            return;
        }
        switch (this.mode.getValue()) {
            case RECTANGLE: {
                final boolean isThirdPersonFrontal = Wrapper.getMinecraft().getRenderManager().options.thirdPersonView == 2;
                final float viewerYaw = Wrapper.getMinecraft().getRenderManager().playerViewY;
                final boolean b;
                final Vec3d pos;
                final float n;
                final boolean b2;
                ESP.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !entity.isInvisible() || this.renderInvis.getValue()).filter(entity -> ESP.mc.player != entity).map(entity -> entity).filter(entityLivingBase -> !entityLivingBase.isDead).filter(entity -> {
                    if (!this.players.getValue() || !(entity instanceof EntityPlayer)) {
                        if (!(EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue())) {
                            return b;
                        }
                    }
                    return b;
                }).forEach(e -> {
                    GlStateManager.pushMatrix();
                    pos = EntityUtil.getInterpolatedPos((Entity)e, event.getPartialTicks());
                    GlStateManager.translate(pos.x - ESP.mc.getRenderManager().renderPosX, pos.y - ESP.mc.getRenderManager().renderPosY, pos.z - ESP.mc.getRenderManager().renderPosZ);
                    GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-n, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate((float)(b2 ? -1 : 1), 1.0f, 0.0f, 0.0f);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.disableDepth();
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    if (e instanceof EntityPlayer) {
                        GL11.glColor3f(1.0f, 1.0f, 1.0f);
                    }
                    else if (EntityUtil.isPassive((Entity)e)) {
                        GL11.glColor3f(0.11f, 0.9f, 0.11f);
                    }
                    else {
                        GL11.glColor3f(0.9f, 0.1f, 0.1f);
                    }
                    GlStateManager.disableTexture2D();
                    GL11.glLineWidth(2.0f);
                    GL11.glEnable(2848);
                    GL11.glBegin(2);
                    GL11.glVertex2d((double)(-e.width / 2.0f), 0.0);
                    GL11.glVertex2d((double)(-e.width / 2.0f), (double)e.height);
                    GL11.glVertex2d((double)(e.width / 2.0f), (double)e.height);
                    GL11.glVertex2d((double)(e.width / 2.0f), 0.0);
                    GL11.glEnd();
                    GlStateManager.popMatrix();
                    return;
                });
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.disableTexture2D();
                GlStateManager.enableBlend();
                GlStateManager.disableAlpha();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.shadeModel(7425);
                GlStateManager.disableDepth();
                GlStateManager.enableCull();
                GlStateManager.glLineWidth(1.0f);
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                break;
            }
        }
    }
    
    @Override
    public void onUpdate() {
        if (this.mode.getValue().equals(ESPMode.GLOW)) {
            for (final Entity e : ESP.mc.world.loadedEntityList) {
                if (e == null || e.isDead) {
                    return;
                }
                if (e instanceof EntityPlayer && this.players.getValue() && !e.isGlowing()) {
                    e.setGlowing(true);
                }
                else if (e instanceof EntityPlayer && !this.players.getValue() && e.isGlowing()) {
                    e.setGlowing(false);
                }
                if (EntityUtil.isHostileMob(e) && this.mobs.getValue() && !e.isGlowing()) {
                    e.setGlowing(true);
                }
                else if (EntityUtil.isHostileMob(e) && !this.mobs.getValue() && e.isGlowing()) {
                    e.setGlowing(false);
                }
                if (EntityUtil.isPassive(e) && this.animals.getValue() && !e.isGlowing()) {
                    e.setGlowing(true);
                }
                else {
                    if (!EntityUtil.isPassive(e) || this.animals.getValue() || !e.isGlowing()) {
                        continue;
                    }
                    e.setGlowing(false);
                }
            }
        }
    }
    
    public void onDisable() {
        if (this.mode.getValue().equals(ESPMode.GLOW)) {
            for (final Entity e : ESP.mc.world.loadedEntityList) {
                e.setGlowing(false);
            }
            ESP.mc.player.setGlowing(false);
        }
    }
    
    public enum ESPMode
    {
        RECTANGLE, 
        GLOW;
    }
}
