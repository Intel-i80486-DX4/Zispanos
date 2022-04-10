//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import java.util.concurrent.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.modules.crystals.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.block.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import java.awt.*;

@Info(name = "HoleESP", category = Category.RENDER, description = "Show safe holes")
public class HoleESP extends Module
{
    private final BlockPos[] surroundOffset;
    private Setting renderDistance;
    private Setting a0;
    private Setting r1;
    private Setting g1;
    private Setting b1;
    private Setting r2;
    private Setting g2;
    private Setting b2;
    private Setting renderModeSetting;
    private Setting renderBlocksSetting;
    private ConcurrentHashMap safeHoles;
    
    public HoleESP() {
        this.surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        this.renderDistance = this.register(Settings.d("Render Distance", 8.0));
        this.a0 = this.register((Setting<Object>)Settings.integerBuilder("Transparency").withMinimum(0).withValue(169).withMaximum(255).build());
        this.r1 = this.register((Setting<Object>)Settings.integerBuilder("Red (Obby)").withMinimum(0).withValue(104).withMaximum(255).build());
        this.g1 = this.register((Setting<Object>)Settings.integerBuilder("Green (Obby)").withMinimum(0).withValue(12).withMaximum(255).build());
        this.b1 = this.register((Setting<Object>)Settings.integerBuilder("Blue (Obby)").withMinimum(0).withValue(35).withMaximum(255).build());
        this.r2 = this.register((Setting<Object>)Settings.integerBuilder("Red (Bedrock)").withMinimum(0).withValue(81).withMaximum(255).build());
        this.g2 = this.register((Setting<Object>)Settings.integerBuilder("Green (Bedrock)").withMinimum(0).withValue(12).withMaximum(255).build());
        this.b2 = this.register((Setting<Object>)Settings.integerBuilder("Blue (Bedrock)").withMinimum(0).withValue(104).withMaximum(255).build());
        this.renderModeSetting = this.register(Settings.e("Render Mode", RenderMode.BLOCK));
        this.renderBlocksSetting = this.register(Settings.e("Render", RenderBlocks.BOTH));
    }
    
    @Override
    public void onUpdate() {
        if (this.safeHoles == null) {
            this.safeHoles = new ConcurrentHashMap();
        }
        else {
            this.safeHoles.clear();
        }
        final int range = (int)Math.ceil(this.renderDistance.getValue());
        final AutoCrystalAtom crystalAura = (AutoCrystalAtom)ModuleManager.getModuleByName("AutoCrystalAtom");
        final List blockPosList = AutoCrystalAtom3.getSphere(AutoCrystalAtom.getPlayerPos(), (float)range, range, false, true, 0);
        for (final BlockPos pos : blockPosList) {
            if (HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) && HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                boolean isSafe = true;
                boolean isBedrock = true;
                for (final BlockPos offset : this.surroundOffset) {
                    final Block block = HoleESP.mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
                    if (block != Blocks.BEDROCK) {
                        isBedrock = false;
                    }
                    if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
                        isSafe = false;
                        break;
                    }
                }
                if (!isSafe) {
                    continue;
                }
                this.safeHoles.put(pos, isBedrock);
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (HoleESP.mc.player != null && this.safeHoles != null && !this.safeHoles.isEmpty()) {
            KamiTessellator.prepare(7);
            this.safeHoles.forEach((blockPos, isBedrock) -> {
                switch (this.renderBlocksSetting.getValue()) {
                    case BOTH: {
                        if (isBedrock) {
                            this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                            break;
                        }
                        else {
                            this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                            break;
                        }
                        break;
                    }
                    case OBBY: {
                        if (!isBedrock) {
                            this.drawBox(blockPos, this.r1.getValue(), this.g1.getValue(), this.b1.getValue());
                            break;
                        }
                        else {
                            break;
                        }
                        break;
                    }
                    case BEDROCK: {
                        if (isBedrock) {
                            this.drawBox(blockPos, this.r2.getValue(), this.g2.getValue(), this.b2.getValue());
                            break;
                        }
                        else {
                            break;
                        }
                        break;
                    }
                }
                return;
            });
            KamiTessellator.release();
        }
    }
    
    private void drawBox(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.a0.getValue());
        if (this.renderModeSetting.getValue().equals(RenderMode.DOWN)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 1);
        }
        else if (this.renderModeSetting.getValue().equals(RenderMode.BLOCK)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
    }
    
    private enum RenderBlocks
    {
        OBBY, 
        BEDROCK, 
        BOTH;
    }
    
    private enum RenderMode
    {
        DOWN, 
        BLOCK;
    }
}
