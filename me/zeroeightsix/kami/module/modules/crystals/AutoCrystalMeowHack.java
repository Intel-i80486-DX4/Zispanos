//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;

@Info(name = "AutoCrystalMeowHack", description = "Auto Place Crystals, Modified Kami paste", category = Category.CRYSTALS)
public class AutoCrystalMeowHack extends Module
{
    static final Minecraft mc;
    private Setting<Integer> placePerTick;
    private Setting<Boolean> shouldPlace;
    private Setting<Boolean> shouldBreak;
    private Setting<Boolean> shouldSwitch;
    private Setting<Boolean> noGappleSwitch;
    private Setting<Boolean> dontCancelMining;
    private Setting<Double> placeRange;
    private Setting<Double> breakRange;
    private Setting<Double> throughWallsRange;
    private Setting<Double> minDamage;
    private BlockPos currentTarget;
    private long systemTime;
    
    public AutoCrystalMeowHack() {
        this.placePerTick = this.register((Setting<Integer>)Settings.integerBuilder("Place Per Tick").withRange(0, 6).withValue(1).build());
        this.shouldPlace = this.register(Settings.b("Place", true));
        this.shouldBreak = this.register(Settings.b("Break", true));
        this.shouldSwitch = this.register(Settings.b("Switch", true));
        this.noGappleSwitch = this.register(Settings.b("No Gapple Switch", true));
        this.dontCancelMining = this.register(Settings.b("Dont Cancel Mining", true));
        this.placeRange = this.register(Settings.d("Place Range", 4.0));
        this.breakRange = this.register(Settings.d("Break Range", 4.0));
        this.throughWallsRange = this.register(Settings.d("Raytrace Place Range", 3.0));
        this.minDamage = this.register(Settings.d("Min Damage", 4.0));
        this.systemTime = -1L;
    }
    
    @Override
    public void onUpdate() {
        if (!this.isEnabled()) {
            return;
        }
        if (this.shouldBreak.getValue()) {
            this.breakCrystals();
        }
        if (this.shouldPlace.getValue()) {
            for (int i = 0; i < this.placePerTick.getValue(); ++i) {
                this.placeCrystals();
            }
        }
    }
    
