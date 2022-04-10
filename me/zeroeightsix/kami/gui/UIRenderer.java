//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.gui;

import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.*;
import org.lwjgl.opengl.*;
import me.zeroeightsix.kami.gui.rgui.component.*;
import me.zeroeightsix.kami.gui.rgui.component.container.use.*;
import net.minecraft.client.renderer.*;
import me.zeroeightsix.kami.gui.rgui.component.listen.*;
import me.zeroeightsix.kami.gui.kami.*;
import java.util.*;

public class UIRenderer
{
    public static void renderAndUpdateFrames() {
        if (Wrapper.getMinecraft().currentScreen instanceof DisplayGuiScreen || Wrapper.getMinecraft().gameSettings.showDebugInfo) {
            return;
        }
        final KamiGUI gui = KamiMod.getInstance().getGuiManager();
        GL11.glDisable(3553);
        for (final Component c : gui.getChildren()) {
            if (c instanceof Frame) {
                GlStateManager.pushMatrix();
                final Frame child = (Frame)c;
                if (child.isPinned() && child.isVisible()) {
                    final boolean slide = child.getOpacity() != 0.0f;
                    GL11.glTranslated((double)child.getX(), (double)child.getY(), 0.0);
                    child.getRenderListeners().forEach(renderListener -> renderListener.onPreRender());
                    child.getTheme().getUIForComponent(child).renderComponent(child, child.getTheme().getFontRenderer());
                    int translateX = 0;
                    int translateY = 0;
                    if (slide) {
                        translateX += child.getOriginOffsetX();
                        translateY += child.getOriginOffsetY();
                    }
                    else {
                        if (child.getDocking().isBottom()) {
                            translateY += child.getOriginOffsetY();
                        }
                        if (child.getDocking().isRight()) {
                            translateX += child.getOriginOffsetX();
                            if (child.getChildren().size() > 0) {
                                translateX += (child.getWidth() - child.getChildren().get(0).getX() - child.getChildren().get(0).getWidth()) / DisplayGuiScreen.getScale();
                            }
                        }
                        if (child.getDocking().isLeft() && child.getChildren().size() > 0) {
                            translateX -= child.getChildren().get(0).getX();
                        }
                        if (child.getDocking().isTop() && child.getChildren().size() > 0) {
                            translateY -= child.getChildren().get(0).getY();
                        }
                    }
                    GL11.glTranslated((double)translateX, (double)translateY, 0.0);
                    child.getRenderListeners().forEach(RenderListener::onPostRender);
                    child.renderChildren();
                    GL11.glTranslated((double)(-translateX), (double)(-translateY), 0.0);
                    GL11.glTranslated((double)(-child.getX()), (double)(-child.getY()), 0.0);
                }
                GlStateManager.popMatrix();
            }
        }
        GL11.glEnable(3553);
        GL11.glEnable(3042);
        GlStateManager.enableBlend();
    }
}
