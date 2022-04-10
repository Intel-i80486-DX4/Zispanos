//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;
import java.util.function.*;
import net.minecraft.network.play.server.*;
import me.zeroeightsix.kami.util.*;

@Info(name = "AutoReply", description = "Automatically replies to messages", category = Category.HIDDEN)
public class AutoReply extends Module
{
    public Setting<Boolean> customMessage;
    public Setting<String> message;
    public Setting<Boolean> customListener;
    public Setting<String> listener;
    public Setting<Boolean> customReplyCommand;
    public Setting<String> replyCommand;
    private String listenerDefault;
    private String replyCommandDefault;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    private static long startTime;
    
    public AutoReply() {
        this.customMessage = this.register(Settings.b("Custom Message", false));
        this.message = this.register(Settings.stringBuilder("Custom Text").withValue("Use &7" + Command.getCommandPrefix() + "autoreply&r to modify this").withConsumer((old, value) -> {}).withVisibility(v -> this.customMessage.getValue()).build());
        this.customListener = this.register(Settings.b("Custom Listener", false));
        this.listener = this.register(Settings.stringBuilder("Custom Listener Name").withValue("unchanged").withConsumer((old, value) -> {}).withVisibility(v -> this.customListener.getValue()).build());
        this.customReplyCommand = this.register(Settings.b("Custom Reply Command", false));
        this.replyCommand = this.register(Settings.stringBuilder("Custom Reply Command").withValue("unchanged").withConsumer((old, value) -> {}).withVisibility(v -> this.customReplyCommand.getValue()).build());
        this.listenerDefault = "whispers:";
        this.replyCommandDefault = "r";
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (event.getPacket() instanceof SPacketChat && ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains(this.listenerDefault) && !((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText().contains(AutoReply.mc.player.getName())) {
                if (this.customMessage.getValue()) {
                    Wrapper.getPlayer().sendChatMessage("/" + this.replyCommandDefault + " " + this.message.getValue());
                }
                else {
                    Wrapper.getPlayer().sendChatMessage("/" + this.replyCommandDefault + " I am currently afk, thanks to KAMI Blue's AutoReply module!");
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.customListener.getValue()) {
            this.listenerDefault = this.listener.getValue();
        }
        else {
            this.listenerDefault = "whispers:";
        }
        if (this.customReplyCommand.getValue()) {
            this.replyCommandDefault = this.replyCommand.getName();
        }
        else {
            this.replyCommandDefault = "r";
        }
        if (AutoReply.startTime == 0L) {
            AutoReply.startTime = System.currentTimeMillis();
        }
        if (AutoReply.startTime + 5000L <= System.currentTimeMillis()) {
            if (this.customListener.getValue() && this.listener.getValue().equalsIgnoreCase("unchanged") && AutoReply.mc.player != null) {
                Command.sendWarningMessage(this.getChatName() + " Warning: In order to use the custom listener, please run the &7" + Command.getCommandPrefix() + "autoreply&r =LISTENERNAME command to change it");
            }
            if (this.customReplyCommand.getValue() && this.replyCommand.getValue().equalsIgnoreCase("unchanged") && AutoReply.mc.player != null) {
                Command.sendWarningMessage(this.getChatName() + " Warning: In order to use the custom reply command, please run the &7" + Command.getCommandPrefix() + "autoreply&r -REPLYCOMMAND command to change it");
            }
            AutoReply.startTime = System.currentTimeMillis();
        }
    }
    
    static {
        AutoReply.startTime = 0L;
    }
}
