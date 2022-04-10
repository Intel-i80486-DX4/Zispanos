//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "Timer", category = Category.PLAYER, description = "Changes your client tick speed")
public class Timer extends Module
{
    private Setting<Boolean> slow;
    private Setting<Float> tickNormal;
    private Setting<Float> tickSlow;
    
    public Timer() {
        this.slow = this.register(Settings.b("Slow Mode", false));
        this.tickNormal = this.register(Settings.floatBuilder("Tick N").withMinimum(1.0f).withMaximum(10.0f).withValue(2.0f).withVisibility(v -> !this.slow.getValue()).build());
        this.tickSlow = this.register(Settings.floatBuilder("Tick S").withMinimum(1.0f).withMaximum(10.0f).withValue(8.0f).withVisibility(v -> this.slow.getValue()).build());
    }
    
    public void onDisable() {
        Timer.mc.timer.tickLength = 50.0f;
    }
    
    @Override
    public void onUpdate() {
        if (!this.slow.getValue()) {
            Timer.mc.timer.tickLength = 50.0f / this.tickNormal.getValue();
        }
        else {
            Timer.mc.timer.tickLength = 50.0f / (this.tickSlow.getValue() / 10.0f);
        }
    }
}
