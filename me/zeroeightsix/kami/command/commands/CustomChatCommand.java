//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.module.modules.chat.*;
import me.zeroeightsix.kami.module.*;

public class CustomChatCommand extends Command
{
    public CustomChatCommand() {
        super("customchat", new ChunkBuilder().append("ending").build(), new String[] { "chat" });
        this.setDescription("Allows you to customize CustomChat's custom setting");
    }
    
    public void call(final String[] args) {
        final CustomChat cC = (CustomChat)ModuleManager.getModuleByName("CustomChat");
        if (cC == null) {
            Command.sendErrorMessage("&cThe CustomChat module is not available for some reason. Make sure the name you're calling is correct and that you have the module installed!!");
            return;
        }
        if (!cC.isEnabled()) {
            Command.sendWarningMessage("&6Warning: The CustomChat module is not enabled!");
            Command.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        if (!cC.textMode.getValue().equals((Object)CustomChat.TextMode.CUSTOM)) {
            Command.sendWarningMessage("&6Warning: You don't have custom mode enabled in CustomChat!");
            Command.sendWarningMessage("The command will still work, but will not visibly do anything.");
        }
        for (final String s : args) {
            if (s != null) {
                cC.customText.setValue(s);
                Command.sendChatMessage("Set the Custom Text Mode to <" + s + ">");
            }
        }
    }
}
