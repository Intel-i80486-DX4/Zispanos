//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import net.minecraft.block.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.entity.*;
import javax.annotation.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BlockStateContainer.StateImplementation.class })
public class MixinStateImplementation
{
    @Shadow
    @Final
    private Block block;
    
    @Redirect(method = { "addCollisionBoxToList" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;addCollisionBoxToList(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lnet/minecraft/entity/Entity;Z)V"))
    public void addCollisionBoxToList(final Block b, final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean isActualState) {
        final AddCollisionBoxToListEvent event = new AddCollisionBoxToListEvent(b, state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
        KamiMod.EVENT_BUS.post((Object)event);
        if (!event.isCancelled()) {
            this.block.addCollisionBoxToList(state, worldIn, pos, entityBox, (List)collidingBoxes, entityIn, isActualState);
        }
    }
}
