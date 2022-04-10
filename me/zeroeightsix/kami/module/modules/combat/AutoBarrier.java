//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.block.*;

@Info(name = "SurroundLunar", category = Category.COMBAT)
public class AutoBarrier extends Module
{
    private final Vec3d[] surroundList;
    private final List obsidian;
    private Vec3d[] surroundTargets;
    private BlockPos basePos;
    private Setting jumpDisable;
    private Setting teleport;
    private Setting<Speed> speed;
    private boolean slowModeSwitch;
    private boolean centered;
    private boolean done;
    private int blocksPerTick;
    private Vec3d playerPos;
    private int offsetStep;
    private int oldSlot;
    boolean BaseBuilt;
    private static AutoBarrier INSTANCE;
    
    public AutoBarrier() {
        this.surroundList = new Vec3d[] { new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, -1.0, 0.0) };
        this.speed = this.register(Settings.e("Speed", Speed.Medium));
        this.BaseBuilt = false;
        this.jumpDisable = this.register(Settings.b("Jump Disable", true));
        this.teleport = this.register(Settings.b("Auto Center (Glitchy)", true));
        this.obsidian = Collections.singletonList(Blocks.OBSIDIAN);
        this.slowModeSwitch = false;
        this.blocksPerTick = 6;
        this.offsetStep = 0;
        this.oldSlot = 0;
        this.centered = false;
        this.done = false;
        AutoBarrier.INSTANCE = this;
    }
    
    @Override
    public void onUpdate() {
        if (this.speed.getValue() == Speed.Slow) {
            this.blocksPerTick = 1;
        }
        if (this.speed.getValue() == Speed.Medium) {
            this.blocksPerTick = 6;
        }
        if (this.speed.getValue() == Speed.Fast) {
            this.blocksPerTick = 15;
        }
        if (this.jumpDisable.getValue() && !AutoBarrier.mc.player.onGround) {
            this.disable();
        }
        if (AutoBarrier.mc.world != null && AutoBarrier.mc.player.onGround) {
            final BlockPos centerPos = AutoBarrier.mc.player.getPosition();
            this.playerPos = AutoBarrier.mc.player.getPositionVector();
            final double y = centerPos.getY();
            double x = centerPos.getX();
            double z = centerPos.getZ();
            final Vec3d plusPlus = new Vec3d(x + 0.5, y, z + 0.5);
            final Vec3d plusMinus = new Vec3d(x + 0.5, y, z - 0.5);
            final Vec3d minusMinus = new Vec3d(x - 0.5, y, z - 0.5);
            final Vec3d minusPlus = new Vec3d(x - 0.5, y, z + 0.5);
            if (this.teleport.getValue() && !this.centered) {
                if (this.getDst(plusPlus) < this.getDst(plusMinus) && this.getDst(plusPlus) < this.getDst(minusMinus) && this.getDst(plusPlus) < this.getDst(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() + 0.5;
                    AutoBarrier.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
                    AutoBarrier.mc.player.setPosition(x, y, z);
                }
                if (this.getDst(plusMinus) < this.getDst(plusPlus) && this.getDst(plusMinus) < this.getDst(minusMinus) && this.getDst(plusMinus) < this.getDst(minusPlus)) {
                    x = centerPos.getX() + 0.5;
                    z = centerPos.getZ() - 0.5;
                    AutoBarrier.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
                    AutoBarrier.mc.player.setPosition(x, y, z);
                }
                if (this.getDst(minusMinus) < this.getDst(plusPlus) && this.getDst(minusMinus) < this.getDst(plusMinus) && this.getDst(minusMinus) < this.getDst(minusPlus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() - 0.5;
                    AutoBarrier.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
                    AutoBarrier.mc.player.setPosition(x, y, z);
                }
                if (this.getDst(minusPlus) < this.getDst(plusPlus) && this.getDst(minusPlus) < this.getDst(plusMinus) && this.getDst(minusPlus) < this.getDst(minusMinus)) {
                    x = centerPos.getX() - 0.5;
                    z = centerPos.getZ() + 0.5;
                    AutoBarrier.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(x, y, z, true));
                    AutoBarrier.mc.player.setPosition(x, y, z);
                }
                this.centered = true;
            }
            if (!this.isDisabled() && AutoBarrier.mc.player != null && !ModuleManager.isModuleEnabled("Freecam")) {
                this.done = false;
                if (this.offsetStep == 0) {
                    this.init();
                }
                for (int i = 0; i < this.blocksPerTick; ++i) {
                    if (this.offsetStep >= this.surroundTargets.length) {
                        this.end();
                        return;
                    }
                    final Vec3d offset = this.surroundTargets[this.offsetStep];
                    if (!AutoBarrier.mc.world.getEntitiesWithinAABB((Class)EntityPlayer.class, new AxisAlignedBB(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)))).isEmpty() || !AutoBarrier.mc.world.getEntitiesWithinAABB((Class)EntityEnderCrystal.class, new AxisAlignedBB(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)))).isEmpty()) {
                        ++this.offsetStep;
                    }
                    else {
                        this.placeBlock(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)));
                        ++this.offsetStep;
                    }
                }
                if (this.offsetStep >= this.surroundTargets.length) {
                    this.done = true;
                }
            }
        }
    }
    
    private void placeBlock(final BlockPos blockPos) {
        if (Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable()) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                    final Block block = ((ItemBlock)stack.getItem()).getBlock();
                    if (!BlockInteractionHelper.blackList.contains(block) && !(block instanceof BlockContainer) && Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock() && (!(((ItemBlock)stack.getItem()).getBlock() instanceof BlockFalling) || !Wrapper.getWorld().getBlockState(blockPos.down()).getMaterial().isReplaceable()) && this.obsidian.contains(block)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot == -1) {
                this.end();
            }
            else {
                final int oldSlot = Wrapper.getPlayer().inventory.currentItem;
                this.BaseBuilt = false;
                Wrapper.getPlayer().inventory.currentItem = newSlot;
                if (BlockInteractionHelper.checkForNeighbours(blockPos)) {
                    this.surroundTargets = this.surroundList;
                    BlockInteractionHelper.placeBlockScaffold(blockPos);
                    Wrapper.getPlayer().inventory.currentItem = oldSlot;
                }
            }
        }
    }
    
    public static boolean done() {
        return AutoBarrier.INSTANCE.done;
    }
    
    private void init() {
        this.basePos = new BlockPos(AutoBarrier.mc.player.getPositionVector()).down();
        this.surroundTargets = this.surroundList;
    }
    
    private double getDst(final Vec3d vec) {
        return this.playerPos.distanceTo(vec);
    }
    
    private void end() {
        this.offsetStep = 0;
    }
    
    @Override
    protected void onEnable() {
        this.centered = false;
        if (AutoBarrier.mc.world != null) {
            this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
        }
    }
    
    @Override
    protected void onDisable() {
        if (AutoBarrier.mc.world != null) {
            Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
        }
    }
    
    static {
        AutoBarrier.INSTANCE = new AutoBarrier();
    }
    
    public enum Speed
    {
        Slow, 
        Medium, 
        Fast;
    }
}
