//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;

@Info(name = "CustomChat", category = Category.CHAT, description = "Add a custom suffix to the end of your message!", showOnArray = ShowOnArray.OFF)
public class CustomChat extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<TextMode> textMode;
    private Setting<DecoMode> decoMode;
    private Setting<Boolean> commands;
    public Setting<String> customText;
    public static String[] cmdCheck;
    @EventHandler
    public Listener<PacketEvent.Send> listener;
    private static long startTime;
    
    public CustomChat() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        this.textMode = this.register(Settings.e("Message", TextMode.ON_TOP));
        this.decoMode = this.register(Settings.e("Separator", DecoMode.NONE));
        this.commands = this.register(Settings.b("Commands", false));
        this.customText = this.register(Settings.stringBuilder("Custom Text").withValue("unchanged").withConsumer((old, value) -> {}).build());
        this.listener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                String s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (!this.commands.getValue() && this.isCommand(s)) {
                    return;
                }
                s += this.getFull(this.decoMode.getValue());
                if (s.length() >= 256) {
                    s = s.substring(0, 256);
                }
                ((CPacketChatMessage)event.getPacket()).message = s;
            }
        }, new Predicate[0]);
    }
    
    private String getText(final TextMode t) {
        switch (t) {
            case NAME: {
                return "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
            }
            case ON_TOP: {
                return "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
            }
            case WEBSITE: {
                return "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
            }
            case JAPANESE: {
                return "\u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
            }
            case CUSTOM: {
                return this.customText.getValue();
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
            case RIGHTSEPARATOR: {
                return " » " + this.getText(this.textMode.getValue());
            }
            default: {
                return "";
            }
        }
    }
    
    private boolean isCommand(final String s) {
        for (final String value : CustomChat.cmdCheck) {
            if (s.startsWith(value)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if (CustomChat.startTime == 0L) {
            CustomChat.startTime = System.currentTimeMillis();
        }
        if (CustomChat.startTime + 5000L <= System.currentTimeMillis()) {
            if (this.textMode.getValue().equals(TextMode.CUSTOM) && this.customText.getValue().equalsIgnoreCase("unchanged") && CustomChat.mc.player != null) {
                Command.sendWarningMessage(this.getChatName() + " Warning: In order to use the custom " + this.getName() + ", please run the &7" + Command.getCommandPrefix() + "customchat&r command to change it");
            }
            CustomChat.startTime = System.currentTimeMillis();
        }
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
    
    static {
        CustomChat.cmdCheck = new String[] { "/", ",", ".", "-", ";", "?", "*", "^", "&", Command.getCommandPrefix() };
        CustomChat.startTime = 0L;
    }
    
    private enum DecoMode
    {
        SEPARATOR, 
        CLASSIC, 
        NONE, 
        RIGHTSEPARATOR;
    }
    
    public enum TextMode
    {
        NAME, 
        ON_TOP, 
        WEBSITE, 
        JAPANESE, 
        CUSTOM;
    }
}
