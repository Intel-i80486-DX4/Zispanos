//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.*;

@Info(name = "AutoReconnect", description = "Automatically reconnects after being disconnected", category = Category.MISC, alwaysListening = true, showOnArray = ShowOnArray.OFF)
public class AutoReconnect extends Module
{
    private Setting<Integer> seconds;
    private static ServerData cServer;
    @EventHandler
    public Listener<GuiScreenEvent.Closed> closedListener;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> displayedListener;
    
    public AutoReconnect() {
        this.seconds = this.register((Setting<Integer>)Settings.integerBuilder("Seconds").withValue(5).withMinimum(0).build());
        this.closedListener = (Listener<GuiScreenEvent.Closed>)new Listener(event -> {
            if (event.getScreen() instanceof GuiConnecting) {
                AutoReconnect.cServer = AutoReconnect.mc.currentServerData;
            }
        }, new Predicate[0]);
        this.displayedListener = (Listener<GuiScreenEvent.Displayed>)new Listener(event -> {
            if (this.isEnabled() && event.getScreen() instanceof GuiDisconnected && (AutoReconnect.cServer != null || AutoReconnect.mc.currentServerData != null)) {
                event.setScreen((GuiScreen)new KamiGuiDisconnected((GuiDisconnected)event.getScreen()));
            }
        }, new Predicate[0]);
    }
    
    private class KamiGuiDisconnected extends GuiDisconnected
    {
        int millis;
        long cTime;
        
        public KamiGuiDisconnected(final GuiDisconnected disconnected) {
            super(disconnected.parentScreen, disconnected.reason, disconnected.message);
            this.millis = AutoReconnect.this.seconds.getValue() * 1000;
            this.cTime = System.currentTimeMillis();
        }
        
        public void updateScreen() {
            if (this.millis <= 0) {
                this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(this.parentScreen, this.mc, (AutoReconnect.cServer == null) ? this.mc.currentServerData : AutoReconnect.cServer));
            }
        }
        
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            final long a = System.currentTimeMillis();
            this.millis -= (int)(a - this.cTime);
            this.cTime = a;
            final String s = "Reconnecting in " + Math.max(0.0, Math.floor(this.millis / 100.0) / 10.0) + "s";
            this.fontRenderer.drawString(s, (float)(this.width / 2 - this.fontRenderer.getStringWidth(s) / 2), (float)(this.height - 16), 16777215, true);
        }
    }
}
