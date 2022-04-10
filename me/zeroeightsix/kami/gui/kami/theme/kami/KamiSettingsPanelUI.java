//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.kami.component.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.module.modules.experimental.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.gui.kami.*;
import me.zeroeightsix.kami.gui.rgui.component.*;

public class KamiSettingsPanelUI extends AbstractComponentUI<SettingsPanel>
{
    @Override
    public void renderComponent(final SettingsPanel component, final FontRenderer fontRenderer) {
        super.renderComponent(component, fontRenderer);
        GL11.glLineWidth(2.0f);
        final float red = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).red.getValue() / 255.0f;
        final float green = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).green.getValue() / 255.0f;
        final float blue = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).blue.getValue() / 255.0f;
        final float alpha = ((GUIColour)ModuleManager.getModuleByName("GUI Colour")).alpha.getValue() / 255.0f;
        if (ModuleManager.getModuleByName("GUI Colour").isEnabled()) {
            GL11.glColor4f(red, green, blue, alpha);
        }
        else {
            GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        }
        RenderHelper.drawFilledRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
        GL11.glColor3f(2.55f, 0.0f, 0.0f);
        GL11.glLineWidth(1.5f);
        RenderHelper.drawRectangle(0.0f, 0.0f, (float)component.getWidth(), (float)component.getHeight());
    }
}
