//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.entity.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.player.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.world.*;

@Info(name = "TotemPopCount", category = Category.COMBAT)
public class TotemPopCount extends Module
{
    public Map popList;
    private Setting chatMessages;
    private Setting ignoreSelf;
    @EventHandler
    private Listener damageListener;
    
    public TotemPopCount() {
        this.popList = new HashMap();
        this.chatMessages = this.register(Settings.b("Chat Messages", true));
        this.ignoreSelf = this.register(Settings.b("Ignore Self", true));
        this.damageListener = new Listener(event -> {
            if (TotemPopCount.mc.player != null && TotemPopCount.mc.world != null && event.getPacket() instanceof SPacketEntityStatus) {
                final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
                if (packet.getOpCode() == 35) {
                    final Entity entity = packet.getEntity((World)TotemPopCount.mc.world);
                    this.popTotem(entity);
                }
            }
        }, new Predicate[0]);
    }
    
    private void popTotem(final Entity entity) {
        final String name = entity.getName();
        int count = 1;
        if (this.popList.get(name) == null) {
            this.popList.put(name, 1);
        }
        else {
            count = this.popList.get(name) + 1;
            this.popList.put(name, count);
        }
        if ((!entity.getName().equals(TotemPopCount.mc.player.getName()) || !this.ignoreSelf.getValue()) && this.chatMessages.getValue()) {
            Command.sendChatMessage("&d" + name + " popped " + count + " totems!");
        }
    }
    
    @Override
    public void onUpdate() {
        if (TotemPopCount.mc.world != null) {
            for (final EntityPlayer player : TotemPopCount.mc.world.playerEntities) {
                if (player.getHealth() <= 0.0f && this.popList.containsKey(player.getName())) {
                    if ((!player.getName().equals(TotemPopCount.mc.player.getName()) || !this.ignoreSelf.getValue()) && this.chatMessages.getValue()) {
                        Command.sendChatMessage("&d" + player.getName() + " died after popping " + this.popList.get(player.getName()) + " totems!");
                    }
                    this.popList.remove(player.getName());
                }
            }
        }
    }
}
