//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.container.use;

import me.zeroeightsix.kami.gui.rgui.render.theme.*;
import me.zeroeightsix.kami.gui.rgui.layout.*;
import me.zeroeightsix.kami.gui.rgui.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.kami.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;
import java.util.*;

public class Scrollpane extends OrganisedContainer
{
    int scrolledX;
    int maxScrollX;
    int scrolledY;
    int maxScrollY;
    boolean doScrollX;
    boolean doScrollY;
    boolean canScrollX;
    boolean canScrollY;
    boolean lockWidth;
    boolean lockHeight;
    int step;
    
    public Scrollpane(final Theme theme, final Layout layout, final int width, final int height) {
        super(theme, layout);
        this.doScrollX = false;
        this.doScrollY = true;
        this.canScrollX = false;
        this.canScrollY = false;
        this.lockWidth = false;
        this.lockHeight = false;
        this.step = 22;
        this.setWidth(width);
        this.setHeight(height);
        this.scrolledX = 0;
        this.scrolledY = 0;
        this.addRenderListener((RenderListener)new RenderListener() {
            int translatex;
            int translatey;
            
            public void onPreRender() {
                this.translatex = Scrollpane.this.scrolledX;
                this.translatey = Scrollpane.this.scrolledY;
                final int[] real = GUI.calculateRealPosition((Component)Scrollpane.this);
                final int scale = DisplayGuiScreen.getScale();
                GL11.glScissor(Scrollpane.this.getX() * scale + real[0] * scale - Scrollpane.this.getParent().getOriginOffsetX() - 1, Display.getHeight() - Scrollpane.this.getHeight() * scale - real[1] * scale - 1, Scrollpane.this.getWidth() * scale + Scrollpane.this.getParent().getOriginOffsetX() * scale + 1, Scrollpane.this.getHeight() * scale + 1);
                GL11.glEnable(3089);
            }
            
            public void onPostRender() {
                GL11.glDisable(3089);
            }
        });
        this.addMouseListener((MouseListener)new MouseListener() {
            public void onMouseDown(final MouseListener.MouseButtonEvent event) {
                if (event.getY() > Scrollpane.this.getHeight() || event.getX() > Scrollpane.this.getWidth() || event.getX() < 0 || event.getY() < 0) {
                    event.cancel();
                }
            }
            
            public void onMouseRelease(final MouseListener.MouseButtonEvent event) {
            }
            
            public void onMouseDrag(final MouseListener.MouseButtonEvent event) {
            }
            
            public void onMouseMove(final MouseListener.MouseMoveEvent event) {
            }
            
            public void onScroll(final MouseListener.MouseScrollEvent event) {
                if (Scrollpane.this.canScrollY() && (!Scrollpane.this.canScrollX() || Scrollpane.this.scrolledX == 0 || !Scrollpane.this.isDoScrollX()) && Scrollpane.this.isDoScrollY()) {
                    if (event.isUp() && Scrollpane.this.getScrolledY() > 0) {
                        Scrollpane.this.setScrolledY(Math.max(0, Scrollpane.this.getScrolledY() - Scrollpane.this.step));
                        return;
                    }
                    if (!event.isUp() && Scrollpane.this.getScrolledY() < Scrollpane.this.getMaxScrollY()) {
                        Scrollpane.this.setScrolledY(Math.min(Scrollpane.this.getMaxScrollY(), Scrollpane.this.getScrolledY() + Scrollpane.this.step));
                        return;
                    }
                }
                if (Scrollpane.this.canScrollX() && Scrollpane.this.isDoScrollX()) {
                    if (event.isUp() && Scrollpane.this.getScrolledX() > 0) {
                        Scrollpane.this.setScrolledX(Math.max(0, Scrollpane.this.getScrolledX() - Scrollpane.this.step));
                        return;
                    }
                    if (!event.isUp() && Scrollpane.this.getScrolledX() < Scrollpane.this.getMaxScrollX()) {
                        Scrollpane.this.setScrolledX(Math.min(Scrollpane.this.getMaxScrollX(), Scrollpane.this.getScrolledX() + Scrollpane.this.step));
                    }
                }
            }
        });
        this.addUpdateListener((UpdateListener)new UpdateListener() {
            public void updateSize(final Component component, final int oldWidth, final int oldHeight) {
                Scrollpane.this.performCalculations();
            }
            
            public void updateLocation(final Component component, final int oldX, final int oldY) {
                Scrollpane.this.performCalculations();
            }
        });
    }
    
    public void setWidth(final int width) {
        if (!this.lockWidth) {
            super.setWidth(width);
        }
    }
    
    public void setHeight(final int height) {
        if (!this.lockHeight) {
            super.setHeight(height);
        }
    }
    
    public Container addChild(final Component... component) {
        super.addChild(component);
        this.performCalculations();
        return (Container)this;
    }
    
    private void performCalculations() {
        int farX = 0;
        int farY = 0;
        for (final Component c : this.getChildren()) {
            farX = Math.max(this.getScrolledX() + c.getX() + c.getWidth(), farX);
            farY = Math.max(this.getScrolledY() + c.getY() + c.getHeight(), farY);
        }
        this.canScrollX = (farX > this.getWidth());
        this.maxScrollX = farX - this.getWidth();
        this.canScrollY = (farY > this.getHeight());
        this.maxScrollY = farY - this.getHeight();
    }
    
    public boolean canScrollX() {
        return this.canScrollX;
    }
    
    public boolean canScrollY() {
        return this.canScrollY;
    }
    
    public boolean isDoScrollX() {
        return this.doScrollX;
    }
    
    public boolean isDoScrollY() {
        return this.doScrollY;
    }
    
    public void setDoScrollY(final boolean doScrollY) {
        this.doScrollY = doScrollY;
    }
    
    public void setDoScrollX(final boolean doScrollX) {
        this.doScrollX = doScrollX;
    }
    
    public void setScrolledX(final int scrolledX) {
        final int a = this.getScrolledX();
        this.scrolledX = scrolledX;
        final int dif = this.getScrolledX() - a;
        for (final Component component : this.getChildren()) {
            component.setX(component.getX() - dif);
        }
    }
    
    public void setScrolledY(final int scrolledY) {
        final int a = this.getScrolledY();
        this.scrolledY = scrolledY;
        final int dif = this.getScrolledY() - a;
        for (final Component component : this.getChildren()) {
            component.setY(component.getY() - dif);
        }
    }
    
    public int getScrolledX() {
        return this.scrolledX;
    }
    
    public int getScrolledY() {
        return this.scrolledY;
    }
    
    public int getMaxScrollX() {
        return this.maxScrollX;
    }
    
    public int getMaxScrollY() {
        return this.maxScrollY;
    }
    
    public Scrollpane setLockHeight(final boolean lockHeight) {
        this.lockHeight = lockHeight;
        return this;
    }
    
    public Scrollpane setLockWidth(final boolean lockWidth) {
        this.lockWidth = lockWidth;
        return this;
    }
    
    public boolean penetrateTest(final int x, final int y) {
        return x > 0 && x < this.getWidth() && y > 0 && y < this.getHeight();
    }
}
