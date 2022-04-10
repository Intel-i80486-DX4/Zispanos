//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import net.minecraftforge.client.event.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;

@Info(name = "NoRender", category = Category.RENDER, description = "Ignore entity spawn packets")
public class NoRender extends Module
{
    private Setting<Boolean> mob;
    private Setting<Boolean> sand;
    private Setting<Boolean> gentity;
    private Setting<Boolean> object;
    private Setting<Boolean> xp;
    private Setting<Boolean> paint;
    private Setting<Boolean> fire;
    private Setting<Boolean> explosion;
    @EventHandler
    public Listener<PacketEvent.Receive> receiveListener;
    @EventHandler
    public Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
    
    public NoRender() {
        this.mob = this.register(Settings.b("Mob", false));
        this.sand = this.register(Settings.b("Sand", false));
        this.gentity = this.register(Settings.b("GEntity", false));
        this.object = this.register(Settings.b("Object", false));
        this.xp = this.register(Settings.b("XP", false));
        this.paint = this.register(Settings.b("Paintings", false));
        this.fire = this.register(Settings.b("Fire"));
        this.explosion = this.register(Settings.b("Explosions"));
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if ((packet instanceof SPacketSpawnMob && this.mob.getValue()) || (packet instanceof SPacketSpawnGlobalEntity && this.gentity.getValue()) || (packet instanceof SPacketSpawnObject && this.object.getValue()) || (packet instanceof SPacketSpawnExperienceOrb && this.xp.getValue()) || (packet instanceof SPacketSpawnObject && this.sand.getValue()) || (packet instanceof SPacketExplosion && this.explosion.getValue()) || (packet instanceof SPacketSpawnPainting && this.paint.getValue())) {
                event.cancel();
            }
        }, new Predicate[0]);
        this.blockOverlayEventListener = (Listener<RenderBlockOverlayEvent>)new Listener(event -> {
            if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE) {
                event.setCanceled(true);
            }
        }, new Predicate[0]);
    }
}
