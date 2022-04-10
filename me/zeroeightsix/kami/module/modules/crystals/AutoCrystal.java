//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import java.util.function.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;

@Info(name = "AutoCrystalSalHack", category = Category.CRYSTALS)
public class AutoCrystal extends Module
{
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    private Setting<Boolean> place;
    private Setting<Boolean> slow;
    private Setting<Double> BreakRange;
    private Setting<Double> PlaceRange;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> Walls;
    private Setting<Double> WallsRange;
    private Setting<Boolean> rotate;
    private Setting<Double> placereset;
    private Setting<Double> breakreset;
    private Setting<Boolean> delay;
    private Setting<Boolean> suicide;
    private Setting<Boolean> RayTrace;
    private Setting<Boolean> Return;
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    double playerDistance;
    private int breaks;
    private int placements;
    private boolean uniDamage;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    
    public AutoCrystal() {
        this.autoSwitch = this.register(Settings.b("Auto Switch"));
        this.players = this.register(Settings.b("Players"));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.animals = this.register(Settings.b("Animals", false));
        this.place = this.register(Settings.b("Place", false));
        this.slow = this.register(Settings.b("Slow", false));
        this.BreakRange = this.register(Settings.d("Break Range", 6.0));
        this.PlaceRange = this.register(Settings.d("Place Range", 6.0));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", false));
        this.Walls = this.register(Settings.b("Walls", false));
        this.WallsRange = this.register(Settings.d("Walls Range", 4.5));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.placereset = this.register(Settings.d("Placements before rotate", 1.0));
        this.breakreset = this.register(Settings.d("Breaks before reset", 1.0));
        this.delay = this.register(Settings.b("Delay", true));
        this.suicide = this.register(Settings.b("Suicide Protect", true));
        this.RayTrace = this.register(Settings.b("RayTrace", true));
        this.Return = this.register(Settings.b("Return", true));
        this.systemTime = -1L;
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
    }
    
