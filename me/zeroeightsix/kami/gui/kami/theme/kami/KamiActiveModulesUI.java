//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.render.*;
import me.zeroeightsix.kami.gui.kami.component.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;
import java.util.stream.*;
import me.zeroeightsix.kami.util.*;
import java.awt.*;
import java.util.function.*;
import me.zeroeightsix.kami.gui.rgui.component.*;

public class KamiActiveModulesUI extends AbstractComponentUI<ActiveModules>
{
    me.zeroeightsix.kami.module.modules.gui.ActiveModules activeMods;
    
    @Override
    public void renderComponent(final ActiveModules component, final FontRenderer f) {
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glEnable(3553);
        final FontRenderer renderer = Wrapper.getFontRenderer();
        String string;
        final FontRenderer fontRenderer;
        final StringBuilder sb;
        final List<Module> mods = (List<Module>)ModuleManager.getModules().stream().filter(Module::isEnabled).filter(Module::isOnArray).sorted(Comparator.comparing(module -> {
            new StringBuilder().append(module.getName());
            if (module.getHudInfo() == null) {
                string = "";
            }
            else {
                string = module.getHudInfo() + " ";
            }
            return Integer.valueOf(fontRenderer.getStringWidth(sb.append(string).toString()) * (component.sort_up ? -1 : 1));
        })).collect(Collectors.toList());
        final int[] y = { 2 };
        this.activeMods = (me.zeroeightsix.kami.module.modules.gui.ActiveModules)ModuleManager.getModuleByName("ActiveModules");
        if (component.getParent().getY() < 26 && Wrapper.getPlayer().getActivePotionEffects().size() > 0 && component.getParent().getOpacity() == 0.0f) {
            y[0] = Math.max(component.getParent().getY(), 26 - component.getParent().getY());
        }
        final float[] hue = { System.currentTimeMillis() % (360 * this.activeMods.getRainbowSpeed()) / (360.0f * this.activeMods.getRainbowSpeed()) };
        Function<Integer, Integer> xFunc = null;
        switch (component.getAlignment()) {
            case RIGHT: {
                xFunc = (Function<Integer, Integer>)(i -> component.getWidth() - i);
                break;
            }
            case CENTER: {
                xFunc = (Function<Integer, Integer>)(i -> component.getWidth() / 2 - i / 2);
                break;
            }
            default: {
                xFunc = (Function<Integer, Integer>)(i -> 0);
                break;
            }
        }
        for (int j = 0; j < mods.size(); ++j) {
            final Module module2 = mods.get(j);
            int rgb;
            if (this.activeMods.mode.getValue().equals((Object)me.zeroeightsix.kami.module.modules.gui.ActiveModules.Mode.RAINBOW)) {
                rgb = Color.HSBtoRGB(hue[0], ColourConverter.toF((int)this.activeMods.saturationR.getValue()), ColourConverter.toF((int)this.activeMods.brightnessR.getValue()));
            }
            else if (this.activeMods.mode.getValue().equals((Object)me.zeroeightsix.kami.module.modules.gui.ActiveModules.Mode.CATEGORY)) {
                rgb = me.zeroeightsix.kami.module.modules.gui.ActiveModules.getCategoryColour(module2);
            }
            else if (this.activeMods.mode.getValue().equals((Object)me.zeroeightsix.kami.module.modules.gui.ActiveModules.Mode.CUSTOM)) {
                rgb = Color.HSBtoRGB(ColourConverter.toF((int)this.activeMods.hueC.getValue()), ColourConverter.toF((int)this.activeMods.saturationC.getValue()), ColourConverter.toF((int)this.activeMods.brightnessC.getValue()));
            }
            else {
                rgb = this.activeMods.getInfoColour(j);
            }
            final String hudInfo = module2.getHudInfo();
            final String text = this.activeMods.fHax() + module2.getName() + ((hudInfo == null) ? "" : (" §c" + hudInfo));
            final int textWidth = renderer.getStringWidth(text);
            final int textHeight = renderer.getFontHeight() + 1;
            final int red = rgb >> 16 & 0xFF;
            final int green = rgb >> 8 & 0xFF;
            final int blue = rgb & 0xFF;
            renderer.drawStringWithShadow(xFunc.apply(textWidth), y[0], red, green, blue, text);
            final float[] array = hue;
            final int n = 0;
            array[n] += 0.02f;
            final int[] array2 = y;
            final int n2 = 0;
            array2[n2] += textHeight;
        }
        component.setHeight(y[0]);
        GL11.glEnable(2884);
        GL11.glDisable(3042);
    }
    
    @Override
    public void handleSizeComponent(final ActiveModules component) {
        component.setWidth(100);
        component.setHeight(100);
    }
}
