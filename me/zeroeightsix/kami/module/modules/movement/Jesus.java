//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.event.events.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.event.*;
import net.minecraft.network.play.client.*;
import net.minecraft.block.*;

@Info(name = "Jesus", description = "Allows you to walk on water", category = Category.MOVEMENT)
public class Jesus extends Module
{
    private static final AxisAlignedBB WATER_WALK_AA;
    @EventHandler
    Listener<AddCollisionBoxToListEvent> addCollisionBoxToListEventListener;
    @EventHandler
    Listener<PacketEvent.Send> packetEventSendListener;
    
    public Jesus() {
        this.addCollisionBoxToListEventListener = (Listener<AddCollisionBoxToListEvent>)new Listener(event -> {
            if (Jesus.mc.player != null && event.getBlock() instanceof BlockLiquid && (EntityUtil.isDrivenByPlayer(event.getEntity()) || event.getEntity() == Jesus.mc.player) && !(event.getEntity() instanceof EntityBoat) && !Jesus.mc.player.isSneaking() && Jesus.mc.player.fallDistance < 3.0f && !EntityUtil.isInWater((Entity)Jesus.mc.player) && (EntityUtil.isAboveWater((Entity)Jesus.mc.player, false) || EntityUtil.isAboveWater(Jesus.mc.player.getRidingEntity(), false)) && isAboveBlock((Entity)Jesus.mc.player, event.getPos())) {
                final AxisAlignedBB axisalignedbb = Jesus.WATER_WALK_AA.offset(event.getPos());
                if (event.getEntityBox().intersects(axisalignedbb)) {
                    event.getCollidingBoxes().add(axisalignedbb);
                }
                event.cancel();
            }
        }, new Predicate[0]);
        this.packetEventSendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getEra() == KamiEvent.Era.PRE && event.getPacket() instanceof CPacketPlayer && EntityUtil.isAboveWater((Entity)Jesus.mc.player, true) && !EntityUtil.isInWater((Entity)Jesus.mc.player) && !isAboveLand((Entity)Jesus.mc.player)) {
                final int ticks = Jesus.mc.player.ticksExisted % 2;
                if (ticks == 0) {
                    final CPacketPlayer cPacketPlayer = (CPacketPlayer)event.getPacket();
                    cPacketPlayer.y += 0.02;
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (!ModuleManager.isModuleEnabled("Freecam") && EntityUtil.isInWater((Entity)Jesus.mc.player) && !Jesus.mc.player.isSneaking()) {
            Jesus.mc.player.motionY = 0.1;
            if (Jesus.mc.player.getRidingEntity() != null && !(Jesus.mc.player.getRidingEntity() instanceof EntityBoat)) {
                Jesus.mc.player.getRidingEntity().motionY = 0.3;
            }
        }
    }
    
    private static boolean isAboveLand(final Entity entity) {
        if (entity == null) {
            return false;
        }
        final double y = entity.posY - 0.01;
        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); ++x) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); ++z) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (Wrapper.getWorld().getBlockState(pos).getBlock().isFullBlock(Wrapper.getWorld().getBlockState(pos))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isAboveBlock(final Entity entity, final BlockPos pos) {
        return entity.posY >= pos.getY();
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
}
