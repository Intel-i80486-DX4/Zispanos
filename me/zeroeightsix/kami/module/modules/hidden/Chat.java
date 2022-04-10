//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import java.util.*;
import java.text.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.util.text.*;

@Info(name = "Chat", category = Category.HIDDEN, description = "Chat ending", showOnArray = ShowOnArray.OFF)
public class Chat extends Module
{
    public Setting startupGlobal;
    public static Setting changeChatBackground;
    private Setting addSuffix;
    public Setting addTimestamp;
    private Setting textMode;
    private Setting decoMode;
    private Setting commands;
    public static Chat INSTANCE;
    @EventHandler
    public Listener receiveListener;
    @EventHandler
    public Listener listener;
    
    public Chat() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.addSuffix = this.register(Settings.b("Suffix", false));
        this.addTimestamp = this.register(Settings.b("Timestamp", true));
        this.textMode = this.register(Settings.e("Content", TextMode.NAME));
        this.decoMode = this.register(Settings.e("Punctuation", DecoMode.CLASSIC));
        this.commands = this.register(Settings.b("Commands", false));
        this.receiveListener = new Listener(event -> {
            if (this.addTimestamp.getValue()) {
                if (!(event.getPacket() instanceof SPacketChat)) {
                    return;
                }
                final Calendar calendar = Calendar.getInstance();
                final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                final String timestampString = "&7[" + formatter.format(calendar.getTime()) + "]&r ";
                final SPacketChat packet = (SPacketChat)event.getPacket();
                final Command.ChatMessage newMessage = new Command.ChatMessage(timestampString);
                newMessage.appendSibling(packet.getChatComponent());
                packet.chatComponent = (ITextComponent)newMessage;
            }
        }, new Predicate[0]);
        this.listener = new Listener(event -> {
            if (this.addSuffix.getValue() && event.getPacket() instanceof CPacketChatMessage) {
                String s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (!this.commands.getValue()) {
                    if (s.startsWith("/")) {
                        return;
                    }
                    if (s.startsWith(",")) {
                        return;
                    }
                    if (s.startsWith(".")) {
                        return;
                    }
                    if (s.startsWith("-")) {
                        return;
                    }
                    if (s.startsWith(";")) {
                        return;
                    }
                    if (s.startsWith("?")) {
                        return;
                    }
                    if (s.startsWith("*")) {
                        return;
                    }
                    if (s.startsWith("^")) {
                        return;
                    }
                    if (s.startsWith("&")) {
                        return;
                    }
                }
                s += this.getFull(this.decoMode.getValue());
                if (s.length() >= 256) {
                    s = s.substring(0, 256);
                }
                ((CPacketChatMessage)event.getPacket()).message = s;
            }
        }, new Predicate[0]);
        (Chat.INSTANCE = this).register((Setting<Object>)Chat.changeChatBackground);
    }
    
    private String getText(final TextMode t) {
        switch (t) {
            case NAME: {
                return "\uff3a\uff49\uff53\uff50\uff41\uff4e\uff4f\uff53";
            }
            case ONTOP: {
                return "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
            }
            default: {
                return "";
            }
        }
    }
    
    private String getFull(final DecoMode d) {
        switch (d) {
            case NONE: {
                return " " + this.getText(this.textMode.getValue());
            }
            case CLASSIC: {
                return " « " + this.getText(this.textMode.getValue()) + " " + '»';
            }
            case SEPARATOR: {
                return " \u23d0 " + this.getText(this.textMode.getValue());
            }
            default: {
                return "";
            }
        }
    }
    
    static {
        Chat.changeChatBackground = Settings.b("Chat Background", true);
        Chat.INSTANCE = new Chat();
    }
    
    private enum DecoMode
    {
        SEPARATOR, 
        CLASSIC, 
        NONE;
    }
    
    private enum TextMode
    {
        NAME, 
        ONTOP;
    }
}
