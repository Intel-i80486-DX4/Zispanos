//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import net.minecraft.client.gui.*;

@Info(name = "AntiDeathScreen", description = "Fixes random death screen glitches", category = Category.COMBAT)
public class AntiDeathScreen extends Module
{
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AntiDeathScreen() {
        this.listener = (Listener<GuiScreenEvent.Displayed>)new Listener(event -> {
            if (!(event.getScreen() instanceof GuiGameOver)) {
                return;
            }
            if (AntiDeathScreen.mc.player.getHealth() > 0.0f) {
                AntiDeathScreen.mc.player.respawnPlayer();
                AntiDeathScreen.mc.displayGuiScreen((GuiScreen)null);
            }
        }, new Predicate[0]);
    }
}
