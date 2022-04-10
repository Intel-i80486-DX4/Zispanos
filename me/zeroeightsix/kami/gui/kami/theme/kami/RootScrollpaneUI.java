//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.rgui.component.container.use.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.util.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.kami.*;

public class RootScrollpaneUI extends AbstractComponentUI<Scrollpane>
{
    long lastScroll;
    Component scrollComponent;
    float barLife;
    boolean dragBar;
    int dY;
    double hovering;
    
    public RootScrollpaneUI() {
        this.lastScroll = 0L;
        this.scrollComponent = null;
        this.barLife = 1220.0f;
        this.dragBar = false;
        this.dY = 0;
        this.hovering = 0.0;
    }
    
    @Override
    public void renderComponent(final Scrollpane component, final FontRenderer fontRenderer) {
    }
    
    @Override
    public void handleAddComponent(final Scrollpane component, final Container container) {
        component.addMouseListener(new MouseListener() {
            @Override
            public void onMouseDown(final MouseButtonEvent event) {
                if (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll < RootScrollpaneUI.this.barLife && RootScrollpaneUI.this.scrollComponent.liesIn(component) && component.canScrollY()) {
                    final double progress = component.getScrolledY() / (double)component.getMaxScrollY();
                    final int barHeight = 30;
                    final int y = (int)((component.getHeight() - barHeight) * progress);
                    if (event.getX() > component.getWidth() - 10 && event.getY() > y && event.getY() < y + barHeight) {
                        RootScrollpaneUI.this.dragBar = true;
                        RootScrollpaneUI.this.dY = event.getY() - y;
                        event.cancel();
                    }
                }
            }
            
            @Override
            public void onMouseRelease(final MouseButtonEvent event) {
                RootScrollpaneUI.this.dragBar = false;
            }
            
            @Override
            public void onMouseDrag(final MouseButtonEvent event) {
                if (RootScrollpaneUI.this.dragBar) {
                    double progress = event.getY() / (double)component.getHeight();
                    progress = Math.max(Math.min(progress, 1.0), 0.0);
                    component.setScrolledY((int)(component.getMaxScrollY() * progress));
                    event.cancel();
                }
            }
            
            @Override
            public void onMouseMove(final MouseMoveEvent event) {
            }
            
            @Override
            public void onScroll(final MouseScrollEvent event) {
                RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                RootScrollpaneUI.this.scrollComponent = event.getComponent();
            }
        });
        component.addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
            }
            
            @Override
            public void onPostRender() {
                if (RootScrollpaneUI.this.dragBar) {
                    RootScrollpaneUI.this.lastScroll = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll < RootScrollpaneUI.this.barLife && RootScrollpaneUI.this.scrollComponent.liesIn(component) && component.canScrollY()) {
                    float alpha = Math.min(1.0f, (RootScrollpaneUI.this.barLife - (System.currentTimeMillis() - RootScrollpaneUI.this.lastScroll)) / 100.0f) / 3.0f;
                    if (RootScrollpaneUI.this.dragBar) {
                        alpha = 0.4f;
                    }
                    GL11.glColor4f(ColourConverter.toF(ColourSet.scrollBar.getRed()), ColourConverter.toF(ColourSet.scrollBar.getGreen()), ColourConverter.toF(ColourSet.scrollBar.getBlue()), alpha);
                    GL11.glDisable(3553);
                    final int barHeight = 30;
                    final double progress = component.getScrolledY() / (double)component.getMaxScrollY();
                    final int y = (int)((component.getHeight() - barHeight) * progress);
                    RenderHelper.drawRoundedRectangle((float)(component.getWidth() - 6), (float)y, 4.0f, (float)barHeight, 1.0f);
                }
            }
        });
    }
}
