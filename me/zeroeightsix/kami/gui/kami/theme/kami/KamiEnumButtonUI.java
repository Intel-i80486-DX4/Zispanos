//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.kami.*;
import java.awt.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.kami.component.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.poof.*;

public class KamiEnumButtonUI extends AbstractComponentUI<EnumButton>
{
    RootSmallFontRenderer smallFontRenderer;
    protected Color idleColour;
    protected Color downColour;
    EnumButton modeComponent;
    long lastMS;
    
    public KamiEnumButtonUI() {
        this.smallFontRenderer = new RootSmallFontRenderer();
        this.idleColour = new Color(163, 163, 163);
        this.downColour = new Color(255, 255, 255);
        this.lastMS = System.currentTimeMillis();
    }
    
    @Override
    public void renderComponent(final EnumButton component, final FontRenderer aa) {
        if (System.currentTimeMillis() - this.lastMS > 3000L && this.modeComponent != null) {
            this.modeComponent = null;
        }
        int c = component.isPressed() ? 11184810 : 14540253;
        if (component.isHovered()) {
            c = (c & 0x7F7F7F) << 1;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        final int parts = component.getModes().length;
        final double step = component.getWidth() / (double)parts;
        final double startX = step * component.getIndex();
        final double endX = step * (component.getIndex() + 1);
        final int height = component.getHeight();
        final float downscale = 1.1f;
        GL11.glDisable(3553);
        GL11.glColor3f(ColourConverter.toF(ColourSet.enumColour.getRed()), ColourConverter.toF(ColourSet.enumColour.getGreen()), ColourConverter.toF(ColourSet.enumColour.getBlue()));
        GL11.glBegin(1);
        GL11.glVertex2d(startX, (double)(height / downscale));
        GL11.glVertex2d(endX, (double)(height / downscale));
        GL11.glEnd();
        if (this.modeComponent == null || !this.modeComponent.equals(component)) {
            this.smallFontRenderer.drawString(0, 0, c, component.getName());
            this.smallFontRenderer.drawString(component.getWidth() - this.smallFontRenderer.getStringWidth(component.getIndexMode()), 0, c, component.getIndexMode());
        }
        else {
            this.smallFontRenderer.drawString(component.getWidth() / 2 - this.smallFontRenderer.getStringWidth(component.getIndexMode()) / 2, 0, c, component.getIndexMode());
        }
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final EnumButton component) {
        int width = 0;
        for (final String s : component.getModes()) {
            width = Math.max(width, this.smallFontRenderer.getStringWidth(s));
        }
        component.setWidth(this.smallFontRenderer.getStringWidth(component.getName()) + width + 1);
        component.setHeight(this.smallFontRenderer.getFontHeight() + 2);
    }
    
    @Override
    public void handleAddComponent(final EnumButton component, final Container container) {
        component.addPoof((IPoof)new EnumButton.EnumbuttonIndexPoof<EnumButton, EnumButton.EnumbuttonIndexPoof.EnumbuttonInfo>() {
            public void execute(final EnumButton component, final EnumbuttonIndexPoof.EnumbuttonInfo info) {
                KamiEnumButtonUI.this.modeComponent = component;
                KamiEnumButtonUI.this.lastMS = System.currentTimeMillis();
            }
        });
    }
}
