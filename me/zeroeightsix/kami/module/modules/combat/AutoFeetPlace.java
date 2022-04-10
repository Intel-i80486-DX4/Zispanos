//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import net.minecraft.block.state.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import me.zeroeightsix.kami.module.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.item.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import me.zeroeightsix.kami.module.modules.player.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.block.*;

@Info(name = "AutoFeetPlace", category = Category.HIDDEN, description = "Continually places obsidian around your feet")
public class AutoFeetPlace extends Module
{
    private Setting<Mode> mode;
    private Setting<Boolean> triggerable;
    private Setting<Boolean> disableNone;
    private Setting<Integer> timeoutTicks;
    private Setting<Integer> blocksPerTick;
    private Setting<Integer> tickDelay;
    private Setting<Boolean> rotate;
    private Setting<Boolean> infoMessage;
    private int offsetStep;
    private int delayStep;
    private int playerHotbarSlot;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int totalTicksRunning;
    private boolean firstRun;
    private boolean missingObiDisable;
    
    public AutoFeetPlace() {
        this.mode = this.register(Settings.e("Mode", Mode.FULL));
        this.triggerable = this.register(Settings.b("Triggerable", true));
        this.disableNone = this.register(Settings.b("DisableNoObby", true));
        this.timeoutTicks = this.register(Settings.integerBuilder("TimeoutTicks").withMinimum(1).withValue(40).withMaximum(100).withVisibility(b -> this.triggerable.getValue()).build());
        this.blocksPerTick = this.register((Setting<Integer>)Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(4).withMaximum(9).build());
        this.tickDelay = this.register((Setting<Integer>)Settings.integerBuilder("TickDelay").withMinimum(0).withValue(0).withMaximum(10).build());
        this.rotate = this.register(Settings.b("Rotate", true));
        this.infoMessage = this.register(Settings.b("InfoMessage", false));
        this.offsetStep = 0;
        this.delayStep = 0;
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.isSneaking = false;
        this.totalTicksRunning = 0;
        this.missingObiDisable = false;
    }
    
    private static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (AutoFeetPlace.mc.world.getBlockState(neighbour).getBlock().canCollideCheck(AutoFeetPlace.mc.world.getBlockState(neighbour), false)) {
                final IBlockState blockState = AutoFeetPlace.mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }
    
    @Override
    protected void onEnable() {
        if (AutoFeetPlace.mc.player == null) {
            this.disable();
            return;
        }
        this.firstRun = true;
        this.playerHotbarSlot = AutoFeetPlace.mc.player.inventory.currentItem;
        this.lastHotbarSlot = -1;
    }
    
    @Override
    protected void onDisable() {
        if (AutoFeetPlace.mc.player == null) {
            return;
        }
        if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
            AutoFeetPlace.mc.player.inventory.currentItem = this.playerHotbarSlot;
        }
        if (this.isSneaking) {
            AutoFeetPlace.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetPlace.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
        }
        this.playerHotbarSlot = -1;
        this.lastHotbarSlot = -1;
        this.missingObiDisable = false;
    }
    
    @Override
    public void onUpdate() {
        if (AutoFeetPlace.mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }
        if (this.triggerable.getValue() && this.totalTicksRunning >= this.timeoutTicks.getValue()) {
            this.totalTicksRunning = 0;
            this.disable();
            return;
        }
        if (!this.firstRun) {
            if (this.delayStep < this.tickDelay.getValue()) {
                ++this.delayStep;
                return;
            }
            this.delayStep = 0;
        }
        if (this.firstRun) {
            this.firstRun = false;
            if (this.findObiInHotbar() == -1) {
                this.missingObiDisable = true;
            }
        }
        Vec3d[] offsetPattern = new Vec3d[0];
        int maxSteps = 0;
        if (this.mode.getValue().equals(Mode.FULL)) {
            offsetPattern = Offsets.FULL;
            maxSteps = Offsets.FULL.length;
        }
        if (this.mode.getValue().equals(Mode.SURROUND)) {
            offsetPattern = Offsets.SURROUND;
            maxSteps = Offsets.SURROUND.length;
        }
        int blocksPlaced = 0;
        while (blocksPlaced < this.blocksPerTick.getValue()) {
            if (this.offsetStep >= maxSteps) {
                this.offsetStep = 0;
                break;
            }
            final BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetStep]);
            final BlockPos targetPos = new BlockPos(AutoFeetPlace.mc.player.getPositionVector()).add(offsetPos.x, offsetPos.y, offsetPos.z);
            if (this.placeBlock(targetPos)) {
                ++blocksPlaced;
            }
            ++this.offsetStep;
        }
        if (blocksPlaced > 0) {
            if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
                AutoFeetPlace.mc.player.inventory.currentItem = this.playerHotbarSlot;
                this.lastHotbarSlot = this.playerHotbarSlot;
            }
            if (this.isSneaking) {
                AutoFeetPlace.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetPlace.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                this.isSneaking = false;
            }
        }
        ++this.totalTicksRunning;
        if (this.missingObiDisable && this.disableNone.getValue()) {
            this.missingObiDisable = false;
            if (this.infoMessage.getValue()) {
                Command.sendChatMessage(this.getChatName() + " " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
            }
            this.disable();
        }
    }
    
    private boolean placeBlock(final BlockPos pos) {
        final Block block = AutoFeetPlace.mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }
        for (final Entity entity : AutoFeetPlace.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }
        final EnumFacing side = getPlaceableSide(pos);
        if (side == null) {
            return false;
        }
        final BlockPos neighbour = pos.offset(side);
        final EnumFacing opposite = side.getOpposite();
        if (!BlockInteractionHelper.canBeClicked(neighbour)) {
            return false;
        }
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        final Block neighbourBlock = AutoFeetPlace.mc.world.getBlockState(neighbour).getBlock();
        final int obiSlot = this.findObiInHotbar();
        if (obiSlot == -1) {
            this.missingObiDisable = true;
            return false;
        }
        if (this.lastHotbarSlot != obiSlot) {
            AutoFeetPlace.mc.player.inventory.currentItem = obiSlot;
            this.lastHotbarSlot = obiSlot;
        }
        if ((!this.isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock)) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            AutoFeetPlace.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AutoFeetPlace.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        AutoFeetPlace.mc.playerController.processRightClickBlock(AutoFeetPlace.mc.player, AutoFeetPlace.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        AutoFeetPlace.mc.player.swingArm(EnumHand.MAIN_HAND);
        AutoFeetPlace.mc.rightClickDelayTimer = 4;
        if (ModuleManager.getModuleByName("NoBreakAnimation").isEnabled()) {
            ((NoBreakAnimation)ModuleManager.getModuleByName("NoBreakAnimation")).resetMining();
        }
        return true;
    }
    
    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = AutoFeetPlace.mc.player.inventory.getStackInSlot(i);
            if (stack != ItemStack.EMPTY) {
                if (stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (block instanceof BlockObsidian) {
                        slot = i;
                        break;
                    }
                }
            }
        }
        return slot;
    }
    
    private enum Mode
    {
        SURROUND, 
        FULL;
    }
    
    private static class Offsets
    {
        private static final Vec3d[] SURROUND;
        private static final Vec3d[] FULL;
        
        static {
            SURROUND = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0) };
            FULL = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, 1.0), new Vec3d(-1.0, -1.0, 0.0), new Vec3d(0.0, -1.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        }
    }
}
