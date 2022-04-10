//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.parsers.*;
import me.zeroeightsix.kami.command.syntax.*;
import java.util.stream.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;
import me.zeroeightsix.kami.setting.*;

public class SetCommand extends Command
{
    public SetCommand() {
        super("set", new ChunkBuilder().append("module", true, new ModuleParser()).append("setting", true).append("value", true).build(), new String[0]);
        this.setDescription("Change the setting of a certain module");
    }
    
    public void call(final String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module!");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Unknown module &b" + args[0] + "&r!");
            return;
        }
        if (args[1] == null) {
            final String settings = String.join(", ", (Iterable<? extends CharSequence>)m.settingList.stream().map(setting -> setting.getName()).collect(Collectors.toList()));
            if (settings.isEmpty()) {
                Command.sendChatMessage("Module &b" + m.getName() + "&r has no settings.");
            }
            else {
                Command.sendStringChatMessage(new String[] { "Please specify a setting! Choose one of the following:", settings });
            }
            return;
        }
        final Optional<Setting> optionalSetting = (Optional<Setting>)m.settingList.stream().filter(setting1 -> setting1.getName().equalsIgnoreCase(args[1])).findFirst();
        if (!optionalSetting.isPresent()) {
            Command.sendChatMessage("Unknown setting &b" + args[1] + "&r in &b" + m.getName() + "&r!");
            return;
        }
        final ISettingUnknown setting2 = optionalSetting.get();
        if (args[2] == null) {
            Command.sendChatMessage("&b" + setting2.getName() + "&r is a &3" + setting2.getValueClass().getSimpleName() + "&r. Its current value is &3" + setting2.getValueAsString());
            return;
        }
        try {
            String arg2 = args[2];
            if (setting2.getClass().getSimpleName().equals("EnumSetting")) {
                arg2 = arg2.toUpperCase();
            }
            setting2.setValueFromString(arg2);
            Command.sendChatMessage("Set &b" + setting2.getName() + "&r to &3" + arg2 + "&r.");
        }
        catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Unable to set value! &6" + e.getMessage());
        }
    }
}
