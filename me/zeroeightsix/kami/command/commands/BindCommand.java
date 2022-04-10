//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.parsers.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;

public class BindCommand extends Command
{
    public static Setting<Boolean> modifiersEnabled;
    
    public BindCommand() {
        super("bind", new ChunkBuilder().append("[module]|modifiers", true, new ModuleParser()).append("[key]|[on|off]", true).build(), new String[0]);
        this.setDescription("Binds a module to a key, or allows you to change modifier options");
    }
    
    public void call(final String[] args) {
        if (args.length == 1) {
            Command.sendChatMessage("Please specify a module.");
            return;
        }
        final String module = args[0];
        final String rkey = args[1];
        if (module.equalsIgnoreCase("modifiers")) {
            if (rkey == null) {
                sendChatMessage("Expected: on or off");
                return;
            }
            if (rkey.equalsIgnoreCase("on")) {
                BindCommand.modifiersEnabled.setValue(true);
                sendChatMessage("Turned modifiers on.");
            }
            else if (rkey.equalsIgnoreCase("off")) {
                BindCommand.modifiersEnabled.setValue(false);
                sendChatMessage("Turned modifiers off.");
            }
            else {
                sendChatMessage("Expected: on or off");
            }
        }
        else {
            final Module m = ModuleManager.getModuleByName(module);
            if (m == null) {
                sendChatMessage("Unknown module '" + module + "'!");
                return;
            }
            if (rkey == null) {
                sendChatMessage(m.getName() + " is bound to &b" + m.getBindName());
                return;
            }
            int key = Wrapper.getKey(rkey);
            if (rkey.equalsIgnoreCase("none")) {
                key = -1;
            }
            if (key == 0) {
                sendChatMessage("Unknown key '" + rkey + "'!");
                return;
            }
            m.getBind().setKey(key);
            sendChatMessage("Bind for &b" + m.getName() + "&r set to &b" + rkey.toUpperCase());
        }
    }
    
    static {
        BindCommand.modifiersEnabled = SettingBuilder.register(Settings.b("modifiersEnabled", false), "binds");
    }
}
