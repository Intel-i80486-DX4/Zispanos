//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.gui;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "PrefixChat", category = Category.GUI, description = "Opens chat with prefix inside when prefix is pressed.", showOnArray = ShowOnArray.OFF)
public class PrefixChat extends Module
{
    public Setting<Boolean> startupGlobal;
    
    public PrefixChat() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
}
