//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.event.events.*;
import java.util.function.*;
import net.minecraft.entity.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.util.math.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraft.init.*;
import java.awt.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.enchantment.*;

@Info(name = "Nametags", description = "Draws descriptive nametags above entities", category = Category.RENDER)
public class Nametags extends Module
{
    private Setting<Boolean> players;
    private Setting<Boolean> animals;
    private Setting<Boolean> mobs;
    private Setting<Double> range;
    private Setting<Float> scale;
    private Setting<Boolean> health;
    private Setting<Boolean> armor;
    RenderItem itemRenderer;
    static final Minecraft mc;
    
    public Nametags() {
        this.players = this.register(Settings.b("Players", true));
        this.animals = this.register(Settings.b("Animals", false));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.range = this.register(Settings.d("Range", 200.0));
        this.scale = this.register((Setting<Float>)Settings.floatBuilder("Scale").withMinimum(0.5f).withMaximum(10.0f).withValue(2.5f).build());
        this.health = this.register(Settings.b("Health", true));
        this.armor = this.register(Settings.b("Armor", true));
        this.itemRenderer = Nametags.mc.getRenderItem();
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Nametags.mc.getRenderManager().options == null) {
            return;
        }
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> (entity instanceof EntityPlayer) ? (this.players.getValue() && Nametags.mc.player != entity) : (EntityUtil.isPassive(entity) ? this.animals.getValue() : ((boolean)this.mobs.getValue()))).filter(entity -> Nametags.mc.player.getDistance(entity) < this.range.getValue()).sorted(Comparator.comparing(entity -> -Nametags.mc.player.getDistance(entity))).forEach(this::drawNametag);
        GlStateManager.disableTexture2D();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
    }
    
    private void drawNametag(final Entity entityIn) {
        GlStateManager.pushMatrix();
        final Vec3d interp = EntityUtil.getInterpolatedRenderPos(entityIn, Nametags.mc.getRenderPartialTicks());
        final float yAdd = entityIn.height + 0.5f - (entityIn.isSneaking() ? 0.25f : 0.0f);
        final double x = interp.x;
        final double y = interp.y + yAdd;
        final double z = interp.z;
        final float viewerYaw = Nametags.mc.getRenderManager().playerViewY;
        final float viewerPitch = Nametags.mc.getRenderManager().playerViewX;
        final boolean isThirdPersonFrontal = Nametags.mc.getRenderManager().options.thirdPersonView == 2;
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate((isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0f, 0.0f, 0.0f);
        final float f = Nametags.mc.player.getDistance(entityIn);
        final float m = f / 8.0f * (float)Math.pow(1.258925437927246, this.scale.getValue());
        GlStateManager.scale(m, m, m);
        final FontRenderer fontRendererIn = Nametags.mc.fontRenderer;
        GlStateManager.scale(-0.025f, -0.025f, 0.025f);
        final String str = entityIn.getName() + (this.health.getValue() ? (" §a" + Math.round(((EntityLivingBase)entityIn).getHealth() + ((entityIn instanceof EntityPlayer) ? ((EntityPlayer)entityIn).getAbsorptionAmount() : 0.0f))) : "");
        final int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture2D();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableDepth();
        GL11.glTranslatef(0.0f, -20.0f, 0.0f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)(-i - 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferbuilder.pos((double)(-i - 1), 19.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferbuilder.pos((double)(i + 1), 19.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        bufferbuilder.pos((double)(i + 1), 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.5f).endVertex();
        tessellator.draw();
        bufferbuilder.begin(2, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)(-i - 1), 8.0, 0.0).color(0.1f, 0.1f, 0.1f, 0.1f).endVertex();
        bufferbuilder.pos((double)(-i - 1), 19.0, 0.0).color(0.1f, 0.1f, 0.1f, 0.1f).endVertex();
        bufferbuilder.pos((double)(i + 1), 19.0, 0.0).color(0.1f, 0.1f, 0.1f, 0.1f).endVertex();
        bufferbuilder.pos((double)(i + 1), 8.0, 0.0).color(0.1f, 0.1f, 0.1f, 0.1f).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        if (!entityIn.isSneaking()) {
            fontRendererIn.drawString(str, -i, 10, (entityIn instanceof EntityPlayer) ? (Friends.isFriend(entityIn.getName()) ? 49151 : 16777215) : 16777215);
        }
        else {
            fontRendererIn.drawString(str, -i, 10, 16755200);
        }
        if (entityIn instanceof EntityPlayer && this.armor.getValue()) {
            this.renderArmor((EntityPlayer)entityIn, 0, -(fontRendererIn.FONT_HEIGHT + 1) - 20);
        }
        GlStateManager.glNormal3f(0.0f, 0.0f, 0.0f);
        GL11.glTranslatef(0.0f, 20.0f, 0.0f);
        GlStateManager.scale(-40.0f, -40.0f, 40.0f);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    public void renderArmor(final EntityPlayer player, int x, final int y) {
        final InventoryPlayer items = player.inventory;
        final ItemStack inHand = player.getHeldItemMainhand();
        final ItemStack boots = items.armorItemInSlot(0);
        final ItemStack leggings = items.armorItemInSlot(1);
        final ItemStack body = items.armorItemInSlot(2);
        final ItemStack helm = items.armorItemInSlot(3);
        final ItemStack offHand = player.getHeldItemOffhand();
        ItemStack[] stuff = null;
        if (inHand != null && offHand != null) {
            stuff = new ItemStack[] { inHand, helm, body, leggings, boots, offHand };
        }
        else if (inHand != null && offHand == null) {
            stuff = new ItemStack[] { inHand, helm, body, leggings, boots };
        }
        else if (inHand == null && offHand != null) {
            stuff = new ItemStack[] { helm, body, leggings, boots, offHand };
        }
        else {
            stuff = new ItemStack[] { helm, body, leggings, boots };
        }
        final List<ItemStack> stacks = new ArrayList<ItemStack>();
        ItemStack[] array;
        for (int length = (array = stuff).length, j = 0; j < length; ++j) {
            final ItemStack i = array[j];
            if (i != null && i.getItem() != null) {
                stacks.add(i);
            }
        }
        final int width = 16 * stacks.size() / 2;
        x -= width;
        GlStateManager.disableDepth();
        for (final ItemStack stack : stacks) {
            this.renderItem(stack, x, y);
            x += 16;
        }
        GlStateManager.enableDepth();
    }
    
    public void renderItem(final ItemStack stack, final int x, int y) {
        final FontRenderer fontRenderer = Nametags.mc.fontRenderer;
        final RenderItem renderItem = Nametags.mc.getRenderItem();
        final EnchantEntry[] enchants = { new EnchantEntry(Enchantments.PROTECTION, "Pro"), new EnchantEntry(Enchantments.THORNS, "Thr"), new EnchantEntry(Enchantments.SHARPNESS, "Sha"), new EnchantEntry(Enchantments.FIRE_ASPECT, "Fia"), new EnchantEntry(Enchantments.KNOCKBACK, "Knb"), new EnchantEntry(Enchantments.UNBREAKING, "Unb"), new EnchantEntry(Enchantments.POWER, "Pow"), new EnchantEntry(Enchantments.FIRE_PROTECTION, "Fpr"), new EnchantEntry(Enchantments.FEATHER_FALLING, "Fea"), new EnchantEntry(Enchantments.BLAST_PROTECTION, "Bla"), new EnchantEntry(Enchantments.PROJECTILE_PROTECTION, "Ppr"), new EnchantEntry(Enchantments.RESPIRATION, "Res"), new EnchantEntry(Enchantments.AQUA_AFFINITY, "Aqu"), new EnchantEntry(Enchantments.DEPTH_STRIDER, "Dep"), new EnchantEntry(Enchantments.FROST_WALKER, "Fro"), new EnchantEntry(Enchantments.BINDING_CURSE, "Bin"), new EnchantEntry(Enchantments.SMITE, "Smi"), new EnchantEntry(Enchantments.BANE_OF_ARTHROPODS, "Ban"), new EnchantEntry(Enchantments.LOOTING, "Loo"), new EnchantEntry(Enchantments.SWEEPING, "Swe"), new EnchantEntry(Enchantments.EFFICIENCY, "Eff"), new EnchantEntry(Enchantments.SILK_TOUCH, "Sil"), new EnchantEntry(Enchantments.FORTUNE, "For"), new EnchantEntry(Enchantments.FLAME, "Fla"), new EnchantEntry(Enchantments.LUCK_OF_THE_SEA, "Luc"), new EnchantEntry(Enchantments.LURE, "Lur"), new EnchantEntry(Enchantments.MENDING, "Men"), new EnchantEntry(Enchantments.VANISHING_CURSE, "Van"), new EnchantEntry(Enchantments.PUNCH, "Pun") };
        GlStateManager.pushMatrix();
        GlStateManager.pushMatrix();
        final float scale1 = 0.3f;
        GlStateManager.translate((float)(x - 3), (float)(y + 8), 0.0f);
        GlStateManager.scale(0.3f, 0.3f, 0.3f);
        GlStateManager.popMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        renderItem.zLevel = -100.0f;
        GlStateManager.disableDepth();
        renderItem.renderItemIntoGUI(stack, x, y);
        renderItem.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, (String)null);
        GlStateManager.enableDepth();
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        if (stack.isItemStackDamageable()) {
            this.drawDamage(stack, x, y);
        }
        GlStateManager.scale(1.33f, 1.33f, 1.33f);
        EnchantEntry[] array;
        for (int length = (array = enchants).length, i = 0; i < length; ++i) {
            final EnchantEntry enchant = array[i];
            final int level = EnchantmentHelper.getEnchantmentLevel(enchant.getEnchant(), stack);
            String levelDisplay = "" + level;
            if (level > 10) {
                levelDisplay = "10+";
            }
            if (level > 0) {
                final float scale2 = 0.32f;
                GlStateManager.translate((float)(x - 1), (float)(y + 2), 0.0f);
                GlStateManager.scale(0.42f, 0.42f, 0.42f);
                GlStateManager.disableDepth();
                GlStateManager.disableLighting();
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                fontRenderer.drawString("§f" + enchant.getName() + " " + levelDisplay, (float)(20 - fontRenderer.getStringWidth("§f" + enchant.getName() + " " + levelDisplay) / 2), 0.0f, Color.WHITE.getRGB(), true);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.scale(2.42f, 2.42f, 2.42f);
                GlStateManager.translate((float)(-x + 1), (float)(-y), 0.0f);
                y += (int)((fontRenderer.FONT_HEIGHT + 3) * 0.28f);
            }
        }
        renderItem.zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
    
    public void drawDamage(final ItemStack itemstack, final int x, final int y) {
        final float green = (itemstack.getMaxDamage() - (float)itemstack.getItemDamage()) / itemstack.getMaxDamage();
        final float red = 1.0f - green;
        final int dmg = 100 - (int)(red * 100.0f);
        GlStateManager.disableDepth();
        Nametags.mc.fontRenderer.drawStringWithShadow(dmg + "", x + 8 - Nametags.mc.fontRenderer.getStringWidth(dmg + "") / 2.0f, (float)(y - 11), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
        GlStateManager.enableDepth();
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public static class EnchantEntry
    {
        private Enchantment enchant;
        private String name;
        
        public EnchantEntry(final Enchantment enchant, final String name) {
            this.enchant = enchant;
            this.name = name;
        }
        
        public Enchantment getEnchant() {
            return this.enchant;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
