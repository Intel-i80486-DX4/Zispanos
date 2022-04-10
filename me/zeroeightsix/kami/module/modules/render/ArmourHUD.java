//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import net.minecraft.client.*;

@Info(name = "ArmourHUD", category = Category.GUI, showOnArray = ShowOnArray.OFF, description = "Displays your armour and it's durability on screen")
public class ArmourHUD extends Module
{
    private static RenderItem itemRender;
    private Setting<Boolean> damage;
    
    public ArmourHUD() {
        this.damage = this.register(Settings.b("Damage", false));
    }
    
    private NonNullList<ItemStack> getArmour() {
        if (ArmourHUD.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE) || ArmourHUD.mc.playerController.getCurrentGameType().equals((Object)GameType.SPECTATOR)) {
            return (NonNullList<ItemStack>)NonNullList.withSize(4, (Object)ItemStack.EMPTY);
        }
        return (NonNullList<ItemStack>)ArmourHUD.mc.player.inventory.armorInventory;
    }
    
    @Override
    public void onRender() {
        GlStateManager.enableTexture2D();
        final ScaledResolution resolution = new ScaledResolution(ArmourHUD.mc);
        final int i = resolution.getScaledWidth() / 2;
        int iteration = 0;
        final int y = resolution.getScaledHeight() - 55 - (ArmourHUD.mc.player.isInWater() ? 10 : 0);
        for (final ItemStack is : this.getArmour()) {
            ++iteration;
            if (is.isEmpty()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            ArmourHUD.itemRender.zLevel = 200.0f;
            ArmourHUD.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            ArmourHUD.itemRender.renderItemOverlayIntoGUI(ArmourHUD.mc.fontRenderer, is, x, y, "");
            ArmourHUD.itemRender.zLevel = 0.0f;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            final String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
            ArmourHUD.mc.fontRenderer.drawStringWithShadow(s, (float)(x + 19 - 2 - ArmourHUD.mc.fontRenderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!this.damage.getValue()) {
                continue;
            }
            final float green = (is.getMaxDamage() - (float)is.getItemDamage()) / is.getMaxDamage();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            ArmourHUD.mc.fontRenderer.drawStringWithShadow(dmg + "", (float)(x + 8 - ArmourHUD.mc.fontRenderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColourHolder.toHex((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }
    
    static {
        ArmourHUD.itemRender = Minecraft.getMinecraft().getRenderItem();
    }
}