    @Override
    public void onUpdate() {
        final double Placements = 0.0;
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystal.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && AutoCrystal.mc.player.getDistance((Entity)crystal) <= this.BreakRange.getValue()) {
            if (this.Walls.getValue() && !AutoCrystal.mc.player.canEntityBeSeen((Entity)crystal) && AutoCrystal.mc.player.getDistance((Entity)crystal) <= this.WallsRange.getValue()) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
                AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (this.delay.getValue()) {
                    this.systemTime = System.nanoTime() / 9000000L;
                }
                ++this.breaks;
            }
            else if (this.Walls.getValue() && AutoCrystal.mc.player.canEntityBeSeen((Entity)crystal)) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
                AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (this.delay.getValue()) {
                    this.systemTime = System.nanoTime() / 90000000L;
                }
                ++this.breaks;
            }
            else if (!this.Walls.getValue()) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystal.mc.player);
                AutoCrystal.mc.playerController.attackEntity((EntityPlayer)AutoCrystal.mc.player, (Entity)crystal);
                AutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                if (this.delay.getValue()) {
                    this.systemTime = System.nanoTime() / 90000000L;
                }
                ++this.breaks;
            }
            if (this.breaks == this.breakreset.getValue()) {
                if (this.rotate.getValue()) {
                    resetRotation();
                }
                this.breaks = 0;
                if (this.Return.getValue()) {
                    return;
                }
            }
        }
        else {
            if (this.rotate.getValue()) {
                resetRotation();
            }
            if (this.oldSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
        int crystalSlot = (AutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystal.mc.player.inventory.currentItem : -1;
        final int n;
        if ((n = crystalSlot) == -1) {
            int l = 0;
            while (l < 9) {
                if (AutoCrystal.mc.player.inventory.getStackInSlot(l).getItem() != Items.END_CRYSTAL) {
                    ++l;
                }
                else {
                    crystalSlot = l;
                }
            }
        }
        boolean offhand = false;
        if (AutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        if (this.players.getValue()) {
            entities.addAll((Collection<? extends Entity>)AutoCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        final boolean b2;
        entities.addAll((Collection<? extends Entity>)AutoCrystal.mc.world.loadedEntityList.stream().filter(entity -> {
            if (EntityUtil.isLiving(entity)) {
                if (EntityUtil.isPassive(entity) ? this.animals.getValue() : this.mobs.getValue()) {
                    return b2;
                }
            }
            return b2;
        }).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != AutoCrystal.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b >= 169.0) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    this.uniDamage = (d >= 12.0);
                    final double self;
                    if (d <= damage || ((self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AutoCrystal.mc.player)) > d && d >= ((EntityLivingBase)entity2).getHealth())) {
                        continue;
                    }
                    if (self - 0.5 > AutoCrystal.mc.player.getHealth() && this.suicide.getValue()) {
                        continue;
                    }
                    damage = d;
                    q = blockPos;
                    this.renderEnt = entity2;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            if (this.rotate.getValue()) {
                resetRotation();
            }
            return;
        }
        this.render = q;
        if (this.place.getValue()) {
            if (!offhand && AutoCrystal.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    AutoCrystal.mc.player.inventory.currentItem = crystalSlot;
                    if (this.rotate.getValue()) {
                        resetRotation();
                    }
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)AutoCrystal.mc.player);
            EnumFacing f;
            if (this.RayTrace.getValue()) {
                final RayTraceResult result = AutoCrystal.mc.world.rayTraceBlocks(new Vec3d(AutoCrystal.mc.player.posX, AutoCrystal.mc.player.posY + AutoCrystal.mc.player.getEyeHeight(), AutoCrystal.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
                f = ((result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit);
                if (this.switchCooldown) {
                    this.switchCooldown = false;
                    return;
                }
            }
            else {
                f = EnumFacing.UP;
            }
            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            ++this.placements;
            if (this.placements == this.placereset.getValue()) {
                resetRotation();
            }
            if (AutoCrystal.isSpoofingAngles) {
                if (AutoCrystal.togglePitch) {
                    AutoCrystal.mc.player.rotationPitch += (float)4.0E-4;
                    AutoCrystal.togglePitch = false;
                }
                else {
                    AutoCrystal.mc.player.rotationPitch -= (float)4.0E-4;
                    AutoCrystal.togglePitch = true;
                }
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            if (this.uniDamage) {
                KamiTessellator.prepare(7);
                KamiTessellator.drawBox(this.render, -1728013927, 63);
                KamiTessellator.release();
                if (this.renderEnt != null) {
                    EntityUtil.getInterpolatedRenderPos(this.renderEnt, AutoCrystal.mc.getRenderPartialTicks());
                }
            }
            else {
                KamiTessellator.prepare(7);
                KamiTessellator.drawBox(this.render, 1342216601, 63);
                KamiTessellator.release();
                if (this.renderEnt != null) {
                    EntityUtil.getInterpolatedRenderPos(this.renderEnt, AutoCrystal.mc.getRenderPartialTicks());
                }
            }
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystal.mc.player.posX), Math.floor(AutoCrystal.mc.player.posY), Math.floor(AutoCrystal.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.PlaceRange.getValue().floatValue(), this.PlaceRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = sphere ? (cy + r) : ((float)(cy + h));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 12.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystal.yaw = yaw1;
        AutoCrystal.pitch = pitch1;
        AutoCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystal.isSpoofingAngles) {
            AutoCrystal.yaw = AutoCrystal.mc.player.rotationYaw;
            AutoCrystal.pitch = AutoCrystal.mc.player.rotationPitch;
            AutoCrystal.isSpoofingAngles = false;
        }
    }
    
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
        if (this.rotate.getValue()) {
            resetRotation();
        }
    }
    
    static {
        AutoCrystal.togglePitch = false;
    }
}
