//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.util.math.*;

@Info(name = "YawLock", category = Category.PLAYER, description = "Locks your camera yaw")
public class YawLock extends Module
{
    private Setting<Boolean> auto;
    private Setting<Float> yaw;
    private Setting<Integer> slice;
    
    public YawLock() {
        this.auto = this.register(Settings.b("Auto", true));
        this.yaw = this.register(Settings.f("Yaw", 180.0f));
        this.slice = this.register(Settings.i("Slice", 8));
    }
    
    @Override
    public void onUpdate() {
        if (this.slice.getValue() == 0) {
            return;
        }
        if (this.auto.getValue()) {
            final int angle = 360 / this.slice.getValue();
            float yaw = YawLock.mc.player.rotationYaw;
            yaw = (float)(Math.round(yaw / angle) * angle);
            YawLock.mc.player.rotationYaw = yaw;
            if (YawLock.mc.player.isRiding()) {
                YawLock.mc.player.getRidingEntity().rotationYaw = yaw;
            }
        }
        else {
            YawLock.mc.player.rotationYaw = MathHelper.clamp(this.yaw.getValue() - 180.0f, -180.0f, 180.0f);
        }
    }
}
