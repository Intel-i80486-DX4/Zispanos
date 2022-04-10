//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;

public class SayCommand extends Command
{
    public SayCommand() {
        super("say", new ChunkBuilder().append("message").build(), new String[0]);
        this.setDescription("Allows you to send any message, even with a prefix in it");
    }
    
    public void call(final String[] args) {
        final StringBuilder message = new StringBuilder();
        for (final String arg : args) {
            if (arg != null) {
                message.append(" ").append(arg);
            }
        }
        Command.sendServerMessage(message.toString());
    }
}
