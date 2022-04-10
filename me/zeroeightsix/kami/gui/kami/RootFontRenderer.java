//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami;

import me.zeroeightsix.kami.gui.rgui.render.font.*;
import net.minecraft.client.*;
import java.awt.*;
import org.lwjgl.opengl.*;

public class RootFontRenderer implements FontRenderer
{
    private final float fontsize;
    private final net.minecraft.client.gui.FontRenderer fontRenderer;
    
    public RootFontRenderer(final float fontsize) {
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.fontsize = fontsize;
    }
    
    @Override
    public int getFontHeight() {
        return (int)(Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT * this.fontsize);
    }
    
    @Override
    public int getStringHeight(final String text) {
        return this.getFontHeight();
    }
    
    @Override
    public int getStringWidth(final String text) {
        return (int)(this.fontRenderer.getStringWidth(text) * this.fontsize);
    }
    
    @Override
    public void drawString(final int x, final int y, final String text) {
        this.drawString(x, y, 255, 255, 255, text);
    }
    
    @Override
    public void drawString(final int x, final int y, final int r, final int g, final int b, final String text) {
        this.drawString(x, y, 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF), text);
    }
    
    @Override
    public void drawString(final int x, final int y, final Color color, final String text) {
        this.drawString(x, y, color.getRGB(), text);
    }
    
    @Override
    public void drawString(final int x, final int y, final int colour, final String text) {
        this.drawString(x, y, colour, text, true);
    }
    
    public void drawString(final int x, final int y, final int colour, final String text, final boolean shadow) {
        this.prepare(x, y);
        Minecraft.getMinecraft().fontRenderer.drawString(text, 0.0f, 0.0f, colour, shadow);
        this.pop(x, y);
    }
    
    @Override
    public void drawStringWithShadow(final int x, final int y, final int r, final int g, final int b, final String text) {
        this.drawString(x, y, 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF), text, true);
    }
    
    private void prepare(final int x, final int y) {
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GL11.glTranslatef((float)x, (float)y, 0.0f);
        GL11.glScalef(this.fontsize, this.fontsize, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    private void pop(final int x, final int y) {
        GL11.glScalef(1.0f / this.fontsize, 1.0f / this.fontsize, 1.0f);
        GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
        GL11.glDisable(3553);
    }
}
