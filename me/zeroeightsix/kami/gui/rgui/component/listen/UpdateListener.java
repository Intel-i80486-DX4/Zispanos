//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.listen;

import me.zeroeightsix.kami.gui.rgui.component.*;

public interface UpdateListener<T extends Component>
{
    void updateSize(final T p0, final int p1, final int p2);
    
    void updateLocation(final T p0, final int p1, final int p2);
}
