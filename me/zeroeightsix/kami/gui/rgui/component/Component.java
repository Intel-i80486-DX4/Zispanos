//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component;

import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.rgui.render.theme.*;
import java.util.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;

public interface Component
{
    int getX();
    
    int getY();
    
    int getWidth();
    
    int getHeight();
    
    void setX(final int p0);
    
    void setY(final int p0);
    
    void setWidth(final int p0);
    
    void setHeight(final int p0);
    
    Component setMinimumWidth(final int p0);
    
    Component setMaximumWidth(final int p0);
    
    Component setMinimumHeight(final int p0);
    
    Component setMaximumHeight(final int p0);
    
    int getMinimumWidth();
    
    int getMaximumWidth();
    
    int getMinimumHeight();
    
    int getMaximumHeight();
    
    float getOpacity();
    
    void setOpacity(final float p0);
    
    boolean doAffectLayout();
    
    void setAffectLayout(final boolean p0);
    
    Container getParent();
    
    void setParent(final Container p0);
    
    boolean liesIn(final Component p0);
    
    boolean isVisible();
    
    void setVisible(final boolean p0);
    
    void setFocused(final boolean p0);
    
    boolean isFocused();
    
    ComponentUI getUI();
    
    Theme getTheme();
    
    void setTheme(final Theme p0);
    
    boolean isHovered();
    
    boolean isPressed();
    
    ArrayList<MouseListener> getMouseListeners();
    
    void addMouseListener(final MouseListener p0);
    
    ArrayList<RenderListener> getRenderListeners();
    
    void addRenderListener(final RenderListener p0);
    
    ArrayList<KeyListener> getKeyListeners();
    
    void addKeyListener(final KeyListener p0);
    
    ArrayList<UpdateListener> getUpdateListeners();
    
    void addUpdateListener(final UpdateListener p0);
    
    ArrayList<TickListener> getTickListeners();
    
    void addTickListener(final TickListener p0);
    
    void addPoof(final IPoof p0);
    
    void callPoof(final Class<? extends IPoof> p0, final PoofInfo p1);
    
    int getPriority();
    
    void kill();
}
