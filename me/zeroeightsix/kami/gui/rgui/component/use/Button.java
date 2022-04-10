//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.use;

import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;
import me.zeroeightsix.kami.gui.rgui.poof.use.*;

public class Button extends AbstractComponent
{
    private String name;
    
    public Button(final String name) {
        this(name, 0, 0);
        this.addMouseListener((MouseListener)new MouseListener() {
            public void onMouseDown(final MouseListener.MouseButtonEvent event) {
                Button.this.callPoof((Class)ButtonPoof.class, (PoofInfo)new ButtonPoof.ButtonInfo(event.getButton(), event.getX(), event.getY()));
            }
            
            public void onMouseRelease(final MouseListener.MouseButtonEvent event) {
            }
            
            public void onMouseDrag(final MouseListener.MouseButtonEvent event) {
            }
            
            public void onMouseMove(final MouseListener.MouseMoveEvent event) {
            }
            
            public void onScroll(final MouseListener.MouseScrollEvent event) {
            }
        });
    }
    
    public Button(final String name, final int x, final int y) {
        this.name = name;
        this.setX(x);
        this.setY(y);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void kill() {
    }
    
    public abstract static class ButtonPoof<T extends Button, S extends ButtonInfo> extends Poof<T, S>
    {
        ButtonInfo info;
        
        public static class ButtonInfo extends PoofInfo
        {
            int button;
            int x;
            int y;
            
            public ButtonInfo(final int button, final int x, final int y) {
                this.button = button;
                this.x = x;
                this.y = y;
            }
            
            public int getX() {
                return this.x;
            }
            
            public int getY() {
                return this.y;
            }
            
            public int getButton() {
                return this.button;
            }
        }
    }
}
