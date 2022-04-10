//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.rgui.component.listen;

public interface KeyListener
{
    void onKeyDown(final KeyEvent p0);
    
    void onKeyUp(final KeyEvent p0);
    
    public static class KeyEvent
    {
        int key;
        
        public KeyEvent(final int key) {
            this.key = key;
        }
        
        public int getKey() {
            return this.key;
        }
        
        public void setKey(final int key) {
            this.key = key;
        }
    }
}
