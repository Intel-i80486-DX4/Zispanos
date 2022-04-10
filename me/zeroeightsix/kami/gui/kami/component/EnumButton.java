//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.component;

import me.zeroeightsix.kami.gui.rgui.component.use.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;
import me.zeroeightsix.kami.gui.rgui.poof.use.*;

public class EnumButton extends Button
{
    String[] modes;
    int index;
    
    public EnumButton(final String name, final String[] modes) {
        super(name);
        this.modes = modes;
        this.index = 0;
        this.addPoof(new ButtonPoof<EnumButton, ButtonPoof.ButtonInfo>() {
            @Override
            public void execute(final EnumButton component, final ButtonInfo info) {
                if (info.getButton() == 0) {
                    final double p = info.getX() / (double)component.getWidth();
                    if (p <= 0.5) {
                        EnumButton.this.increaseIndex(-1);
                    }
                    else {
                        EnumButton.this.increaseIndex(1);
                    }
                }
            }
        });
    }
    
    public void setModes(final String[] modes) {
        this.modes = modes;
    }
    
    protected void increaseIndex(final int amount) {
        final int old = this.index;
        int newI = this.index + amount;
        if (newI < 0) {
            newI = this.modes.length - Math.abs(newI);
        }
        else if (newI >= this.modes.length) {
            newI = Math.abs(newI - this.modes.length);
        }
        this.index = Math.min(this.modes.length, Math.max(0, newI));
        this.callPoof(EnumbuttonIndexPoof.class, new EnumbuttonIndexPoof.EnumbuttonInfo(old, this.index));
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public String[] getModes() {
        return this.modes;
    }
    
    public String getIndexMode() {
        return this.modes[this.index];
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public abstract static class EnumbuttonIndexPoof<T extends Button, S extends EnumbuttonInfo> extends Poof<T, S>
    {
        ButtonPoof.ButtonInfo info;
        
        public static class EnumbuttonInfo extends PoofInfo
        {
            int oldIndex;
            int newIndex;
            
            public EnumbuttonInfo(final int oldIndex, final int newIndex) {
                this.oldIndex = oldIndex;
                this.newIndex = newIndex;
            }
            
            public int getNewIndex() {
                return this.newIndex;
            }
            
            public void setNewIndex(final int newIndex) {
                this.newIndex = newIndex;
            }
            
            public int getOldIndex() {
                return this.oldIndex;
            }
        }
    }
}