    private void placeCrystals() {
        this.currentTarget = null;
        final boolean gapplingAllow = !this.noGappleSwitch.getValue() || AutoCrystalMeowHack.mc.player.getActiveItemStack().getItem() != Items.GOLDEN_APPLE;
        final boolean miningAllow = !this.dontCancelMining.getValue() || AutoCrystalMeowHack.mc.player.getActiveItemStack().getItem() != Items.DIAMOND_PICKAXE;
        if (!gapplingAllow || !miningAllow) {
            return;
        }
        final List<BlockPos> validBlocks = this.findAvailableCrystalBlocks();
        final List<EntityPlayer> targets = new ArrayList<EntityPlayer>();
        for (final EntityPlayer player : AutoCrystalMeowHack.mc.world.playerEntities) {
            if (!Friends.isFriend(player.getName())) {
                targets.add(player);
            }
        }
        double bestDamage = 0.1;
        double bestSelfDamage = 1000.0;
        BlockPos bestSpot = null;
        for (final EntityPlayer player2 : targets) {
            if (!player2.getUniqueID().equals(AutoCrystalMeowHack.mc.player.getUniqueID())) {
                if (player2.isDead) {
                    continue;
                }
                for (final BlockPos blockPos : validBlocks) {
                    if (player2.getDistanceSq(blockPos) >= 169.0) {
                        continue;
                    }
                    final double enemyDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)player2) / 10.0f;
                    final double selfDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)AutoCrystalMeowHack.mc.player) / 10.0f;
                    if (enemyDamage < this.minDamage.getValue()) {
                        continue;
                    }
                    boolean matchesThroughWallsRange = true;
                    final RayTraceResult result = AutoCrystalMeowHack.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalMeowHack.mc.player.posX, AutoCrystalMeowHack.mc.player.posY + AutoCrystalMeowHack.mc.player.getEyeHeight(), AutoCrystalMeowHack.mc.player.posZ), new Vec3d(blockPos.getX() + 0.5, blockPos.getY() - 0.5, blockPos.getZ() + 0.5));
                    matchesThroughWallsRange = ((result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) || AutoCrystalMeowHack.mc.player.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()) <= this.throughWallsRange.getValue());
                    final boolean matchesRange = AutoCrystalMeowHack.mc.player.getDistance((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()) <= this.placeRange.getValue();
                    if (!matchesRange) {
                        continue;
                    }
                    if (!matchesThroughWallsRange) {
                        continue;
                    }
                    if (enemyDamage > bestDamage) {
                        bestDamage = enemyDamage;
                        bestSelfDamage = selfDamage;
                        bestSpot = blockPos;
                        this.currentTarget = blockPos;
                    }
                    else {
                        if (enemyDamage != bestDamage) {
                            continue;
                        }
                        if (selfDamage >= bestSelfDamage) {
                            continue;
                        }
                        bestDamage = enemyDamage;
                        bestSelfDamage = selfDamage;
                        bestSpot = blockPos;
                        this.currentTarget = blockPos;
                    }
                }
            }
        }
        if (bestDamage < this.minDamage.getValue() || bestSpot == null) {
            return;
        }
        final boolean isHoldingCrystal = AutoCrystalMeowHack.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || AutoCrystalMeowHack.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        if (!isHoldingCrystal && !this.shouldSwitch.getValue()) {
            return;
        }
        if (isHoldingCrystal) {
            final RayTraceResult result2 = AutoCrystalMeowHack.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalMeowHack.mc.player.posX, AutoCrystalMeowHack.mc.player.posY + AutoCrystalMeowHack.mc.player.getEyeHeight(), AutoCrystalMeowHack.mc.player.posZ), new Vec3d(bestSpot.getX() + 0.5, bestSpot.getY() - 0.5, bestSpot.getZ() + 0.5));
            EnumFacing face;
            if (result2 == null || result2.sideHit == null) {
                face = EnumFacing.UP;
            }
            else {
                face = result2.sideHit;
            }
            EnumHand hand = EnumHand.MAIN_HAND;
            if (AutoCrystalMeowHack.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                hand = EnumHand.OFF_HAND;
            }
            final Vec3d hitVec = new Vec3d((Vec3i)bestSpot).add(0.5, 0.5, 0.5).add(new Vec3d(face.getDirectionVec()).scale(0.5));
            AutoCrystalMeowHack.mc.playerController.processRightClickBlock(AutoCrystalMeowHack.mc.player, AutoCrystalMeowHack.mc.world, bestSpot, face, hitVec, hand);
            AutoCrystalMeowHack.mc.player.swingArm(hand);
            return;
        }
        int crystalSlot = (AutoCrystalMeowHack.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalMeowHack.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AutoCrystalMeowHack.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        if (crystalSlot >= 0) {
            AutoCrystalMeowHack.mc.player.inventory.currentItem = crystalSlot;
        }
    }
    
    private void breakCrystals() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystalMeowHack.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystalMeowHack.mc.player.getDistance(c))).orElse(null);
        if (crystal != null) {
            final double distance = AutoCrystalMeowHack.mc.player.getDistance((Entity)crystal);
            boolean matchesThroughWallsRange = true;
            final RayTraceResult result = AutoCrystalMeowHack.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalMeowHack.mc.player.posX, AutoCrystalMeowHack.mc.player.posY + AutoCrystalMeowHack.mc.player.getEyeHeight(), AutoCrystalMeowHack.mc.player.posZ), new Vec3d(crystal.posX + 0.5, crystal.posY - 0.5, crystal.posZ + 0.5));
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                matchesThroughWallsRange = (distance <= this.throughWallsRange.getValue());
            }
            final boolean matchesRange = distance <= this.placeRange.getValue();
            if (matchesRange && matchesThroughWallsRange && System.nanoTime() / 1000000L - this.systemTime >= 250L) {
                AutoCrystalMeowHack.mc.playerController.attackEntity((EntityPlayer)AutoCrystalMeowHack.mc.player, (Entity)crystal);
                AutoCrystalMeowHack.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.systemTime = System.nanoTime() / 1000000L;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.currentTarget != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.currentTarget, 255, 255, 255, 255, 63);
            KamiTessellator.release();
        }
    }
    
    private List<BlockPos> findAvailableCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(new BlockPos(Math.floor(AutoCrystalMeowHack.mc.player.posX), Math.floor(AutoCrystalMeowHack.mc.player.posY), Math.floor(AutoCrystalMeowHack.mc.player.posZ)), this.breakRange.getValue().floatValue(), this.breakRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List circleblocks = new ArrayList();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return (List<BlockPos>)circleblocks;
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystalMeowHack.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystalMeowHack.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystalMeowHack.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystalMeowHack.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystalMeowHack.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
    }
    
    private static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystalMeowHack.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystalMeowHack.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
