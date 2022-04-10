//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.block.material.*;
import net.minecraft.item.*;
import java.util.function.*;
import java.util.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;

@Info(name = "HoleFill", category = Category.COMBAT)
public class HoleFill extends Module
{
    private Setting range;
    private Setting yRange;
    private Setting rotate;
    private Setting waitTick;
    private Setting useEC;
    private Setting chat;
    private ArrayList<BlockPos> holes;
    private List whiteList;
    BlockPos pos;
    private int waitCounter;
    
    public HoleFill() {
        this.range = this.register(Settings.d("Range", 5.0));
        this.yRange = this.register(Settings.d("YRange", 2.0));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.waitTick = this.register(Settings.d("TickDelay", 1.0));
        this.useEC = this.register(Settings.b("UseEnderchests", false));
        this.chat = this.register(Settings.b("Chat", false));
        this.holes = new ArrayList<BlockPos>();
        this.whiteList = Arrays.asList(Blocks.OBSIDIAN);
    }
    
    @Override
    public void onUpdate() {
        this.holes = new ArrayList<BlockPos>();
        if (this.useEC.getValue()) {
            if (!this.whiteList.contains(Blocks.ENDER_CHEST)) {
                this.whiteList.add(Blocks.ENDER_CHEST);
            }
        }
        else if (this.whiteList.contains(Blocks.ENDER_CHEST)) {
            this.whiteList.remove(Blocks.ENDER_CHEST);
        }
        final Iterable blocks = BlockPos.getAllInBox(HoleFill.mc.player.getPosition().add(-this.range.getValue(), -this.yRange.getValue(), -this.range.getValue()), HoleFill.mc.player.getPosition().add((double)this.range.getValue(), (double)this.yRange.getValue(), (double)this.range.getValue()));
        for (final BlockPos pos : blocks) {
            if (!HoleFill.mc.world.getBlockState(pos).getMaterial().blocksMovement() && !HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial().blocksMovement()) {
                final boolean solidNeighbours = (HoleFill.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() == Blocks.OBSIDIAN) && (HoleFill.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.BEDROCK | HoleFill.mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() == Blocks.OBSIDIAN) && HoleFill.mc.world.getBlockState(pos.add(0, 0, 0)).getMaterial() == Material.AIR && HoleFill.mc.world.getBlockState(pos.add(0, 1, 0)).getMaterial() == Material.AIR && HoleFill.mc.world.getBlockState(pos.add(0, 2, 0)).getMaterial() == Material.AIR;
                if (!solidNeighbours) {
                    continue;
                }
                this.holes.add(pos);
            }
        }
        int newSlot = -1;
        for (int oldSlot = 0; oldSlot < 9; ++oldSlot) {
            final ItemStack stack = HoleFill.mc.player.inventory.getStackInSlot(oldSlot);
            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
                final Block block = ((ItemBlock)stack.getItem()).getBlock();
                if (this.whiteList.contains(block)) {
                    newSlot = oldSlot;
                    break;
                }
            }
        }
        if (newSlot == -1) {
            return;
        }
        int oldSlot = HoleFill.mc.player.inventory.currentItem;
        if (this.waitTick.getValue() > 0.0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                HoleFill.mc.player.inventory.currentItem = newSlot;
                this.holes.forEach(this::place);
                HoleFill.mc.player.inventory.currentItem = oldSlot;
                return;
            }
            this.waitCounter = 0;
        }
    }
    
    public void onEnable() {
        if (HoleFill.mc.player != null && this.chat.getValue()) {
            Command.sendChatMessage("HoleFill " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        if (HoleFill.mc.player != null && this.chat.getValue()) {
            Command.sendChatMessage("HoleFill " + ChatFormatting.RED + "Disabled!");
        }
    }
    
    private void place(final BlockPos blockPos) {
        for (final Entity entity : HoleFill.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(blockPos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        Surround.placeBlockScaffold(blockPos, this.rotate.getValue());
        ++this.waitCounter;
    }
}
