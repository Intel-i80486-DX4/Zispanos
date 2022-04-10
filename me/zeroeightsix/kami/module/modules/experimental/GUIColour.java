//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.experimental;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "GUI Colour", description = "Change GUI Colours", category = Category.EXPERIMENTAL)
public class GUIColour extends Module
{
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    
    public GUIColour() {
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withValue(13).withMaximum(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withValue(13).withMaximum(255).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withValue(13).withMaximum(255).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withValue(117).withMaximum(255).build());
    }
}
