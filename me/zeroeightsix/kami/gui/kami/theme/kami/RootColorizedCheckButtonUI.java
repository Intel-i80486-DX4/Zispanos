//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.kami.component.*;
import me.zeroeightsix.kami.util.*;
import java.awt.*;
import me.zeroeightsix.kami.gui.rgui.component.use.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.kami.*;
import me.zeroeightsix.kami.gui.rgui.component.*;

public class RootColorizedCheckButtonUI extends RootCheckButtonUI<ColorizedCheckButton>
{
    RootSmallFontRenderer ff;
    
    public RootColorizedCheckButtonUI() {
        this.ff = new RootSmallFontRenderer();
        ColourSet.bgColour = new Color(ColourConverter.toF(200), ColourConverter.toF(ColourSet.bgColour.getGreen()), ColourConverter.toF(ColourSet.bgColour.getBlue()));
        ColourSet.bgColourHover = new Color(ColourConverter.toF(255), ColourConverter.toF(ColourSet.bgColourHover.getGreen()), ColourConverter.toF(ColourSet.bgColourHover.getBlue()));
    }
    
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer aa) {
        GL11.glColor4f(ColourConverter.toF(ColourSet.bgColour.getRed()), ColourConverter.toF(ColourSet.bgColour.getGreen()), ColourConverter.toF(ColourSet.bgColour.getBlue()), component.getOpacity());
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(ColourConverter.toF(ColourSet.bgColourHover.getRed()), ColourConverter.toF(ColourSet.bgColourHover.getGreen()), ColourConverter.toF(ColourSet.bgColourHover.getBlue()), component.getOpacity());
        }
        if (component.isToggled()) {
            GL11.glColor3f(ColourConverter.toF(ColourSet.bgColour.getRed()), ColourConverter.toF(ColourSet.bgColour.getGreen()), ColourConverter.toF(ColourSet.bgColour.getBlue()));
        }
        GL11.glLineWidth(2.5f);
        GL11.glBegin(1);
        GL11.glVertex2d(0.0, (double)component.getHeight());
        GL11.glVertex2d((double)component.getWidth(), (double)component.getHeight());
        GL11.glEnd();
        final Color idleColour = component.isToggled() ? ColourSet.buttonIdleT : ColourSet.buttonIdleN;
        final Color downColour = component.isToggled() ? ColourSet.buttonHoveredT : ColourSet.buttonHoveredN;
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        this.ff.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        GL11.glDisable(3553);
    }
}
