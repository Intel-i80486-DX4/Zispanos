//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.event.*;
import net.minecraft.network.play.server.*;

@Info(name = "Velocity", description = "Modify knockback impact", category = Category.MOVEMENT)
public class Velocity extends Module
{
    private Setting<Boolean> noPush;
    private Setting<Float> horizontal;
    private Setting<Float> vertical;
    @EventHandler
    private Listener<PacketEvent.Receive> packetEventListener;
    @EventHandler
    private Listener<EntityEvent.EntityCollision> entityCollisionListener;
    
    public Velocity() {
        this.noPush = this.register(Settings.b("NoPush", true));
        this.horizontal = this.register(Settings.f("Horizontal", 0.0f));
        this.vertical = this.register(Settings.f("Vertical", 0.0f));
        this.packetEventListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (event.getEra() == KamiEvent.Era.PRE) {
                if (event.getPacket() instanceof SPacketEntityVelocity) {
                    final SPacketEntityVelocity velocity = (SPacketEntityVelocity)event.getPacket();
                    if (velocity.getEntityID() == Velocity.mc.player.entityId) {
                        if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                            event.cancel();
                        }
                        final SPacketEntityVelocity sPacketEntityVelocity = velocity;
                        sPacketEntityVelocity.motionX *= (int)(Object)this.horizontal.getValue();
                        final SPacketEntityVelocity sPacketEntityVelocity2 = velocity;
                        sPacketEntityVelocity2.motionY *= (int)(Object)this.vertical.getValue();
                        final SPacketEntityVelocity sPacketEntityVelocity3 = velocity;
                        sPacketEntityVelocity3.motionZ *= (int)(Object)this.horizontal.getValue();
                    }
                }
                else if (event.getPacket() instanceof SPacketExplosion) {
                    if (this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) {
                        event.cancel();
                    }
                    final SPacketExplosion sPacketExplosion;
                    final SPacketExplosion velocity2 = sPacketExplosion = (SPacketExplosion)event.getPacket();
                    sPacketExplosion.motionX *= this.horizontal.getValue();
                    final SPacketExplosion sPacketExplosion2 = velocity2;
                    sPacketExplosion2.motionY *= this.vertical.getValue();
                    final SPacketExplosion sPacketExplosion3 = velocity2;
                    sPacketExplosion3.motionZ *= this.horizontal.getValue();
                }
            }
        }, new Predicate[0]);
        this.entityCollisionListener = (Listener<EntityEvent.EntityCollision>)new Listener(event -> {
            if (event.getEntity() == Velocity.mc.player) {
                if ((this.horizontal.getValue() == 0.0f && this.vertical.getValue() == 0.0f) || this.noPush.getValue()) {
                    event.cancel();
                    return;
                }
                event.setX(-event.getX() * this.horizontal.getValue());
                event.setY(0.0);
                event.setZ(-event.getZ() * this.horizontal.getValue());
            }
        }, new Predicate[0]);
    }
}
