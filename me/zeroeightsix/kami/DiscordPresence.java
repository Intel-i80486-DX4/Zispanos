//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami;

import me.zeroeightsix.kami.module.modules.misc.*;
import club.minnced.discord.rpc.*;
import me.zeroeightsix.kami.module.*;

public class DiscordPresence
{
    public static DiscordRichPresence presence;
    private static boolean hasStarted;
    private static final DiscordRPC rpc;
    private static String details;
    private static String state;
    private static DiscordSettings discordSettings;
    
    public static void start() {
        KamiMod.log.info("Starting Discord RPC");
        if (DiscordPresence.hasStarted) {
            return;
        }
        DiscordPresence.hasStarted = true;
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> KamiMod.log.info("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));
        DiscordPresence.rpc.Discord_Initialize("693281610459512844", handlers, true, "");
        DiscordPresence.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        setRpcFromSettings();
        new Thread(DiscordPresence::setRpcFromSettingsNonInt, "Discord-RPC-Callback-Handler").start();
        KamiMod.log.info("Discord RPC initialised successfully");
    }
    
    private static void setRpcFromSettingsNonInt() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                DiscordPresence.rpc.Discord_RunCallbacks();
                DiscordPresence.discordSettings = (DiscordSettings)ModuleManager.getModuleByName("DiscordSettings");
                final String separator = " | ";
                DiscordPresence.details = DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line1Setting.getValue()) + separator + DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line3Setting.getValue());
                DiscordPresence.state = DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line2Setting.getValue()) + separator + DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line4Setting.getValue());
                DiscordPresence.presence.details = DiscordPresence.details;
                DiscordPresence.presence.state = DiscordPresence.state;
                DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                Thread.sleep(4000L);
            }
            catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private static void setRpcFromSettings() {
        DiscordPresence.discordSettings = (DiscordSettings)ModuleManager.getModuleByName("DiscordSettings");
        DiscordPresence.details = DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line1Setting.getValue()) + " " + DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line3Setting.getValue());
        DiscordPresence.state = DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line2Setting.getValue()) + " " + DiscordPresence.discordSettings.getLine((DiscordSettings.LineInfo)DiscordPresence.discordSettings.line4Setting.getValue());
        DiscordPresence.presence.details = DiscordPresence.details;
        DiscordPresence.presence.state = DiscordPresence.state;
        DiscordPresence.presence.largeImageKey = "zispanos";
        DiscordPresence.presence.largeImageText = "Zispanos Backdoor";
        DiscordPresence.rpc.Discord_UpdatePresence(DiscordPresence.presence);
    }
    
    static {
        rpc = DiscordRPC.INSTANCE;
        DiscordPresence.presence = new DiscordRichPresence();
        DiscordPresence.hasStarted = false;
    }
}
