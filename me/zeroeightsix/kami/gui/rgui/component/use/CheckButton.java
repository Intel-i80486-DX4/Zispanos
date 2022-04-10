//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.use;

import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;
import me.zeroeightsix.kami.gui.rgui.poof.use.*;

public class CheckButton extends Button
{
    boolean toggled;
    
    public CheckButton(final String name) {
        this(name, 0, 0);
    }
    
    public CheckButton(final String name, final int x, final int y) {
        super(name, x, y);
        this.addMouseListener((MouseListener)new MouseListener() {
            public void onMouseDown(final MouseListener.MouseButtonEvent event) {
                if (event.getButton() != 0) {
                    return;
                }
                CheckButton.this.toggled = !CheckButton.this.toggled;
                CheckButton.this.callPoof((Class)CheckButtonPoof.class, (PoofInfo)new CheckButtonPoof.CheckButtonPoofInfo(CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.TOGGLE));
                if (CheckButton.this.toggled) {
                    CheckButton.this.callPoof((Class)CheckButtonPoof.class, (PoofInfo)new CheckButtonPoof.CheckButtonPoofInfo(CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.ENABLE));
                }
                else {
                    CheckButton.this.callPoof((Class)CheckButtonPoof.class, (PoofInfo)new CheckButtonPoof.CheckButtonPoofInfo(CheckButtonPoof.CheckButtonPoofInfo.CheckButtonPoofInfoAction.DISABLE));
                }
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
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public abstract static class CheckButtonPoof<T extends CheckButton, S extends CheckButtonPoofInfo> extends Poof<T, S>
    {
        CheckButtonPoofInfo info;
        
        public static class CheckButtonPoofInfo extends PoofInfo
        {
            CheckButtonPoofInfoAction action;
            
            public CheckButtonPoofInfo(final CheckButtonPoofInfoAction action) {
                this.action = action;
            }
            
            public CheckButtonPoofInfoAction getAction() {
                return this.action;
            }
            
            public enum CheckButtonPoofInfoAction
            {
                TOGGLE, 
                ENABLE, 
                DISABLE;
            }
        }
    }
}
