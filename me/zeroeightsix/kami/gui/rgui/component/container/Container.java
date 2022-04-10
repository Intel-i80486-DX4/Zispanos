//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.container;

import me.zeroeightsix.kami.gui.rgui.component.*;
import java.util.*;

public interface Container extends Component
{
    ArrayList<Component> getChildren();
    
    Component getComponentAt(final int p0, final int p1);
    
    Container addChild(final Component... p0);
    
    Container removeChild(final Component p0);
    
    boolean hasChild(final Component p0);
    
    void renderChildren();
    
    int getOriginOffsetX();
    
    int getOriginOffsetY();
    
    boolean penetrateTest(final int p0, final int p1);
}
