//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;

@Info(name = "Hidden:FixGui", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF, description = "Moves GUI elements back on screen")
public class FixGui extends Module
{
    public Setting<Boolean> shouldAutoEnable;
    
    public FixGui() {
        this.shouldAutoEnable = this.register(Settings.b("Enable", true));
    }
    
    @Override
    public void onUpdate() {
        GuiFrameUtil.fixFrames(FixGui.mc);
    }
}
