//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.gui;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.inventory.*;

@Info(name = "ArmourHide", category = Category.GUI, description = "Hides the armour on selected entities", showOnArray = ShowOnArray.OFF)
public class ArmourHide extends Module
{
    public Setting<Boolean> player;
    public Setting<Boolean> armourstand;
    public Setting<Boolean> mobs;
    public Setting<Boolean> helmet;
    public Setting<Boolean> chestplate;
    public Setting<Boolean> leggins;
    public Setting<Boolean> boots;
    public static ArmourHide INSTANCE;
    
    public ArmourHide() {
        this.player = this.register(Settings.b("Players", false));
        this.armourstand = this.register(Settings.b("Armour Stands", true));
        this.mobs = this.register(Settings.b("Mobs", true));
        this.helmet = this.register(Settings.b("Helmet", false));
        this.chestplate = this.register(Settings.b("Chestplate", false));
        this.leggins = this.register(Settings.b("Leggings", false));
        this.boots = this.register(Settings.b("Boots", false));
        ArmourHide.INSTANCE = this;
    }
    
    public static boolean shouldRenderPiece(final EntityEquipmentSlot slotIn) {
        return (slotIn == EntityEquipmentSlot.HEAD && ArmourHide.INSTANCE.helmet.getValue()) || (slotIn == EntityEquipmentSlot.CHEST && ArmourHide.INSTANCE.chestplate.getValue()) || (slotIn == EntityEquipmentSlot.LEGS && ArmourHide.INSTANCE.leggins.getValue()) || (slotIn == EntityEquipmentSlot.FEET && ArmourHide.INSTANCE.boots.getValue());
    }
}
