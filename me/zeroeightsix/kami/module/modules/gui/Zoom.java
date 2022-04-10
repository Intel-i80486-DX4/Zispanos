//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.gui;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "Zoom", category = Category.GUI, description = "Configures FOV", showOnArray = ShowOnArray.OFF)
public class Zoom extends Module
{
    private float fov;
    private float sensi;
    private Setting<Integer> fovChange;
    private Setting<Float> sensChange;
    private Setting<Boolean> smoothCamera;
    private Setting<Boolean> sens;
    
    public Zoom() {
        this.fov = 0.0f;
        this.sensi = 0.0f;
        this.fovChange = this.register((Setting<Integer>)Settings.integerBuilder("FOV").withMinimum(30).withValue(30).withMaximum(150).build());
        this.sensChange = this.register((Setting<Float>)Settings.floatBuilder("Sensitivity").withMinimum(0.25f).withValue(1.3f).withMaximum(2.0f).build());
        this.smoothCamera = this.register(Settings.b("Cinematic Camera", true));
        this.sens = this.register(Settings.b("Sensitivity", true));
    }
    
    public void onEnable() {
        if (Zoom.mc.player == null) {
            return;
        }
        this.fov = Zoom.mc.gameSettings.fovSetting;
        this.sensi = Zoom.mc.gameSettings.mouseSensitivity;
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = true;
        }
    }
    
    public void onDisable() {
        Zoom.mc.gameSettings.fovSetting = this.fov;
        Zoom.mc.gameSettings.mouseSensitivity = this.sensi;
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = false;
        }
    }
    
    @Override
    public void onUpdate() {
        if (Zoom.mc.player == null) {
            return;
        }
        Zoom.mc.gameSettings.fovSetting = this.fovChange.getValue();
        if (this.smoothCamera.getValue()) {
            Zoom.mc.gameSettings.smoothCamera = true;
        }
        else {
            Zoom.mc.gameSettings.smoothCamera = false;
        }
        if (this.sens.getValue()) {
            Zoom.mc.gameSettings.mouseSensitivity = this.sensi * this.sensChange.getValue();
        }
    }
}
