//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.player.*;
import java.util.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "SkinFlicker", description = "Toggle your skin layers rapidly for a cool skin effect", category = Category.HIDDEN)
public class SkinFlicker extends Module
{
    private Setting<FlickerMode> mode;
    private Setting<Integer> slowness;
    private static final EnumPlayerModelParts[] PARTS_HORIZONTAL;
    private static final EnumPlayerModelParts[] PARTS_VERTICAL;
    private Random r;
    private int len;
    
    public SkinFlicker() {
        this.mode = this.register(Settings.e("Mode", FlickerMode.HORIZONTAL));
        this.slowness = this.register((Setting<Integer>)Settings.integerBuilder().withName("Slowness").withValue(2).withMinimum(1).build());
        this.r = new Random();
        this.len = EnumPlayerModelParts.values().length;
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case RANDOM: {
                if (SkinFlicker.mc.player.ticksExisted % this.slowness.getValue() != 0) {
                    return;
                }
                SkinFlicker.mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[this.r.nextInt(this.len)]);
                break;
            }
            case VERTICAL:
            case HORIZONTAL: {
                int i = SkinFlicker.mc.player.ticksExisted / this.slowness.getValue() % (SkinFlicker.PARTS_HORIZONTAL.length * 2);
                boolean on = false;
                if (i >= SkinFlicker.PARTS_HORIZONTAL.length) {
                    on = true;
                    i -= SkinFlicker.PARTS_HORIZONTAL.length;
                }
                SkinFlicker.mc.gameSettings.setModelPartEnabled((this.mode.getValue() == FlickerMode.VERTICAL) ? SkinFlicker.PARTS_VERTICAL[i] : SkinFlicker.PARTS_HORIZONTAL[i], on);
                break;
            }
        }
    }
    
    static {
        PARTS_HORIZONTAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.HAT, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG, EnumPlayerModelParts.RIGHT_SLEEVE };
        PARTS_VERTICAL = new EnumPlayerModelParts[] { EnumPlayerModelParts.HAT, EnumPlayerModelParts.JACKET, EnumPlayerModelParts.LEFT_SLEEVE, EnumPlayerModelParts.RIGHT_SLEEVE, EnumPlayerModelParts.LEFT_PANTS_LEG, EnumPlayerModelParts.RIGHT_PANTS_LEG };
    }
    
    public enum FlickerMode
    {
        HORIZONTAL, 
        VERTICAL, 
        RANDOM;
    }
}
