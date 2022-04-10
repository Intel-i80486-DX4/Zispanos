//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "ExtraTab", description = "Expands the player tab menu", category = Category.RENDER)
public class ExtraTab extends Module
{
    public Setting<Integer> tabSize;
    public static ExtraTab INSTANCE;
    
    public ExtraTab() {
        this.tabSize = this.register((Setting<Integer>)Settings.integerBuilder("Players").withMinimum(1).withValue(80).build());
        ExtraTab.INSTANCE = this;
    }
}
