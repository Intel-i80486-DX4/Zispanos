//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import java.util.function.*;
import java.util.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.modules.misc.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.block.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

@Info(name = "AutoTrapLunar", category = Category.COMBAT)
public class TrapTest extends Module
{
    private final Vec3d[] surroundList;
    private final Vec3d[] surroundListFeet;
    private final Vec3d[] surroundListFull;
    private final List obsidian;
    private long systemTime;
    private Setting toggleable;
    private Setting feet;
    private Setting jumpdisable;
    private Setting<Speed> speed;
    private Setting<Boolean> entityCheck;
    private Vec3d[] surroundTargets;
    private BlockPos basePos;
    private boolean slowModeSwitch;
    private int blocksPerTick;
    private int offsetStep;
    private int oldSlot;
    boolean BaseBuilt;
    
    public TrapTest() {
        this.surroundList = new Vec3d[] { new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(0.0, 3.0, -1.0), new Vec3d(0.0, 3.0, 1.0), new Vec3d(0.0, 3.0, 0.0) };
        this.surroundListFeet = new Vec3d[] { new Vec3d(1.0, 1.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(1.0, 1.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 2.0, 1.0), new Vec3d(1.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, -1.0), new Vec3d(1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, 1.0), new Vec3d(-1.0, 2.0, 0.0), new Vec3d(0.0, 2.0, -1.0), new Vec3d(-1.0, 3.0, 0.0), new Vec3d(0.0, 3.0, 0.0), new Vec3d(1.0, 3.0, 0.0) };
        this.surroundListFull = new Vec3d[] { new Vec3d(0.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(1.0, 1.0, 0.0), new Vec3d(1.0, 0.0, 1.0), new Vec3d(1.0, 1.0, 1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(0.0, 1.0, 1.0), new Vec3d(-1.0, 0.0, 1.0), new Vec3d(-1.0, 1.0, 1.0), new Vec3d(-1.0, 0.0, 0.0), new Vec3d(-1.0, 1.0, 0.0), new Vec3d(-1.0, 0.0, -1.0), new Vec3d(-1.0, 1.0, -1.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 1.0, -1.0), new Vec3d(1.0, 0.0, -1.0), new Vec3d(1.0, 1.0, -1.0) };
        this.systemTime = -1L;
        this.speed = this.register(Settings.e("Speed", Speed.Medium));
        this.entityCheck = this.register(Settings.b("Entity Check", true));
        this.BaseBuilt = false;
        this.obsidian = Collections.singletonList(Blocks.OBSIDIAN);
        this.toggleable = this.register(Settings.b("Toggleable", true));
        this.feet = this.register(Settings.b("Feet", false));
        this.jumpdisable = this.register(Settings.b("Jump Disable", false));
        this.slowModeSwitch = false;
        this.blocksPerTick = 6;
        this.offsetStep = 0;
        this.oldSlot = 0;
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
        if (this.jumpdisable.getValue() && !TrapTest.mc.player.onGround) {
            this.disable();
        }
        if (TrapTest.mc.world != null) {
            final EntityPlayer TrapPlayer = (EntityPlayer)Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).filter(entity -> entity != TrapTest.mc.player).filter(entity -> entity instanceof EntityPlayer).map(entity -> entity).min(Comparator.comparing(c -> TrapTest.mc.player.getDistance(c))).orElse(null);
            if (TrapPlayer == null) {
                this.end();
                return;
            }
            if (this.jumpdisable.getValue() && !TrapTest.mc.player.onGround) {
                this.end();
            }
            if (TrapTest.mc.player.isDead) {
                this.end();
            }
            if (!this.isDisabled() && TrapTest.mc.player != null && !ModuleManager.isModuleEnabled("Freecam") && TrapTest.mc.player.onGround) {
                if (this.slowModeSwitch) {
                    this.slowModeSwitch = false;
                }
                else {
                    if (this.offsetStep == 0) {
                        this.init();
                    }
                    for (int i = 0; i < this.blocksPerTick; ++i) {
                        if (this.offsetStep >= this.surroundTargets.length) {
                            if (Announcer.trap() && ModuleManager.isModuleEnabled("Announcer") && System.nanoTime() / 1000000L - this.systemTime >= Announcer.trapDelay()) {
                                Announcer.sendChatMessage("trapped " + TrapPlayer.getName());
                                this.systemTime = System.nanoTime() / 1000000L;
                            }
                            this.end();
                            return;
                        }
                        final Vec3d offset = this.surroundTargets[this.offsetStep];
                        if (this.entityCheck.getValue() && (!TrapTest.mc.world.getEntitiesWithinAABB((Class)EntityPlayer.class, new AxisAlignedBB(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)))).isEmpty() || !TrapTest.mc.world.getEntitiesWithinAABB((Class)EntityEnderCrystal.class, new AxisAlignedBB(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)))).isEmpty())) {
                            ++this.offsetStep;
                        }
                        else {
                            this.placeBlock(new BlockPos((Vec3i)this.basePos.add(offset.x, offset.y, offset.z)));
                            ++this.offsetStep;
                        }
                    }
                    this.slowModeSwitch = true;
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
                if (!this.toggleable.getValue()) {
                    Command.sendChatMessage("No Obsidian in Hotbar!");
                }
                this.end();
            }
            else {
                final int oldSlot = Wrapper.getPlayer().inventory.currentItem;
                this.BaseBuilt = true;
                Wrapper.getPlayer().inventory.currentItem = newSlot;
                if (BlockInteractionHelper.checkForNeighbours(blockPos)) {
                    if (this.feet.getValue()) {
                        this.surroundTargets = this.surroundListFeet;
                    }
                    else {
                        this.surroundTargets = this.surroundList;
                    }
                    BlockInteractionHelper.placeBlockScaffold(blockPos);
                    Wrapper.getPlayer().inventory.currentItem = oldSlot;
                }
            }
        }
    }
    
    private void init() {
        final EntityPlayer TrapPlayer = (EntityPlayer)Minecraft.getMinecraft().world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).filter(entity -> entity != TrapTest.mc.player).filter(entity -> entity instanceof EntityPlayer).map(entity -> entity).min(Comparator.comparing(c -> TrapTest.mc.player.getDistance(c))).orElse(null);
        if (TrapPlayer == null) {
            return;
        }
        if (TrapTest.mc.player.getDistance((Entity)TrapPlayer) <= 6.0) {
            this.basePos = new BlockPos(TrapPlayer.getPositionVector()).down();
            this.surroundTargets = this.surroundList;
        }
    }
    
    private void end() {
        this.offsetStep = 0;
        if (!this.toggleable.getValue()) {
            this.disable();
        }
        if (this.jumpdisable.getValue() && TrapTest.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.disable();
        }
    }
    
    @Override
    protected void onEnable() {
        if (TrapTest.mc.world != null) {
            TrapTest.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)TrapTest.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
        }
    }
    
    @Override
    protected void onDisable() {
        if (TrapTest.mc.world != null) {
            TrapTest.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)TrapTest.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
        }
    }
    
    public enum Speed
    {
        Slow, 
        Medium, 
        Fast;
    }
}
