//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.component.use.*;
import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import me.zeroeightsix.kami.util.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.kami.*;
import me.zeroeightsix.kami.gui.rgui.component.container.*;
import me.zeroeightsix.kami.gui.rgui.component.*;

public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton>
{
    @Override
    public void renderComponent(final CheckButton component, final FontRenderer ff) {
        GL11.glColor4f(ColourConverter.toF(ColourSet.bgColour.getRed()), ColourConverter.toF(ColourSet.bgColour.getGreen()), ColourConverter.toF(ColourSet.bgColour.getBlue()), component.getOpacity());
        if (component.isToggled()) {
            GL11.glColor3f(ColourConverter.toF(ColourSet.bgColourOther), ColourConverter.toF(ColourSet.bgColour.getGreen()), ColourConverter.toF(ColourSet.bgColour.getBlue()));
        }
        if (component.isHovered() || component.isPressed()) {
            GL11.glColor4f(ColourConverter.toF(ColourSet.bgColourHover.getRed()), ColourConverter.toF(ColourSet.bgColourHover.getGreen()), ColourConverter.toF(ColourSet.bgColourHover.getBlue()), component.getOpacity());
        }
        final String text = component.getName();
        int c = component.isPressed() ? ColourConverter.rgbToInt(ColourSet.buttonPressed.getRed(), ColourSet.buttonPressed.getGreen(), ColourSet.buttonPressed.getBlue()) : (component.isToggled() ? ColourConverter.rgbToInt(ColourSet.buttonIdleT.getRed(), ColourSet.buttonIdleT.getGreen(), ColourSet.buttonIdleT.getBlue()) : ColourConverter.rgbToInt(ColourSet.buttonHoveredT.getRed(), ColourSet.buttonHoveredT.getGreen(), ColourSet.buttonHoveredT.getBlue()));
        if (component.isHovered()) {
            c = (c & ColourConverter.rgbToInt(ColourSet.buttonHoveredN.getRed(), ColourSet.buttonHoveredN.getGreen(), ColourSet.buttonHoveredN.getBlue())) << 1;
        }
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        KamiGUI.fontRenderer.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(text) / 2, KamiGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
        GL11.glDisable(3553);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleAddComponent(final CheckButton component, final Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight() + 2);
    }
}
