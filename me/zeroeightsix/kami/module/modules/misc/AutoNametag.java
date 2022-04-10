//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import java.util.*;

@Info(name = "AutoNametag", category = Category.MISC)
public class AutoNametag extends Module
{
    private Setting range;
    private ArrayList namedWithers;
    
    public AutoNametag() {
        this.range = this.register(Settings.d("Range", 4.0));
        this.namedWithers = new ArrayList();
    }
    
    public void onEnable() {
        this.namedWithers.clear();
    }
    
    @Override
    public void onUpdate() {
        int tagslot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoNametag.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY && !(stack.getItem() instanceof ItemBlock)) {
                final Item tag = stack.getItem();
                if (tag instanceof ItemNameTag) {
                    tagslot = i;
                }
            }
        }
        if (tagslot == -1) {
            Command.sendChatMessage("[AutoNametag] Error: No nametags in inventory, disabling module");
            this.disable();
        }
        else {
            for (final Entity w : AutoNametag.mc.world.getLoadedEntityList()) {
                if (w instanceof EntityWither) {
                    final EntityWither wither = (EntityWither)w;
                    if (this.namedWithers.contains(wither) || AutoNametag.mc.player.getDistance((Entity)wither) > this.range.getValue() || tagslot == -1) {
                        continue;
                    }
                    AutoNametag.mc.player.inventory.currentItem = tagslot;
                    AutoNametag.mc.playerController.interactWithEntity((EntityPlayer)AutoNametag.mc.player, (Entity)wither, EnumHand.MAIN_HAND);
                    this.namedWithers.add(wither);
                }
            }
        }
    }
}
