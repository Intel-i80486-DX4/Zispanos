//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.tileentity.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;

@Info(name = "StorageESP", description = "Draws nice little lines around storage items", category = Category.RENDER)
public class StorageESP extends Module
{
    private Setting<Boolean> chest;
    private Setting<Boolean> dispenser;
    private Setting<Boolean> shulker;
    private Setting<Boolean> echest;
    private Setting<Boolean> furnace;
    private Setting<Boolean> hopper;
    private Setting<Boolean> cart;
    private Setting<Boolean> frame;
    
    public StorageESP() {
        this.chest = this.register(Settings.b("Chest", true));
        this.dispenser = this.register(Settings.b("Dispenser", true));
        this.shulker = this.register(Settings.b("Shulker", true));
        this.echest = this.register(Settings.b("Ender Chest", true));
        this.furnace = this.register(Settings.b("Furnace", true));
        this.hopper = this.register(Settings.b("Hopper", true));
        this.cart = this.register(Settings.b("Minecart", true));
        this.frame = this.register(Settings.b("Item Frame", true));
    }
    
    private int getTileEntityColor(final TileEntity tileEntity) {
        if (tileEntity instanceof TileEntityChest || tileEntity instanceof TileEntityDispenser) {
            return ColourUtils.Colors.ORANGE;
        }
        if (tileEntity instanceof TileEntityShulkerBox) {
            return ColourUtils.Colors.RED;
        }
        if (tileEntity instanceof TileEntityEnderChest) {
            return ColourUtils.Colors.PURPLE;
        }
        if (tileEntity instanceof TileEntityFurnace) {
            return ColourUtils.Colors.GRAY;
        }
        if (tileEntity instanceof TileEntityHopper) {
            return ColourUtils.Colors.DARK_RED;
        }
        return -1;
    }
    
    private int getEntityColor(final Entity entity) {
        if (entity instanceof EntityMinecartChest) {
            return ColourUtils.Colors.ORANGE;
        }
        if (entity instanceof EntityItemFrame && ((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox) {
            return ColourUtils.Colors.YELLOW;
        }
        if (entity instanceof EntityItemFrame && !(((EntityItemFrame)entity).getDisplayedItem().getItem() instanceof ItemShulkerBox)) {
            return ColourUtils.Colors.ORANGE;
        }
        return -1;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final ArrayList<Triplet<BlockPos, Integer, Integer>> a = new ArrayList<Triplet<BlockPos, Integer, Integer>>();
        GlStateManager.pushMatrix();
        for (final TileEntity tileEntity : Wrapper.getWorld().loadedTileEntityList) {
            final BlockPos pos = tileEntity.getPos();
            final int color = this.getTileEntityColor(tileEntity);
            int side = 63;
            if (tileEntity instanceof TileEntityChest) {
                final TileEntityChest chest = (TileEntityChest)tileEntity;
                if (chest.adjacentChestZNeg != null) {
                    side = ~(side & 0x4);
                }
                if (chest.adjacentChestXPos != null) {
                    side = ~(side & 0x20);
                }
                if (chest.adjacentChestZPos != null) {
                    side = ~(side & 0x8);
                }
                if (chest.adjacentChestXNeg != null) {
                    side = ~(side & 0x10);
                }
            }
            if (((tileEntity instanceof TileEntityChest && this.chest.getValue()) || (tileEntity instanceof TileEntityDispenser && this.dispenser.getValue()) || (tileEntity instanceof TileEntityShulkerBox && this.shulker.getValue()) || (tileEntity instanceof TileEntityEnderChest && this.echest.getValue()) || (tileEntity instanceof TileEntityFurnace && this.furnace.getValue()) || (tileEntity instanceof TileEntityHopper && this.hopper.getValue())) && color != -1) {
                a.add(new Triplet<BlockPos, Integer, Integer>(pos, color, side));
            }
        }
        for (final Entity entity : Wrapper.getWorld().loadedEntityList) {
            final BlockPos pos = entity.getPosition();
            final int color = this.getEntityColor(entity);
            if (((entity instanceof EntityItemFrame && this.frame.getValue()) || (entity instanceof EntityMinecartChest && this.cart.getValue())) && color != -1) {
                a.add(new Triplet<BlockPos, Integer, Integer>((entity instanceof EntityItemFrame) ? pos.add(0, -1, 0) : pos, color, 63));
            }
        }
        KamiTessellator.prepare(7);
        for (final Triplet<BlockPos, Integer, Integer> pair : a) {
            KamiTessellator.drawBox((BlockPos)pair.getFirst(), this.changeAlpha(pair.getSecond(), 100), (int)pair.getThird());
        }
        KamiTessellator.release();
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
    }
    
    int changeAlpha(int origColor, final int userInputedAlpha) {
        origColor &= 0xFFFFFF;
        return userInputedAlpha << 24 | origColor;
    }
    
    public class Triplet<T, U, V>
    {
        private final T first;
        private final U second;
        private final V third;
        
        public Triplet(final T first, final U second, final V third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }
        
        public T getFirst() {
            return this.first;
        }
        
        public U getSecond() {
            return this.second;
        }
        
        public V getThird() {
            return this.third;
        }
    }
}
