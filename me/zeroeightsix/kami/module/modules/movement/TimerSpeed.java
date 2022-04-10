//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;

@Info(name = "TimerSpeed", description = "Automatically change your timer to go fast", category = Category.MOVEMENT)
public class TimerSpeed extends Module
{
    private float tickDelay;
    private static float curSpeed;
    private Setting<Float> minimumSpeed;
    private Setting<Float> maxSpeed;
    private Setting<Float> attemptSpeed;
    private Setting<Float> fastSpeed;
    
    public TimerSpeed() {
        this.tickDelay = 0.0f;
        this.minimumSpeed = this.register((Setting<Float>)Settings.floatBuilder("Minimum Speed").withMinimum(0.1f).withMaximum(10.0f).withValue(4.0f).build());
        this.maxSpeed = this.register((Setting<Float>)Settings.floatBuilder("Max Speed").withMinimum(0.1f).withMaximum(10.0f).withValue(7.0f).build());
        this.attemptSpeed = this.register((Setting<Float>)Settings.floatBuilder("Attempt Speed").withMinimum(1.0f).withMaximum(10.0f).withValue(4.2f).build());
        this.fastSpeed = this.register((Setting<Float>)Settings.floatBuilder("Fast Speed").withMinimum(1.0f).withMaximum(10.0f).withValue(5.0f).build());
    }
    
    public static String returnGui() {
        return "" + InfoCalculator.round((double)TimerSpeed.curSpeed, 2);
    }
    
    @Override
    public void onUpdate() {
        if (this.tickDelay == this.minimumSpeed.getValue()) {
            TimerSpeed.curSpeed = this.fastSpeed.getValue();
            TimerSpeed.mc.timer.tickLength = 50.0f / this.fastSpeed.getValue();
        }
        if (this.tickDelay >= this.maxSpeed.getValue()) {
            this.tickDelay = 0.0f;
            TimerSpeed.curSpeed = this.attemptSpeed.getValue();
            TimerSpeed.mc.timer.tickLength = 50.0f / this.attemptSpeed.getValue();
        }
        ++this.tickDelay;
    }
    
    public void onDisable() {
        TimerSpeed.mc.timer.tickLength = 50.0f;
    }
    
    static {
        TimerSpeed.curSpeed = 0.0f;
    }
}
