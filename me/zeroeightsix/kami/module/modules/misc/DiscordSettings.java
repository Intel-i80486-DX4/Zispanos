//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "DiscordSettings", category = Category.MISC, description = "Discord Rich Presence")
public class DiscordSettings extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<Boolean> coordsConfirm;
    public Setting<LineInfo> line1Setting;
    public Setting<LineInfo> line3Setting;
    public Setting<LineInfo> line2Setting;
    public Setting<LineInfo> line4Setting;
    private static long startTime;
    
    public DiscordSettings() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.coordsConfirm = this.register(Settings.b("Coords Confirm", false));
        this.line1Setting = this.register(Settings.e("Line 1 Left", LineInfo.VERSION));
        this.line3Setting = this.register(Settings.e("Line 1 Right", LineInfo.USERNAME));
        this.line2Setting = this.register(Settings.e("Line 2 Left", LineInfo.SERVER_IP));
        this.line4Setting = this.register(Settings.e("Line 2 Right", LineInfo.HEALTH));
    }
    
    public String getLine(final LineInfo line) {
        switch (line) {
            case VERSION: {
                return "0.5beta";
            }
            case WORLD: {
                if (DiscordSettings.mc.isIntegratedServerRunning()) {
                    return "Singleplayer";
                }
                if (DiscordSettings.mc.getCurrentServerData() != null) {
                    return "Multiplayer";
                }
                return "Main Menu";
            }
            case DIMENSION: {
                return InfoCalculator.playerDimension(DiscordSettings.mc);
            }
            case USERNAME: {
                if (DiscordSettings.mc.player != null) {
                    return DiscordSettings.mc.player.getName();
                }
                return DiscordSettings.mc.getSession().getUsername();
            }
            case HEALTH: {
                if (DiscordSettings.mc.player != null) {
                    return (int)DiscordSettings.mc.player.getHealth() + " hp";
                }
                return "No hp";
            }
            case SERVER_IP: {
                if (DiscordSettings.mc.getCurrentServerData() != null) {
                    return DiscordSettings.mc.getCurrentServerData().serverIP;
                }
                if (DiscordSettings.mc.isIntegratedServerRunning()) {
                    return "Offline";
                }
                return "Main Menu";
            }
            case COORDS: {
                if (DiscordSettings.mc.player != null && this.coordsConfirm.getValue()) {
                    return "(" + (int)DiscordSettings.mc.player.posX + " " + (int)DiscordSettings.mc.player.posY + " " + (int)DiscordSettings.mc.player.posZ + ")";
                }
                return "No coords";
            }
            default: {
                return "";
            }
        }
    }
    
    public void onEnable() {
        DiscordPresence.start();
    }
    
    @Override
    public void onUpdate() {
        if (DiscordSettings.startTime == 0L) {
            DiscordSettings.startTime = System.currentTimeMillis();
        }
        if (DiscordSettings.startTime + 10000L <= System.currentTimeMillis()) {
            if ((this.line1Setting.getValue().equals(LineInfo.COORDS) || this.line2Setting.getValue().equals(LineInfo.COORDS) || this.line3Setting.getValue().equals(LineInfo.COORDS) || this.line4Setting.getValue().equals(LineInfo.COORDS)) && !this.coordsConfirm.getValue() && DiscordSettings.mc.player != null) {
                Command.sendWarningMessage(this.getChatName() + " Warning: In order to use the coords option please enable the coords confirmation option. This will display your coords on the discord rpc. Do NOT use this if you do not want your coords displayed");
            }
            DiscordSettings.startTime = System.currentTimeMillis();
        }
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
    
    static {
        DiscordSettings.startTime = 0L;
    }
    
    public enum LineInfo
    {
        VERSION, 
        WORLD, 
        DIMENSION, 
        USERNAME, 
        HEALTH, 
        SERVER_IP, 
        COORDS, 
        NONE;
    }
}
