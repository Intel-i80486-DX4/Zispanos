//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

@Info(name = "Fastplace", category = Category.PLAYER, description = "Nullifies block place delay")
public class Fastplace extends Module
{
    private Setting<Boolean> Crystals;
    private Setting<Boolean> XP;
    private Setting<Boolean> Blocks;
    
    public Fastplace() {
        this.Crystals = this.register(Settings.b("Crystals", false));
        this.XP = this.register(Settings.b("XP", false));
        this.Blocks = this.register(Settings.b("Blocks", false));
    }
    
    @Override
    public void onUpdate() {
        if (Fastplace.mc.player.inventory.getCurrentItem().getItem() instanceof ItemExpBottle && this.XP.getValue()) {
            Fastplace.mc.rightClickDelayTimer = 0;
        }
        if (Fastplace.mc.player.inventory.getCurrentItem().getItem() instanceof ItemEndCrystal && this.Crystals.getValue()) {
            Fastplace.mc.rightClickDelayTimer = 0;
        }
        if (Block.getBlockFromItem(Fastplace.mc.player.getHeldItemMainhand().getItem()).getDefaultState().isFullBlock() && this.Blocks.getValue()) {
            Fastplace.mc.rightClickDelayTimer = 0;
        }
    }
}
