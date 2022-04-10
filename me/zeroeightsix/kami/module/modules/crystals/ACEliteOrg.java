//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;
import java.util.function.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import net.minecraft.network.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import net.minecraft.network.play.client.*;

@Info(name = "ACEliteOrg", category = Category.CRYSTALS)
public class ACEliteOrg extends Module
{
    private Setting<Double> range;
    private Setting<Integer> mindmg;
    private Setting<Integer> hitchance;
    private Setting<Boolean> feetplace;
    private Setting<Boolean> useresolver;
    private Setting<Boolean> faceplace;
    private Setting<Integer> placedelay;
    private Setting<Integer> delay;
    private Random r;
    private int random;
    private BlockPos render;
    private BlockPos oldrender;
    private Entity renderEnt;
    private long systemTime;
    private long systemTime2;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public ACEliteOrg() {
        this.range = this.register(Settings.d("range", 5.0));
        this.mindmg = this.register(Settings.integerBuilder("mindmg").withMinimum(0).withMaximum(16).withValue(0));
        this.hitchance = this.register(Settings.integerBuilder("hitchance").withMinimum(0).withMaximum(100).withValue(100));
        this.feetplace = this.register(Settings.b("FeetPlaceOnly", false));
        this.useresolver = this.register(Settings.b("UseResolver", true));
        this.faceplace = this.register(Settings.b("FacePlaceIfLethal", true));
        this.placedelay = this.register(Settings.integerBuilder("placedelay").withMinimum(0).withMaximum(1000).withValue(0));
        this.delay = this.register(Settings.integerBuilder("Hit Speed").withMinimum(0).withMaximum(20).withValue(20));
        this.r = new Random();
        this.random = 0;
        this.systemTime = -1L;
        this.systemTime2 = -1L;
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && ACEliteOrg.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)ACEliteOrg.yaw;
                ((CPacketPlayer)packet).pitch = (float)ACEliteOrg.pitch;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)ACEliteOrg.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> ACEliteOrg.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && ACEliteOrg.mc.player.getDistance((Entity)crystal) <= this.range.getValue()) {
            if (System.nanoTime() / 1000000L - this.systemTime >= 400 - this.delay.getValue() * 20) {
                this.random = this.r.nextInt(100) + 1;
                if (100 - this.random <= this.hitchance.getValue()) {
                    this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)ACEliteOrg.mc.player);
                    ACEliteOrg.mc.playerController.attackEntity((EntityPlayer)ACEliteOrg.mc.player, (Entity)crystal);
                    ACEliteOrg.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            if (this.useresolver.getValue()) {
                return;
            }
            if (System.nanoTime() / 1000000L - this.systemTime2 <= 400 - this.delay.getValue() * 30) {
                this.systemTime2 = System.nanoTime() / 1000000L;
                return;
            }
        }
        int crystalSlot = (ACEliteOrg.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? ACEliteOrg.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (ACEliteOrg.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (ACEliteOrg.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)ACEliteOrg.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        double d = 0.5;
        double self = 0.5;
        double targetHealth = 36.0;
        for (final Entity entity2 : entities) {
            if (entity2 != ACEliteOrg.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b >= 36.0) {
                        continue;
                    }
                    targetHealth = ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount();
                    d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (this.feetplace.getValue()) {
                        if (this.faceplace.getValue()) {
                            if (targetHealth >= 10.0) {
                                if (blockPos.getY() >= entity2.posY) {
                                    continue;
                                }
                                if (this.getDistanceToBlockPos(blockPos, new BlockPos(entity2.posX, entity2.posY, entity2.posZ)) > 4.0) {
                                    continue;
                                }
                            }
                        }
                        else if (!this.faceplace.getValue()) {
                            if (blockPos.getY() >= entity2.posY) {
                                continue;
                            }
                            if (this.getDistanceToBlockPos(blockPos, new BlockPos(entity2.posX, entity2.posY, entity2.posZ)) > 4.0) {
                                continue;
                            }
                        }
                    }
                    if (d <= damage) {
                        continue;
                    }
                    self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)ACEliteOrg.mc.player);
                    if (self + 5.0 > d && d >= ((EntityLivingBase)entity2).getHealth() && self - 0.5 <= ACEliteOrg.mc.player.getHealth()) {
                        continue;
                    }
                    if (d <= this.mindmg.getValue()) {
                        if (this.faceplace.getValue() && targetHealth >= 10.0) {
                            continue;
                        }
                        if (!this.faceplace.getValue()) {
                            continue;
                        }
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
            resetRotation();
            return;
        }
        this.render = q;
        if (!offhand && ACEliteOrg.mc.player.inventory.currentItem != crystalSlot) {
            ACEliteOrg.mc.player.inventory.currentItem = crystalSlot;
            resetRotation();
            this.switchCooldown = true;
            return;
        }
        this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)ACEliteOrg.mc.player);
        final RayTraceResult result = ACEliteOrg.mc.world.rayTraceBlocks(new Vec3d(ACEliteOrg.mc.player.posX, ACEliteOrg.mc.player.posY + ACEliteOrg.mc.player.getEyeHeight(), ACEliteOrg.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
        EnumFacing f;
        if (result == null || result.sideHit == null) {
            f = EnumFacing.UP;
        }
        else {
            f = result.sideHit;
        }
        if (System.nanoTime() / 1000000L - this.systemTime >= this.placedelay.getValue() / 10) {
            ACEliteOrg.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            this.systemTime2 = System.nanoTime() / 1000000L;
            this.systemTime = System.nanoTime() / 1000000L;
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, 255, 255, 255, 90, 63);
            KamiTessellator.release();
            this.oldrender = this.render;
        }
        else if (this.oldrender != null && this.getDistanceToBlockPos(getPlayerPos(), this.oldrender) <= this.range.getValue()) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.oldrender, 255, 255, 255, 90, 63);
            KamiTessellator.release();
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private double getDistanceToBlockPos(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (ACEliteOrg.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || ACEliteOrg.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && ACEliteOrg.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && ACEliteOrg.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && ACEliteOrg.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && ACEliteOrg.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(ACEliteOrg.mc.player.posX), Math.floor(ACEliteOrg.mc.player.posY), Math.floor(ACEliteOrg.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
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
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)ACEliteOrg.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = ACEliteOrg.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        ACEliteOrg.yaw = yaw1;
        ACEliteOrg.pitch = pitch1;
        ACEliteOrg.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (ACEliteOrg.isSpoofingAngles) {
            ACEliteOrg.yaw = ACEliteOrg.mc.player.rotationYaw;
            ACEliteOrg.pitch = ACEliteOrg.mc.player.rotationPitch;
            ACEliteOrg.isSpoofingAngles = false;
        }
    }
    
    public void onDisable() {
        this.render = null;
        this.oldrender = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    static {
        ACEliteOrg.togglePitch = false;
    }
}
