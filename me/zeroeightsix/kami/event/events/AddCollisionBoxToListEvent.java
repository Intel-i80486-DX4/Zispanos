//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.event.events;

import me.zeroeightsix.kami.event.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.entity.*;

public class AddCollisionBoxToListEvent extends KamiEvent
{
    private final Block block;
    private final IBlockState state;
    private final World world;
    private final BlockPos pos;
    private final AxisAlignedBB entityBox;
    private final List<AxisAlignedBB> collidingBoxes;
    private final Entity entity;
    private final boolean bool;
    
    public AddCollisionBoxToListEvent(final Block block, final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, final Entity entityIn, final boolean bool) {
        this.block = block;
        this.state = state;
        this.world = worldIn;
        this.pos = pos;
        this.entityBox = entityBox;
        this.collidingBoxes = collidingBoxes;
        this.entity = entityIn;
        this.bool = bool;
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public IBlockState getState() {
        return this.state;
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public AxisAlignedBB getEntityBox() {
        return this.entityBox;
    }
    
    public List<AxisAlignedBB> getCollidingBoxes() {
        return this.collidingBoxes;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public boolean isBool() {
        return this.bool;
    }
}
