//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.container.use;

import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.rgui.render.theme.*;

public class Groupbox extends AbstractContainer
{
    String name;
    
    public Groupbox(final Theme theme, final String name) {
        super(theme);
        this.name = name;
    }
    
    public Groupbox(final Theme theme, final String name, final int x, final int y) {
        this(theme, name);
        this.setX(x);
        this.setY(y);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
