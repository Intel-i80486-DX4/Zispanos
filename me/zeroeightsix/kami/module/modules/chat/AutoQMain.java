//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import java.text.*;
import java.util.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

@Info(name = "AutoQMain", description = "Automatically does '/queue main' on servers", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF)
public class AutoQMain extends Module
{
    private Setting<Boolean> showWarns;
    private Setting<Boolean> connectionWarning;
    private Setting<Boolean> dimensionWarning;
    private Setting<Double> delay;
    private double delayTime;
    private double oldDelay;
    
    public AutoQMain() {
        this.showWarns = this.register(Settings.b("Show Warnings", true));
        this.connectionWarning = this.register(Settings.b("Connection Warning", true));
        this.dimensionWarning = this.register(Settings.b("Dimension Warning", true));
        this.delay = this.register((Setting<Double>)Settings.doubleBuilder("Wait time").withMinimum(0.2).withValue(7.1).withMaximum(10.0).build());
        this.oldDelay = 0.0;
    }
    
    @Override
    public void onUpdate() {
        if (AutoQMain.mc.player == null) {
            return;
        }
        if (this.oldDelay == 0.0) {
            this.oldDelay = this.delay.getValue();
        }
        else if (this.oldDelay != this.delay.getValue()) {
            this.delayTime = this.delay.getValue();
            this.oldDelay = this.delay.getValue();
        }
        if (this.delayTime <= 0.0) {
            this.delayTime = (int)(this.delay.getValue() * 2400.0);
        }
        else if (this.delayTime > 0.0) {
            --this.delayTime;
            return;
        }
        if (AutoQMain.mc.getCurrentServerData() == null && this.connectionWarning.getValue()) {
            this.sendMessage("&l&6Error: &r&6You are in singleplayer");
            return;
        }
        if (!AutoQMain.mc.getCurrentServerData().serverIP.equalsIgnoreCase("2b2t.org") && this.connectionWarning.getValue()) {
            this.sendMessage("&l&6Warning: &r&6You are not connected to 2b2t.org");
            return;
        }
        if (AutoQMain.mc.player.dimension != 1 && this.dimensionWarning.getValue()) {
            this.sendMessage("&l&6Warning: &r&6You are not in the end. Not running &b/queue main&7.");
            return;
        }
        this.sendQueueMain();
    }
    
    private void sendQueueMain() {
        final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));
        Command.sendChatMessage("&7Run &b/queue main&7 at " + formatter.format(date));
        AutoQMain.mc.playerController.connection.sendPacket((Packet)new CPacketChatMessage("/queue main"));
    }
    
    private void sendMessage(final String message) {
        if (this.showWarns.getValue()) {
            Command.sendWarningMessage(this.getChatName() + message);
        }
    }
    
    public void onDisable() {
        this.delayTime = 0.0;
    }
}
