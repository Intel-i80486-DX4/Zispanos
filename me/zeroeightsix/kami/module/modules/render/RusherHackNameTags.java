//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.gui.font.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.entity.player.*;
import java.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import me.zeroeightsix.kami.util.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.text.*;
import net.minecraft.nbt.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

@Info(name = "RusherHackNameTags", category = Category.RENDER)
public class RusherHackNameTags extends Module
{
    CFontRenderer cFontRenderer;
    private Setting<Boolean> Armor;
    
    public RusherHackNameTags() {
        this.cFontRenderer = new CFontRenderer(new Font("Arial", 0, 18), true, false);
        this.Armor = this.register(Settings.b("Armor", false));
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        for (final EntityPlayer p : RusherHackNameTags.mc.world.playerEntities) {
            if (p != RusherHackNameTags.mc.getRenderViewEntity() && p.isEntityAlive()) {
                final double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * RusherHackNameTags.mc.timer.renderPartialTicks - RusherHackNameTags.mc.getRenderManager().renderPosX;
                final double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * RusherHackNameTags.mc.timer.renderPartialTicks - RusherHackNameTags.mc.getRenderManager().renderPosY;
                final double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * RusherHackNameTags.mc.timer.renderPartialTicks - RusherHackNameTags.mc.getRenderManager().renderPosZ;
                if (p.getName().startsWith("Body #")) {
                    continue;
                }
                this.renderNametag(p, pX, pY, pZ);
            }
        }
    }
    
    private void renderNametag(final EntityPlayer player, final double x, final double y, final double z) {
        final int l4 = 0;
        GL11.glPushMatrix();
        String name = player.getName() + "§a " + MathHelper.ceil(player.getHealth() + player.getAbsorptionAmount());
        name = name.replace(".0", "");
        final float distance = RusherHackNameTags.mc.player.getDistance((Entity)player);
        final float var15 = ((distance / 5.0f <= 2.0f) ? 2.0f : (distance / 5.0f)) * 2.5f;
        final float var16 = 0.016666668f * this.getNametagSize((EntityLivingBase)player);
        GL11.glTranslated((double)(float)x, (float)y + 2.5, (double)(float)z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-RusherHackNameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(RusherHackNameTags.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-var16, -var16, var16);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GL11.glDisable(2929);
        final int width = this.cFontRenderer.getStringWidth(name) / 2;
        this.drawBorderedRect(-width - 2, 10.0, width + 1, 20.0, 0.0, Friends.isFriend(name) ? new Color(0, 130, 130).getRGB() : 8617341, -1);
        this.cFontRenderer.drawString(name, (float)(-width), 11.0f, -1);
        int xOffset = 0;
        for (final ItemStack armourStack : player.inventory.armorInventory) {
            if (armourStack != null) {
                xOffset -= 8;
            }
        }
        if (player.getHeldItemMainhand() != null) {
            xOffset -= 8;
            final Object renderStack = player.getHeldItemMainhand().copy();
            this.renderItem(player, (ItemStack)renderStack, xOffset, -10);
            xOffset += 16;
        }
        for (int index = 3; index >= 0; --index) {
            final ItemStack armourStack2 = (ItemStack)player.inventory.armorInventory.get(index);
            if (armourStack2 != null) {
                final ItemStack renderStack2 = armourStack2.copy();
                this.renderItem(player, renderStack2, xOffset, -10);
                xOffset += 16;
            }
        }
        if (player.getHeldItemOffhand() != null) {
            xOffset += 0;
            final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
            this.renderItem(player, renderOffhand, xOffset, -10);
            xOffset += 8;
        }
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private float getNametagSize(final EntityLivingBase player) {
        final ScaledResolution scaledRes = new ScaledResolution(RusherHackNameTags.mc);
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        return (float)twoDscale + RusherHackNameTags.mc.player.getDistance((Entity)player) / 7.0f;
    }
    
    public void drawBorderRect(final float left, final float top, final float right, final float bottom, final int bcolor, final int icolor, final float f) {
        this.drawGuiRect(left + f, top + f, right - f, bottom - f, icolor);
        this.drawGuiRect(left, top, left + f, bottom, bcolor);
        this.drawGuiRect(left + f, top, right, top + f, bcolor);
        this.drawGuiRect(left + f, bottom - f, right, bottom, bcolor);
        this.drawGuiRect(right - f, top + f, right, bottom - f, bcolor);
    }
    
    private void drawBorderedRect(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        this.enableGL2D();
        this.fakeGuiRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        this.fakeGuiRect(x + width, y, x1 - width, y + width, borderColor);
        this.fakeGuiRect(x, y, x + width, y1, borderColor);
        this.fakeGuiRect(x1 - width, y, x1, y1, borderColor);
        this.fakeGuiRect(x + width, y1 - width, x1 - width, y1, borderColor);
        this.disableGL2D();
    }
    
    private void renderItem(final EntityPlayer player, final ItemStack stack, final int x, final int y) {
        GL11.glPushMatrix();
        GL11.glDepthMask(true);
        GlStateManager.clear(256);
        GlStateManager.disableDepth();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        RusherHackNameTags.mc.getRenderItem().zLevel = -100.0f;
        GlStateManager.scale(1.0f, 1.0f, 0.01f);
        RusherHackNameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y / 2 - 12);
        RusherHackNameTags.mc.getRenderItem().renderItemOverlays(RusherHackNameTags.mc.fontRenderer, stack, x, y / 2 - 12);
        RusherHackNameTags.mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.scale(1.0f, 1.0f, 1.0f);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        this.renderEnchantText(player, stack, x, y - 18);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
    }
    
    private void renderEnchantText(final EntityPlayer player, final ItemStack stack, final int x, final int y) {
        int encY = y - 24;
        int yCount = encY + 5;
        if (stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
            this.cFontRenderer.drawStringWithShadow(stack.getMaxDamage() - stack.getItemDamage() + "§4", x * 2 + 8, y + 26, -1);
        }
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName((int)level).substring(0, 1).toLowerCase();
                encName += level;
                GL11.glPushMatrix();
                GL11.glScalef(0.9f, 0.9f, 0.0f);
                this.cFontRenderer.drawStringWithShadow(encName, x * 2 + 13, yCount, -1);
                GL11.glScalef(1.0f, 1.0f, 1.0f);
                GL11.glPopMatrix();
                encY += 8;
                yCount -= 10;
            }
        }
    }
    
    private void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    private void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    private void drawGuiRect(final double x1, final double y1, final double x2, final double y2, final int color) {
        final float red = (color >> 24 & 0xFF) / 255.0f;
        final float green = (color >> 16 & 0xFF) / 255.0f;
        final float blue = (color >> 8 & 0xFF) / 255.0f;
        final float alpha = (color & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glColor4f(green, blue, alpha, red);
        GL11.glBegin(7);
        GL11.glVertex2d(x2, y1);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x1, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
    }
    
    private void fakeGuiRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
