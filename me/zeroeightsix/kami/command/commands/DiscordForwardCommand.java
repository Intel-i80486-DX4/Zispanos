//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.module.modules.chat.*;
import me.zeroeightsix.kami.module.*;

public class DiscordForwardCommand extends Command
{
    public DiscordForwardCommand() {
        super("discordforward", new ChunkBuilder().append("webhook url").append("avatar url").build(), new String[] { "webhook" });
    }
    
    public void call(final String[] args) {
        final DiscordForward df = (DiscordForward)ModuleManager.getModuleByName("DiscordForward");
        if (args[0] != null && !args[0].equals("")) {
            df.url.setValue(args[0]);
            Command.sendChatMessage(df.getChatName() + "Set URL to \"" + args[0] + "\"!");
        }
        else if (args[0] == null) {
            Command.sendErrorMessage(df.getChatName() + "Error: you must specify a URL or \"\" for the first parameter when running the command");
        }
        if (args[1] == null) {
            return;
        }
        if (!args[1].equals("")) {
            df.avatar.setValue(args[1]);
            Command.sendChatMessage(df.getChatName() + "Set Avatar to \"" + args[1] + "\"!");
        }
        else {
            df.avatar.setValue("https://github.com/S-B99/kamiblue/raw/assets/assets/icons/kami.png");
            Command.sendChatMessage(df.getChatName() + "Reset Avatar!");
        }
    }
}
