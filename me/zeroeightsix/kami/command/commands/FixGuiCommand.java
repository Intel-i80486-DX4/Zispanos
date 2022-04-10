//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.module.modules.hidden.*;
import me.zeroeightsix.kami.module.*;

public class FixGuiCommand extends Command
{
    public FixGuiCommand() {
        super("fixgui", new ChunkBuilder().build(), new String[0]);
        this.setDescription("Allows you to disable the automatic gui positioning");
    }
    
    public void call(final String[] args) {
        final FixGui fixGui = (FixGui)ModuleManager.getModuleByName("Hidden:FixGui");
        if (fixGui.isEnabled() && fixGui.shouldAutoEnable.getValue()) {
            fixGui.shouldAutoEnable.setValue(false);
            fixGui.disable();
            Command.sendChatMessage("[" + this.getLabel() + "] Disabled");
        }
        else {
            fixGui.shouldAutoEnable.setValue(true);
            fixGui.enable();
            Command.sendChatMessage("[" + this.getLabel() + "] Enabled");
        }
    }
}
