//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.utils;

import me.zeroeightsix.kami.module.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.zero.alpine.listener.*;
import java.util.function.*;
import org.lwjgl.input.*;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.*;
import java.util.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.util.math.*;
import net.minecraft.block.state.*;
import net.minecraft.block.*;

@Info(name = "BlockData", category = Category.UTILS, description = "Right click blocks to display their data")
public class BlockData extends Module
{
    private int delay;
    @EventHandler
    public Listener<InputEvent.MouseInputEvent> mouseListener;
    
    public BlockData() {
        this.delay = 0;
        this.mouseListener = (Listener<InputEvent.MouseInputEvent>)new Listener(event -> {
            if (Mouse.getEventButton() == 1 && this.delay == 0 && BlockData.mc.objectMouseOver.typeOfHit.equals((Object)RayTraceResult.Type.BLOCK)) {
                final BlockPos blockpos = BlockData.mc.objectMouseOver.getBlockPos();
                final IBlockState iblockstate = BlockData.mc.world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (block.hasTileEntity()) {
                    final TileEntity t = BlockData.mc.world.getTileEntity(blockpos);
                    final NBTTagCompound tag = new NBTTagCompound();
                    Objects.requireNonNull(t).writeToNBT(tag);
                    Command.sendChatMessage(this.getChatName() + "&6Block Tags:\n" + tag + "");
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.delay > 0) {
            --this.delay;
        }
    }
}
