//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import net.minecraft.util.*;
import net.minecraft.potion.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import me.zeroeightsix.kami.module.modules.misc.*;
import net.minecraft.client.gui.*;
import java.io.*;
import net.minecraft.init.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.resources.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

public class BeaconGui extends GuiBeacon
{
    private static final ResourceLocation BEACON_GUI_TEXTURES;
    public static final Potion[][] EFFECTS_LIST;
    boolean doRenderButtons;
    
    public BeaconGui(final InventoryPlayer playerInventory, final IInventory tileBeaconIn) {
        super(playerInventory, tileBeaconIn);
    }
    
    public void initGui() {
        super.initGui();
        this.doRenderButtons = true;
    }
    
    public void updateScreen() {
        super.updateScreen();
        if (this.doRenderButtons) {
            int id = 20;
            int newY = this.guiTop;
            for (final Potion[] array : BeaconGui.EFFECTS_LIST) {
                final Potion[] pos1 = array;
                for (final Potion potion : array) {
                    final PowerButtonCustom customPotion = new PowerButtonCustom(id, this.guiLeft - 27, newY, potion, 0);
                    this.buttonList.add(customPotion);
                    if (potion == Potion.getPotionById(BeaconSelector.effect)) {
                        customPotion.setSelected(true);
                    }
                    newY += 27;
                    ++id;
                }
            }
        }
    }
    
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button instanceof PowerButtonCustom) {
            final PowerButtonCustom guibeacon$powerbutton = (PowerButtonCustom)button;
            if (guibeacon$powerbutton.isSelected()) {
                return;
            }
            final int i = Potion.getIdFromPotion(guibeacon$powerbutton.effect);
            if (guibeacon$powerbutton.tier < 3) {
                BeaconSelector.effect = i;
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }
    
    static {
        BEACON_GUI_TEXTURES = new ResourceLocation("textures/gui/container/beacon.png");
        EFFECTS_LIST = new Potion[][] { { MobEffects.SPEED, MobEffects.HASTE }, { MobEffects.RESISTANCE, MobEffects.JUMP_BOOST }, { MobEffects.STRENGTH } };
    }
    
    class PowerButtonCustom extends Button
    {
        private final Potion effect;
        private final int tier;
        
        public PowerButtonCustom(final int buttonId, final int x, final int y, final Potion effectIn, final int tierIn) {
            super(buttonId, x, y, GuiContainer.INVENTORY_BACKGROUND, effectIn.getStatusIconIndex() % 8 * 18, 198 + effectIn.getStatusIconIndex() / 8 * 18);
            this.effect = effectIn;
            this.tier = tierIn;
        }
        
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            String s = I18n.format(this.effect.getName(), new Object[0]);
            if (this.tier >= 3 && this.effect != MobEffects.REGENERATION) {
                s += " II";
            }
            BeaconGui.this.drawHoveringText(s, mouseX, mouseY);
        }
    }
    
    @SideOnly(Side.CLIENT)
    static class Button extends GuiButton
    {
        private final ResourceLocation iconTexture;
        private final int iconX;
        private final int iconY;
        private boolean selected;
        
        protected Button(final int buttonId, final int x, final int y, final ResourceLocation iconTextureIn, final int iconXIn, final int iconYIn) {
            super(buttonId, x, y, 22, 22, "");
            this.iconTexture = iconTextureIn;
            this.iconX = iconXIn;
            this.iconY = iconYIn;
        }
        
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float partialTicks) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(BeaconGui.BEACON_GUI_TEXTURES);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = (mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height);
                int j = 0;
                if (!this.enabled) {
                    j += this.width * 2;
                }
                else if (this.selected) {
                    j += this.width * 1;
                }
                else if (this.hovered) {
                    j += this.width * 3;
                }
                this.drawTexturedModalRect(this.x, this.y, j, 219, this.width, this.height);
                if (!BeaconGui.BEACON_GUI_TEXTURES.equals((Object)this.iconTexture)) {
                    mc.getTextureManager().bindTexture(this.iconTexture);
                }
                this.drawTexturedModalRect(this.x + 2, this.y + 2, this.iconX, this.iconY, 18, 18);
            }
        }
        
        public boolean isSelected() {
            return this.selected;
        }
        
        public void setSelected(final boolean selectedIn) {
            this.selected = selectedIn;
        }
    }
}
