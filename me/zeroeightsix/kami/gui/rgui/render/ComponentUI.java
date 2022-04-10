//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.render;

import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;

public interface ComponentUI<T extends Component>
{
    void renderComponent(final T p0, final FontRenderer p1);
    
    void handleMouseDown(final T p0, final int p1, final int p2, final int p3);
    
    void handleMouseRelease(final T p0, final int p1, final int p2, final int p3);
    
    void handleMouseDrag(final T p0, final int p1, final int p2, final int p3);
    
    void handleScroll(final T p0, final int p1, final int p2, final int p3, final boolean p4);
    
    void handleKeyDown(final T p0, final int p1);
    
    void handleKeyUp(final T p0, final int p1);
    
    void handleAddComponent(final T p0, final Container p1);
    
    void handleSizeComponent(final T p0);
    
    Class<? extends Component> getHandledClass();
}
