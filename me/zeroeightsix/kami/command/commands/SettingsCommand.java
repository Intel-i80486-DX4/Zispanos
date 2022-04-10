//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.parsers.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.impl.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;

public class SettingsCommand extends Command
{
    public SettingsCommand() {
        super("settings", new ChunkBuilder().append("module", true, new ModuleParser()).build(), new String[0]);
        this.setDescription("List the possible settings of a command");
    }
    
    public void call(final String[] args) {
        if (args[0] == null) {
            Command.sendChatMessage("Please specify a module to display the settings of.");
            return;
        }
        final Module m = ModuleManager.getModuleByName(args[0]);
        if (m == null) {
            Command.sendChatMessage("Couldn't find a module &b" + args[0] + "!");
            return;
        }
        final List<Setting> settings = (List<Setting>)m.settingList;
        final String[] result = new String[settings.size()];
        for (int i = 0; i < settings.size(); ++i) {
            final Setting setting = settings.get(i);
            result[i] = "&b" + setting.getName() + "&3(=" + setting.getValue() + ")  &ftype: &3" + setting.getValue().getClass().getSimpleName();
            if (setting instanceof EnumSetting) {
                final StringBuilder sb = new StringBuilder();
                final String[] array = result;
                final int n = i;
                array[n] = sb.append(array[n]).append("  (").toString();
                final Enum[] array2;
                final Enum[] enums = array2 = (Enum[])((EnumSetting)setting).clazz.getEnumConstants();
                for (final Enum e : array2) {
                    final StringBuilder sb2 = new StringBuilder();
                    final String[] array3 = result;
                    final int n2 = i;
                    array3[n2] = sb2.append(array3[n2]).append(e.name()).append(", ").toString();
                }
                result[i] = result[i].substring(0, result[i].length() - 2) + ")";
            }
        }
        Command.sendStringChatMessage(result);
    }
}
