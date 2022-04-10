//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import java.util.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.block.state.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;

@Info(name = "Surround", category = Category.HIDDEN, description = "Surrounds you with obsidian to take less damage")
public class Surround extends Module
{
    private List whiteList;
    private Setting sneak;
    private Setting rotate;
    private Setting disableAfterPlacing;
    
    public Surround() {
        this.whiteList = Arrays.asList(Blocks.OBSIDIAN, Blocks.ENDER_CHEST);
        this.sneak = this.register(Settings.b("SneakOnly", false));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.disableAfterPlacing = this.register(Settings.b("DisableAfterPlacing", true));
    }
    
    public static boolean hasNeighbour(final BlockPos blockPos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = blockPos.offset(side);
            if (!Surround.mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onUpdate() {
        if ((!this.sneak.getValue() || Surround.mc.gameSettings.keyBindSneak.isKeyDown()) && this.isEnabled() && Surround.mc.player != null) {
            final Vec3d vec3d = getInterpolatedPos((Entity)Surround.mc.player, 0.0f);
            BlockPos northBlockPos = new BlockPos(vec3d).north();
            BlockPos southBlockPos = new BlockPos(vec3d).south();
            BlockPos eastBlockPos = new BlockPos(vec3d).east();
            BlockPos westBlockPos = new BlockPos(vec3d).west();
            int newSlot = -1;
            for (int oldSlot = 0; oldSlot < 9; ++oldSlot) {
                final ItemStack stack = Surround.mc.player.inventory.getStackInSlot(oldSlot);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (this.whiteList.contains(block)) {
                        newSlot = oldSlot;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                final int oldSlot = Surround.mc.player.inventory.currentItem;
                Surround.mc.player.inventory.currentItem = newSlot;
                Label_0293: {
                    if (!hasNeighbour(northBlockPos)) {
                        for (final EnumFacing side : EnumFacing.values()) {
                            final BlockPos neighbour = northBlockPos.offset(side);
                            if (hasNeighbour(neighbour)) {
                                northBlockPos = neighbour;
                                break Label_0293;
                            }
                        }
                        return;
                    }
                }
                Label_0356: {
                    if (!hasNeighbour(southBlockPos)) {
                        for (final EnumFacing side : EnumFacing.values()) {
                            final BlockPos neighbour = southBlockPos.offset(side);
                            if (hasNeighbour(neighbour)) {
                                southBlockPos = neighbour;
                                break Label_0356;
                            }
                        }
                        return;
                    }
                }
                Label_0422: {
                    if (!hasNeighbour(eastBlockPos)) {
                        for (final EnumFacing side : EnumFacing.values()) {
                            final BlockPos neighbour = eastBlockPos.offset(side);
                            if (hasNeighbour(neighbour)) {
                                eastBlockPos = neighbour;
                                break Label_0422;
                            }
                        }
                        return;
                    }
                }
                Label_0488: {
                    if (!hasNeighbour(westBlockPos)) {
                        for (final EnumFacing side : EnumFacing.values()) {
                            final BlockPos neighbour = westBlockPos.offset(side);
                            if (hasNeighbour(neighbour)) {
                                westBlockPos = neighbour;
                                break Label_0488;
                            }
                        }
                        return;
                    }
                }
                if (Surround.mc.world.getBlockState(northBlockPos).getMaterial().isReplaceable() && this.isEntitiesEmpty(northBlockPos)) {
                    placeBlockScaffold(northBlockPos, this.rotate.getValue());
                }
                if (Surround.mc.world.getBlockState(southBlockPos).getMaterial().isReplaceable() && this.isEntitiesEmpty(southBlockPos)) {
                    placeBlockScaffold(southBlockPos, this.rotate.getValue());
                }
                if (Surround.mc.world.getBlockState(eastBlockPos).getMaterial().isReplaceable() && this.isEntitiesEmpty(eastBlockPos)) {
                    placeBlockScaffold(eastBlockPos, this.rotate.getValue());
                }
                if (Surround.mc.world.getBlockState(westBlockPos).getMaterial().isReplaceable() && this.isEntitiesEmpty(westBlockPos)) {
                    placeBlockScaffold(westBlockPos, this.rotate.getValue());
                }
                Surround.mc.player.inventory.currentItem = oldSlot;
                if (this.disableAfterPlacing.getValue()) {
                    this.disable();
                }
            }
        }
    }
    
    private boolean isEntitiesEmpty(final BlockPos pos) {
        final List entities = (List)Surround.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos)).stream().filter(e -> !(e instanceof EntityItem)).filter(e -> !(e instanceof EntityXPOrb)).collect(Collectors.toList());
        return entities.isEmpty();
    }
    
    public static boolean placeBlockScaffold(final BlockPos pos, final boolean rotate) {
        new Vec3d(Surround.mc.player.posX, Surround.mc.player.posY + Surround.mc.player.getEyeHeight(), Surround.mc.player.posZ);
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbor = pos.offset(side);
            final EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                final Vec3d hitVec = new Vec3d((Vec3i)neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }
                Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                processRightClickBlock(neighbor, side2, hitVec);
                Surround.mc.player.swingArm(EnumHand.MAIN_HAND);
                Surround.mc.rightClickDelayTimer = 0;
                Surround.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Surround.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                return true;
            }
        }
        return false;
    }
    
    private static PlayerControllerMP getPlayerController() {
        return Surround.mc.playerController;
    }
    
    public static void processRightClickBlock(final BlockPos pos, final EnumFacing side, final Vec3d hitVec) {
        getPlayerController().processRightClickBlock(Surround.mc.player, Surround.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return Surround.mc.world.getBlockState(pos);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = getNeededRotations2(vec);
        Surround.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], Surround.mc.player.onGround));
    }
    
    private static float[] getNeededRotations2(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { Surround.mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - Surround.mc.player.rotationYaw), Surround.mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - Surround.mc.player.rotationPitch) };
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(Surround.mc.player.posX, Surround.mc.player.posY + Surround.mc.player.getEyeHeight(), Surround.mc.player.posZ);
    }
    
    public static Vec3d getInterpolatedPos(final Entity entity, final float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }
    
    public static Vec3d getInterpolatedAmount(final Entity entity, final double x, final double y, final double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }
}
