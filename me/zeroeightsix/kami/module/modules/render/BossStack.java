//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "BossStack", description = "Modify the boss health GUI to take up less space", category = Category.MISC)
public class BossStack extends Module
{
    private static Setting<BossStackMode> mode;
    private static Setting<Double> scale;
    private static final ResourceLocation GUI_BARS_TEXTURES;
    
    public BossStack() {
        this.registerAll(BossStack.mode, BossStack.scale);
    }
    
    public static void render(final RenderGameOverlayEvent.Post event) {
        if (BossStack.mode.getValue() == BossStackMode.MINIMIZE) {
            final Map<UUID, BossInfoClient> map = (Map<UUID, BossInfoClient>)Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            if (map == null) {
                return;
            }
            final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
            final int i = scaledresolution.getScaledWidth();
            int j = 12;
            for (final Map.Entry<UUID, BossInfoClient> entry : map.entrySet()) {
                final BossInfoClient info = entry.getValue();
                final String text = info.getName().getFormattedText();
                final int k = (int)(i / BossStack.scale.getValue() / 2.0 - 91.0);
                GL11.glScaled((double)BossStack.scale.getValue(), (double)BossStack.scale.getValue(), 1.0);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(k, j, (BossInfo)info);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float)(i / BossStack.scale.getValue() / 2.0 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2), (float)(j - 9), 16777215);
                }
                GL11.glScaled(1.0 / BossStack.scale.getValue(), 1.0 / BossStack.scale.getValue(), 1.0);
                j += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            }
        }
        else if (BossStack.mode.getValue() == BossStackMode.STACK) {
            final Map<UUID, BossInfoClient> map = (Map<UUID, BossInfoClient>)Minecraft.getMinecraft().ingameGUI.getBossOverlay().mapBossInfos;
            final HashMap<String, Pair<BossInfoClient, Integer>> to = new HashMap<String, Pair<BossInfoClient, Integer>>();
            for (final Map.Entry<UUID, BossInfoClient> entry2 : map.entrySet()) {
                final String s = entry2.getValue().getName().getFormattedText();
                if (to.containsKey(s)) {
                    Pair<BossInfoClient, Integer> p = to.get(s);
                    p = (Pair<BossInfoClient, Integer>)new Pair(p.getKey(), (Object)((int)p.getValue() + 1));
                    to.put(s, p);
                }
                else {
                    final Pair<BossInfoClient, Integer> p = (Pair<BossInfoClient, Integer>)new Pair((Object)entry2.getValue(), (Object)1);
                    to.put(s, p);
                }
            }
            final ScaledResolution scaledresolution2 = new ScaledResolution(Minecraft.getMinecraft());
            final int l = scaledresolution2.getScaledWidth();
            int m = 12;
            for (final Map.Entry<String, Pair<BossInfoClient, Integer>> entry3 : to.entrySet()) {
                String text = entry3.getKey();
                final BossInfoClient info2 = (BossInfoClient)entry3.getValue().getKey();
                final int a = (int)entry3.getValue().getValue();
                text = text + " x" + a;
                final int k2 = (int)(l / BossStack.scale.getValue() / 2.0 - 91.0);
                GL11.glScaled((double)BossStack.scale.getValue(), (double)BossStack.scale.getValue(), 1.0);
                if (!event.isCanceled()) {
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    Minecraft.getMinecraft().getTextureManager().bindTexture(BossStack.GUI_BARS_TEXTURES);
                    Minecraft.getMinecraft().ingameGUI.getBossOverlay().render(k2, m, (BossInfo)info2);
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (float)(l / BossStack.scale.getValue() / 2.0 - Minecraft.getMinecraft().fontRenderer.getStringWidth(text) / 2), (float)(m - 9), 16777215);
                }
                GL11.glScaled(1.0 / BossStack.scale.getValue(), 1.0 / BossStack.scale.getValue(), 1.0);
                m += 10 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
            }
        }
    }
    
    static {
        BossStack.mode = Settings.e("Mode", BossStackMode.STACK);
        BossStack.scale = (Setting<Double>)Settings.doubleBuilder().withName("Scale").withMinimum(0.1).withValue(0.5).build();
        GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    }
    
    private enum BossStackMode
    {
        REMOVE, 
        STACK, 
        MINIMIZE;
    }
}
