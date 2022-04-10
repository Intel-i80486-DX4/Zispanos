//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component;

public class AlignedComponent extends AbstractComponent
{
    Alignment alignment;
    
    public Alignment getAlignment() {
        return this.alignment;
    }
    
    public void setAlignment(final Alignment alignment) {
        this.alignment = alignment;
    }
    
    public enum Alignment
    {
        LEFT(0), 
        CENTER(1), 
        RIGHT(2);
        
        int index;
        
        private Alignment(final int index) {
            this.index = index;
        }
        
        public int getIndex() {
            return this.index;
        }
    }
}
