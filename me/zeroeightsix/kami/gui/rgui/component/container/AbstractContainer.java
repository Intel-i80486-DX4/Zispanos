//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.container;

import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.render.theme.*;
import me.zeroeightsix.kami.gui.rgui.poof.use.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import java.util.*;

public abstract class AbstractContainer extends AbstractComponent implements Container
{
    protected ArrayList<Component> children;
    int originoffsetX;
    int originoffsetY;
    
    public AbstractContainer(final Theme theme) {
        this.children = new ArrayList<Component>();
        this.originoffsetX = 0;
        this.originoffsetY = 0;
        this.setTheme(theme);
    }
    
    public ArrayList<Component> getChildren() {
        return this.children;
    }
    
    public Container addChild(final Component... components) {
        for (final Component component : components) {
            if (!this.children.contains(component)) {
                component.setTheme(this.getTheme());
                component.setParent((Container)this);
                component.getUI().handleAddComponent(component, (Container)this);
                component.getUI().handleSizeComponent(component);
                synchronized (this.children) {
                    this.children.add(component);
                    Collections.sort(this.children, new Comparator<Component>() {
                        @Override
                        public int compare(final Component o1, final Component o2) {
                            return o1.getPriority() - o2.getPriority();
                        }
                    });
                    component.callPoof((Class<? extends IPoof>)AdditionPoof.class, null);
                }
            }
        }
        return (Container)this;
    }
    
    public Container removeChild(final Component component) {
        if (this.children.contains(component)) {
            this.children.remove(component);
        }
        return (Container)this;
    }
    
    public boolean hasChild(final Component component) {
        return this.children.contains(component);
    }
    
    public void renderChildren() {
        for (final Component c : this.getChildren()) {
            if (!c.isVisible()) {
                continue;
            }
            GL11.glPushMatrix();
            GL11.glTranslatef((float)c.getX(), (float)c.getY(), 0.0f);
            c.getRenderListeners().forEach(RenderListener::onPreRender);
            c.getUI().renderComponent(c, this.getTheme().getFontRenderer());
            if (c instanceof Container) {
                GL11.glTranslatef((float)((Container)c).getOriginOffsetX(), (float)((Container)c).getOriginOffsetY(), 0.0f);
                ((Container)c).renderChildren();
                GL11.glTranslatef((float)(-((Container)c).getOriginOffsetX()), (float)(-((Container)c).getOriginOffsetY()), 0.0f);
            }
            c.getRenderListeners().forEach(RenderListener::onPostRender);
            GL11.glTranslatef((float)(-c.getX()), (float)(-c.getY()), 0.0f);
            GL11.glPopMatrix();
        }
    }
    
    public Component getComponentAt(final int x, final int y) {
        for (int i = this.getChildren().size() - 1; i >= 0; --i) {
            final Component c = this.getChildren().get(i);
            if (c.isVisible()) {
                final int componentX = c.getX() + this.getOriginOffsetX();
                final int componentY = c.getY() + this.getOriginOffsetY();
                final int componentWidth = c.getWidth();
                final int componentHeight = c.getHeight();
                if (c instanceof Container) {
                    final Container container = (Container)c;
                    final boolean penetrate = container.penetrateTest(x - this.getOriginOffsetX(), y - this.getOriginOffsetY());
                    if (!penetrate) {
                        continue;
                    }
                    final Component a = ((Container)c).getComponentAt(x - componentX, y - componentY);
                    if (a != c) {
                        return a;
                    }
                }
                if (x >= componentX && y >= componentY && x <= componentX + componentWidth && y <= componentY + componentHeight) {
                    if (c instanceof Container) {
                        final Container container = (Container)c;
                        final Component hit = container.getComponentAt(x - componentX, y - componentY);
                        return hit;
                    }
                    return c;
                }
            }
        }
        return (Component)this;
    }
    
    public void setWidth(final int width) {
        super.setWidth(width + this.getOriginOffsetX());
    }
    
    public void setHeight(final int height) {
        super.setHeight(height + this.getOriginOffsetY());
    }
    
    public void kill() {
        for (final Component c : this.children) {
            c.kill();
        }
        super.kill();
    }
    
    public int getOriginOffsetX() {
        return this.originoffsetX;
    }
    
    public int getOriginOffsetY() {
        return this.originoffsetY;
    }
    
    public void setOriginOffsetX(final int originoffsetX) {
        this.originoffsetX = originoffsetX;
    }
    
    public void setOriginOffsetY(final int originoffsetY) {
        this.originoffsetY = originoffsetY;
    }
    
    public boolean penetrateTest(final int x, final int y) {
        return true;
    }
}
