//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.init.*;

@Info(name = "IceSpeed", description = "Changes how slippery ice is", category = Category.MOVEMENT)
public class IceSpeed extends Module
{
    private Setting<Float> slipperiness;
    
    public IceSpeed() {
        this.slipperiness = this.register((Setting<Float>)Settings.floatBuilder("Slipperiness").withMinimum(0.2f).withValue(0.4f).withMaximum(1.0f).build());
    }
    
    @Override
    public void onUpdate() {
        Blocks.ICE.slipperiness = this.slipperiness.getValue();
        Blocks.PACKED_ICE.slipperiness = this.slipperiness.getValue();
        Blocks.FROSTED_ICE.slipperiness = this.slipperiness.getValue();
    }
    
    public void onDisable() {
        Blocks.ICE.slipperiness = 0.98f;
        Blocks.PACKED_ICE.slipperiness = 0.98f;
        Blocks.FROSTED_ICE.slipperiness = 0.98f;
    }
}
