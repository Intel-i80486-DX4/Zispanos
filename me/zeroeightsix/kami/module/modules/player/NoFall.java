//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;

@Info(category = Category.PLAYER, description = "Prevents fall damage", name = "NoFall")
public class NoFall extends Module
{
    private Setting<FallMode> fallMode;
    private Setting<Boolean> pickup;
    private Setting<Integer> distance;
    private Setting<Integer> pickupDelay;
    private long last;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    
    public NoFall() {
        this.fallMode = this.register(Settings.e("Mode", FallMode.PACKET));
        this.pickup = this.register(Settings.booleanBuilder("Pickup").withValue(true).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET)).build());
        this.distance = this.register(Settings.integerBuilder("Distance").withValue(3).withMinimum(1).withMaximum(10).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET)).build());
        this.pickupDelay = this.register(Settings.integerBuilder("Pickup Delay").withValue(300).withMinimum(100).withMaximum(1000).withVisibility(v -> this.fallMode.getValue().equals(FallMode.BUCKET) && this.pickup.getValue()).build());
        this.last = 0L;
        this.sendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (this.fallMode.getValue().equals(FallMode.PACKET) && event.getPacket() instanceof CPacketPlayer) {
                ((CPacketPlayer)event.getPacket()).onGround = true;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.fallMode.getValue().equals(FallMode.BUCKET) && NoFall.mc.player.fallDistance >= this.distance.getValue() && !EntityUtil.isAboveWater((Entity)NoFall.mc.player) && System.currentTimeMillis() - this.last > 100L) {
            final Vec3d posVec = NoFall.mc.player.getPositionVector();
            final RayTraceResult result = NoFall.mc.world.rayTraceBlocks(posVec, posVec.add(0.0, -5.329999923706055, 0.0), true, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (NoFall.mc.player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET) {
                    hand = EnumHand.OFF_HAND;
                }
                else if (NoFall.mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET) {
                    for (int i = 0; i < 9; ++i) {
                        if (NoFall.mc.player.inventory.getStackInSlot(i).getItem() == Items.WATER_BUCKET) {
                            NoFall.mc.player.inventory.currentItem = i;
                            NoFall.mc.player.rotationPitch = 90.0f;
                            this.last = System.currentTimeMillis();
                            return;
                        }
                    }
                    return;
                }
                NoFall.mc.player.rotationPitch = 90.0f;
                NoFall.mc.playerController.processRightClick((EntityPlayer)NoFall.mc.player, (World)NoFall.mc.world, hand);
            }
            if (this.pickup.getValue()) {
                new Thread(() -> {
                    try {
                        Thread.sleep(this.pickupDelay.getValue());
                    }
                    catch (InterruptedException ex) {}
                    NoFall.mc.player.rotationPitch = 90.0f;
                    NoFall.mc.rightClickMouse();
                }).start();
            }
        }
    }
    
    private enum FallMode
    {
        BUCKET, 
        PACKET;
    }
}
