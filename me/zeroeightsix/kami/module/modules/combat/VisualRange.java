//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import me.zeroeightsix.kami.util.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import java.util.*;

@Info(name = "VisualRange", description = "Shows players who enter and leave range in chat", category = Category.COMBAT)
public class VisualRange extends Module
{
    private Setting<Boolean> leaving;
    private Setting<Boolean> uwuAura;
    private List<String> knownPlayers;
    
    public VisualRange() {
        this.leaving = this.register(Settings.b("Leaving", false));
        this.uwuAura = this.register(Settings.b("UwU Aura", false));
    }
    
    @Override
    public void onUpdate() {
        if (VisualRange.mc.player == null) {
            return;
        }
        final List<String> tickPlayerList = new ArrayList<String>();
        for (final Entity entity : VisualRange.mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
                tickPlayerList.add(entity.getName());
            }
        }
        if (tickPlayerList.size() > 0) {
            for (final String playerName : tickPlayerList) {
                if (playerName.equals(VisualRange.mc.player.getName())) {
                    continue;
                }
                if (!this.knownPlayers.contains(playerName)) {
                    this.knownPlayers.add(playerName);
                    if (Friends.isFriend(playerName)) {
                        this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    else {
                        this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }
                    if (this.uwuAura.getValue()) {
                        Command.sendServerMessage("/w " + playerName + " hi uwu");
                    }
                    return;
                }
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (final String playerName : this.knownPlayers) {
                if (!tickPlayerList.contains(playerName)) {
                    this.knownPlayers.remove(playerName);
                    if (this.leaving.getValue()) {
                        if (Friends.isFriend(playerName)) {
                            this.sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        else {
                            this.sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                        if (this.uwuAura.getValue()) {
                            Command.sendServerMessage("/w " + playerName + " bye uwu");
                        }
                    }
                }
            }
        }
    }
    
    private void sendNotification(final String s) {
        Command.sendChatMessage(s);
    }
    
    public void onEnable() {
        this.knownPlayers = new ArrayList<String>();
    }
}
