//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.event.events;

import net.minecraft.client.gui.*;

public class GuiScreenEvent
{
    private GuiScreen screen;
    
    public GuiScreenEvent(final GuiScreen screen) {
        this.screen = screen;
    }
    
    public GuiScreen getScreen() {
        return this.screen;
    }
    
    public void setScreen(final GuiScreen screen) {
        this.screen = screen;
    }
    
    public static class Displayed extends GuiScreenEvent
    {
        public Displayed(final GuiScreen screen) {
            super(screen);
        }
    }
    
    public static class Closed extends GuiScreenEvent
    {
        public Closed(final GuiScreen screen) {
            super(screen);
        }
    }
}
