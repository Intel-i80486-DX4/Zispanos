//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import net.minecraftforge.fml.common.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraftforge.event.*;
import net.minecraft.block.material.*;
import net.minecraft.world.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraftforge.registries.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import java.util.*;

@Info(name = "XRay", category = Category.RENDER, description = "See through common blocks!")
@Mod.EventBusSubscriber(modid = "zispanos")
public class XRay extends Module
{
    private static final String DEFAULT_XRAY_CONFIG = "minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone";
    private Setting<String> hiddenBlockNames;
    public Setting<Boolean> invert;
    private Setting<Boolean> outlines;
    private static Set<Block> hiddenBlocks;
    private static boolean invertStatic;
    private static boolean outlinesStatic;
    private static IBlockState transparentState;
    public static Block transparentBlock;
    
    public XRay() {
        this.hiddenBlockNames = this.register(Settings.stringBuilder("HiddenBlocks").withValue("minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone").withConsumer((old, value) -> {
            this.refreshHiddenBlocksSet(value);
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        this.invert = this.register(Settings.booleanBuilder("Invert").withValue(false).withConsumer((old, value) -> {
            XRay.invertStatic = value;
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        this.outlines = this.register(Settings.booleanBuilder("Outlines").withValue(true).withConsumer((old, value) -> {
            XRay.outlinesStatic = value;
            if (this.isEnabled()) {
                XRay.mc.renderGlobal.loadRenderers();
            }
            return;
        }).build());
        XRay.invertStatic = this.invert.getValue();
        XRay.outlinesStatic = this.outlines.getValue();
        this.refreshHiddenBlocksSet(this.hiddenBlockNames.getValue());
    }
    
    public String extGet() {
        return this.extGetInternal(null);
    }
    
    public void extAdd(final String s) {
        this.hiddenBlockNames.setValue(this.extGetInternal(null) + ", " + s);
    }
    
    public void extRemove(final String s) {
        this.hiddenBlockNames.setValue(this.extGetInternal(Block.getBlockFromName(s)));
    }
    
    public void extClear() {
        this.hiddenBlockNames.setValue("");
    }
    
    public void extDefaults() {
        this.extClear();
        this.extAdd("minecraft:grass,minecraft:dirt,minecraft:netherrack,minecraft:gravel,minecraft:sand,minecraft:stone");
    }
    
    public void extSet(final String s) {
        this.extClear();
        this.extAdd(s);
    }
    
    private String extGetInternal(final Block filter) {
        final StringBuilder sb = new StringBuilder();
        boolean notFirst = false;
        for (final Block b : XRay.hiddenBlocks) {
            if (b == filter) {
                continue;
            }
            if (notFirst) {
                sb.append(", ");
            }
            notFirst = true;
            sb.append(Block.REGISTRY.getNameForObject((Object)b));
        }
        return sb.toString();
    }
    
    private void refreshHiddenBlocksSet(final String v) {
        XRay.hiddenBlocks.clear();
        for (final String s : v.split(",")) {
            final String s2 = s.trim();
            final Block block = Block.getBlockFromName(s2);
            if (block != null) {
                XRay.hiddenBlocks.add(block);
            }
        }
    }
    
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        (XRay.transparentBlock = new Block(Material.GLASS) {
            public BlockRenderLayer getRenderLayer() {
                return BlockRenderLayer.CUTOUT;
            }
            
            public boolean isOpaqueCube(final IBlockState blah) {
                return false;
            }
            
            public boolean shouldSideBeRendered(final IBlockState blah, final IBlockAccess w, final BlockPos pos, final EnumFacing side) {
                final BlockPos adj = pos.offset(side);
                final IBlockState other = w.getBlockState(adj);
                return other.getBlock() != this && !other.isOpaqueCube();
            }
        }).setRegistryName("kami_xray_transparent");
        XRay.transparentState = XRay.transparentBlock.getDefaultState();
        event.getRegistry().registerAll((IForgeRegistryEntry[])new Block[] { XRay.transparentBlock });
    }
    
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll((IForgeRegistryEntry[])new Item[] { (Item)new ItemBlock(XRay.transparentBlock).setRegistryName(XRay.transparentBlock.getRegistryName()) });
    }
    
    public static IBlockState transform(final IBlockState input) {
        final Block b = input.getBlock();
        boolean hide = XRay.hiddenBlocks.contains(b);
        if (XRay.invertStatic) {
            hide = !hide;
        }
        if (hide) {
            IBlockState target = Blocks.AIR.getDefaultState();
            if (XRay.outlinesStatic && XRay.transparentState != null) {
                target = XRay.transparentState;
            }
            return target;
        }
        return input;
    }
    
    @Override
    protected void onEnable() {
        XRay.mc.renderGlobal.loadRenderers();
    }
    
    @Override
    protected void onDisable() {
        XRay.mc.renderGlobal.loadRenderers();
    }
    
    static {
        XRay.hiddenBlocks = Collections.synchronizedSet(new HashSet<Block>());
        XRay.outlinesStatic = true;
    }
}
