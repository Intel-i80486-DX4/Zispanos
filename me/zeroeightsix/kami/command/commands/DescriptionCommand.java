//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.module.*;

public class DescriptionCommand extends Command
{
    public DescriptionCommand() {
        super("description", new ChunkBuilder().append("module").build(), new String[] { "tooltip" });
        this.setDescription("Prints a module's description into the chat");
    }
    
    public void call(final String[] args) {
        for (final String s : args) {
            if (s != null) {
                final Module module = ModuleManager.getModuleByName(s);
                Command.sendChatMessage(module.getChatName() + "Description: &7" + module.getDescription());
            }
        }
    }
}
