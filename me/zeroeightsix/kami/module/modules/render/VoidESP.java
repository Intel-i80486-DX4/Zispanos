//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import io.netty.util.internal.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.init.*;
import java.util.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import java.awt.*;

@Info(name = "VoidESP", category = Category.RENDER, description = "Show void holes")
public class VoidESP extends Module
{
    private Setting<Integer> range;
    private Setting<Integer> activateAtY;
    private Setting<HoleMode> holeMode;
    private Setting<RenderMode> renderMode;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private ConcurrentSet<BlockPos> voidHoles;
    
    public VoidESP() {
        this.range = this.register((Setting<Integer>)Settings.integerBuilder("Range").withMinimum(1).withValue(8).withMaximum(32).build());
        this.activateAtY = this.register((Setting<Integer>)Settings.integerBuilder("ActivateAtY").withMinimum(1).withValue(32).withMaximum(512).build());
        this.holeMode = this.register(Settings.e("HoleMode", HoleMode.SIDES));
        this.renderMode = this.register(Settings.e("RenderMode", RenderMode.DOWN));
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withValue(255).withMaximum(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withValue(0).withMaximum(255).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withValue(0).withMaximum(255).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withValue(128).withMaximum(255).build());
    }
    
    @Override
    public void onUpdate() {
        if (VoidESP.mc.player == null) {
            return;
        }
        if (VoidESP.mc.player.dimension == 1) {
            return;
        }
        if (VoidESP.mc.player.getPosition().y > this.activateAtY.getValue()) {
            return;
        }
        if (this.voidHoles == null) {
            this.voidHoles = (ConcurrentSet<BlockPos>)new ConcurrentSet();
        }
        else {
            this.voidHoles.clear();
        }
        final List<BlockPos> blockPosList = (List<BlockPos>)BlockInteractionHelper.getCircle(BlockInteractionHelper.getPlayerPos(), 0, (float)this.range.getValue(), false);
        for (final BlockPos pos : blockPosList) {
            if (VoidESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.BEDROCK)) {
                continue;
            }
            if (this.isAnyBedrock(pos, Offsets.center)) {
                continue;
            }
            boolean aboveFree = false;
            if (!this.isAnyBedrock(pos, Offsets.above)) {
                aboveFree = true;
            }
            if (this.holeMode.getValue().equals(HoleMode.ABOVE)) {
                if (!aboveFree) {
                    continue;
                }
                this.voidHoles.add((Object)pos);
            }
            else {
                boolean sidesFree = false;
                if (!this.isAnyBedrock(pos, Offsets.north)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.east)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.south)) {
                    sidesFree = true;
                }
                if (!this.isAnyBedrock(pos, Offsets.west)) {
                    sidesFree = true;
                }
                if (!this.holeMode.getValue().equals(HoleMode.SIDES)) {
                    continue;
                }
                if (!aboveFree && !sidesFree) {
                    continue;
                }
                this.voidHoles.add((Object)pos);
            }
        }
    }
    
    private boolean isAnyBedrock(final BlockPos origin, final BlockPos[] offset) {
        for (final BlockPos pos : offset) {
            if (VoidESP.mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals(Blocks.BEDROCK)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (VoidESP.mc.player == null || this.voidHoles == null || this.voidHoles.isEmpty()) {
            return;
        }
        KamiTessellator.prepare(7);
        this.voidHoles.forEach(blockPos -> this.drawBlock(blockPos, this.red.getValue(), this.green.getValue(), this.blue.getValue()));
        KamiTessellator.release();
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        int mask = 0;
        if (this.renderMode.getValue().equals(RenderMode.BLOCK)) {
            mask = 63;
        }
        if (this.renderMode.getValue().equals(RenderMode.DOWN)) {
            mask = 1;
        }
        KamiTessellator.drawBox(blockPos, color.getRGB(), mask);
    }
    
    @Override
    public String getHudInfo() {
        return this.holeMode.getValue().toString();
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK;
    }
    
    private enum HoleMode
    {
        SIDES, 
        ABOVE;
    }
    
    private static class Offsets
    {
        static final BlockPos[] center;
        static final BlockPos[] above;
        static final BlockPos[] aboveStep1;
        static final BlockPos[] aboveStep2;
        static final BlockPos[] north;
        static final BlockPos[] east;
        static final BlockPos[] south;
        static final BlockPos[] west;
        
        static {
            center = new BlockPos[] { new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
            above = new BlockPos[] { new BlockPos(0, 3, 0), new BlockPos(0, 4, 0) };
            aboveStep1 = new BlockPos[] { new BlockPos(0, 3, 0) };
            aboveStep2 = new BlockPos[] { new BlockPos(0, 4, 0) };
            north = new BlockPos[] { new BlockPos(0, 1, -1), new BlockPos(0, 2, -1) };
            east = new BlockPos[] { new BlockPos(1, 1, 0), new BlockPos(1, 2, 0) };
            south = new BlockPos[] { new BlockPos(0, 1, 1), new BlockPos(0, 2, 1) };
            west = new BlockPos[] { new BlockPos(-1, 1, 0), new BlockPos(-1, 2, 0) };
        }
    }
}
