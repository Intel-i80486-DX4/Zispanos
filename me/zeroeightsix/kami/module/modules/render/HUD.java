//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import java.awt.*;

@Info(name = "HUD", description = "", category = Category.RENDER)
public class HUD extends Module
{
    private Setting<Boolean> chroma;
    private Setting<Double> chromaSpeed;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private Setting<Pos> arrayListPos;
    private Setting<Boolean> watermark;
    private Setting<Boolean> welcomer;
    private Setting<Boolean> info;
    private static HUD INSTANCE;
    
    public HUD() {
        this.chroma = this.register(Settings.b("Chroma", true));
        this.chromaSpeed = this.register(Settings.d("Chroma speed", 3.0));
        this.r = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(1).withMaximum(255).withValue(255).build());
        this.g = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(1).withMaximum(255).withValue(0).build());
        this.b = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(1).withMaximum(255).withValue(255).build());
        this.arrayListPos = this.register(Settings.e("Active Module List Pos", Pos.TopRight));
        this.watermark = this.register(Settings.b("Watermark", true));
        this.welcomer = this.register(Settings.b("Welcomer", true));
        this.info = this.register(Settings.b("Info", true));
        HUD.INSTANCE = this;
    }
    
    public static boolean chroma() {
        return HUD.INSTANCE.chroma.getValue();
    }
    
    public static boolean info() {
        return HUD.INSTANCE.info.getValue();
    }
    
    public static float speed() {
        return HUD.INSTANCE.chromaSpeed.getValue().floatValue();
    }
    
    public static int rgb() {
        final Color color = new Color(red(), green(), blue());
        final int rgb = color.getRGB();
        return rgb;
    }
    
    public static int red() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int red = rgb >> 16 & 0xFF;
        if (chroma()) {
            return red;
        }
        return HUD.INSTANCE.r.getValue();
    }
    
    public static int green() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int green = rgb >> 8 & 0xFF;
        if (chroma()) {
            return green;
        }
        return HUD.INSTANCE.g.getValue();
    }
    
    public static int blue() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int blue = rgb & 0xFF;
        if (chroma()) {
            return blue;
        }
        return HUD.INSTANCE.b.getValue();
    }
    
    public static float redF() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int red = rgb >> 16 & 0xFF;
        if (chroma()) {
            return (float)red;
        }
        return HUD.INSTANCE.r.getValue();
    }
    
    public static float greenF() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int green = rgb >> 8 & 0xFF;
        if (chroma()) {
            return (float)green;
        }
        return HUD.INSTANCE.g.getValue();
    }
    
    public static float blueF() {
        final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f * speed() };
        final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
        final int blue = rgb & 0xFF;
        if (chroma()) {
            return (float)blue;
        }
        return HUD.INSTANCE.b.getValue();
    }
    
    public static Pos AMpos() {
        return HUD.INSTANCE.arrayListPos.getValue();
    }
    
    public static boolean watermark() {
        return HUD.INSTANCE.watermark.getValue();
    }
    
    public static boolean welcomer() {
        return HUD.INSTANCE.welcomer.getValue();
    }
    
    static {
        HUD.INSTANCE = new HUD();
    }
    
    public enum Pos
    {
        TopRight, 
        TopLeft, 
        BottomRight, 
        BottomLeft, 
        None;
    }
}
