//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.gui;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "CleanGUI", category = Category.GUI, showOnArray = ShowOnArray.OFF, description = "Modifies parts of the GUI to be transparent")
public class CleanGUI extends Module
{
    public Setting<Boolean> startupGlobal;
    public Setting<Boolean> inventoryGlobal;
    public static Setting<Boolean> chatGlobal;
    private static CleanGUI INSTANCE;
    
    public CleanGUI() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", false));
        this.inventoryGlobal = this.register(Settings.b("Inventory", false));
        (CleanGUI.INSTANCE = this).register(CleanGUI.chatGlobal);
    }
    
    public static boolean enabled() {
        return CleanGUI.INSTANCE.isEnabled();
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
    
    static {
        CleanGUI.chatGlobal = Settings.b("Chat", true);
        CleanGUI.INSTANCE = new CleanGUI();
    }
}
