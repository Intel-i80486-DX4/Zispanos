//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.client.gui.*;

@Info(name = "AutoRespawn", description = "Automatically respawn after dying", category = Category.HIDDEN)
public class AutoRespawn extends Module
{
    private Setting<Boolean> respawn;
    private Setting<Boolean> deathCoords;
    private Setting<Boolean> antiGlitchScreen;
    @EventHandler
    public Listener<GuiScreenEvent.Displayed> listener;
    
    public AutoRespawn() {
        this.respawn = this.register(Settings.b("Respawn", true));
        this.deathCoords = this.register(Settings.b("DeathCoords", true));
        this.antiGlitchScreen = this.register(Settings.b("Anti Glitch Screen", true));
        this.listener = (Listener<GuiScreenEvent.Displayed>)new Listener(event -> {
            if (!(event.getScreen() instanceof GuiGameOver)) {
                return;
            }
            if (this.deathCoords.getValue() && AutoRespawn.mc.player.getHealth() <= 0.0f) {
                Command.sendChatMessage(String.format("You died at x %d y %d z %d", (int)AutoRespawn.mc.player.posX, (int)AutoRespawn.mc.player.posY, (int)AutoRespawn.mc.player.posZ));
            }
            if (this.respawn.getValue() || (this.antiGlitchScreen.getValue() && AutoRespawn.mc.player.getHealth() > 0.0f)) {
                AutoRespawn.mc.player.respawnPlayer();
                AutoRespawn.mc.displayGuiScreen((GuiScreen)null);
            }
        }, new Predicate[0]);
    }
}
