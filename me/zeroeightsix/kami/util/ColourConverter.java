//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

public class ColourConverter
{
    public static float toF(final int i) {
        return i / 255.0f;
    }
    
    public static float toF(final double d) {
        return (float)(d / 255.0);
    }
    
    public static int rgbToInt(final int r, final int g, final int b, final int a) {
        return r << 16 | g << 8 | b | a << 24;
    }
    
    public static int rgbToInt(final int r, final int g, final int b) {
        return r << 16 | g << 8 | b;
    }
}
