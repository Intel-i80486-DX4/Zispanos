//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui.kami.theme.kami;

import me.zeroeightsix.kami.gui.rgui.render.theme.*;
import me.zeroeightsix.kami.gui.rgui.render.font.*;
import me.zeroeightsix.kami.gui.kami.theme.staticui.*;
import me.zeroeightsix.kami.gui.kami.*;
import me.zeroeightsix.kami.gui.rgui.render.*;

public class KamiTheme extends AbstractTheme
{
    FontRenderer fontRenderer;
    
    public KamiTheme() {
        this.installUI(new RootButtonUI<Object>());
        this.installUI(new GUIUI());
        this.installUI(new RootGroupboxUI());
        this.installUI(new KamiFrameUI<Object>());
        this.installUI(new RootScrollpaneUI());
        this.installUI(new RootInputFieldUI<Object>());
        this.installUI(new RootLabelUI<Object>());
        this.installUI(new RootChatUI());
        this.installUI(new RootCheckButtonUI<Object>());
        this.installUI(new KamiActiveModulesUI());
        this.installUI(new KamiSettingsPanelUI());
        this.installUI(new RootSliderUI());
        this.installUI(new KamiEnumButtonUI());
        this.installUI(new RootColorizedCheckButtonUI());
        this.installUI(new KamiUnboundSliderUI());
        this.installUI(new RadarUI());
        this.installUI(new TabGuiUI());
        this.fontRenderer = KamiGUI.fontRenderer;
    }
    
    @Override
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public class GUIUI extends AbstractComponentUI<KamiGUI>
    {
    }
}
