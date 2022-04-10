//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraft.network.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import java.awt.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.item.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import me.zeroeightsix.kami.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;

@Info(name = "HunbaoAuraSalHack", category = Category.CRYSTALS)
public class HunbaoAura extends Module
{
    private Setting<Boolean> explode;
    private Setting<Boolean> autoTickDelay;
    private Setting<Double> waitTick;
    private Setting<Double> range;
    private Setting<Double> walls;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> nodesync;
    private Setting<Boolean> place;
    private Setting<Boolean> explodeOwnOnly;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> noGappleSwitch;
    private Setting<Double> placeRange;
    private Setting<Double> minDmg;
    private Setting<Double> facePlace;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> rotate;
    private Setting<Boolean> spoofRotations;
    private Setting<Double> maxSelfDmg;
    private Setting<Boolean> targetPlayers;
    private Setting<Boolean> targetAnimals;
    private Setting<Boolean> targetMobs;
    private BlockPos render;
    private Entity renderEnt;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int waitCounter;
    EnumFacing f;
    private List<BlockPos> ownCrystalPositions;
    private boolean isActive;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetSendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> packetReceiveListener;
    
    public HunbaoAura() {
        this.explode = this.register(Settings.b("Explode"));
        this.autoTickDelay = this.register(Settings.b("Auto Tick Delay", false));
        this.waitTick = this.register(Settings.d("Tick Delay", 1.0));
        this.range = this.register(Settings.d("Hit Range", 5.0));
        this.walls = this.register(Settings.d("Walls Range", 3.5));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.nodesync = this.register(Settings.b("No Desync", true));
        this.place = this.register(Settings.b("Place", true));
        this.explodeOwnOnly = this.register(Settings.b("ExplodeOwnOnly", false));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.noGappleSwitch = this.register(Settings.b("No Gap Switch", false));
        this.placeRange = this.register(Settings.d("Place Range", 5.0));
        this.minDmg = this.register(Settings.d("Min Damage", 5.0));
        this.facePlace = this.register(Settings.d("Faceplace HP", 6.0));
        this.raytrace = this.register(Settings.b("Ray Trace", false));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.spoofRotations = this.register(Settings.b("Spoof Angles", true));
        this.maxSelfDmg = this.register(Settings.d("Max Self Dmg", 10.0));
        this.targetPlayers = this.register(Settings.b("Players", true));
        this.targetAnimals = this.register(Settings.b("Animals", false));
        this.targetMobs = this.register(Settings.b("Mobs", false));
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.ownCrystalPositions = new ArrayList<BlockPos>();
        this.packetSendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && HunbaoAura.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)HunbaoAura.yaw;
                ((CPacketPlayer)packet).pitch = (float)HunbaoAura.pitch;
            }
        }, new Predicate[0]);
        this.packetReceiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            final SPacketSoundEffect packet;
            if (event.getPacket() instanceof SPacketSoundEffect && this.nodesync.getValue() && (packet = (SPacketSoundEffect)event.getPacket()).getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal) {
                        if (e.getDistance(packet.getX(), packet.getY(), packet.getZ()) > 6.0) {
                            continue;
                        }
                        e.setDead();
                    }
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        this.isActive = false;
        if (HunbaoAura.mc.player == null || HunbaoAura.mc.player.isDead) {
            return;
        }
        if (ModuleManager.getModuleByName("Surround").isEnabled()) {
            return;
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)HunbaoAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(e -> HunbaoAura.mc.player.getDistance(e) <= this.range.getValue()).map(entity -> entity).min(Comparator.comparing(c -> HunbaoAura.mc.player.getDistance(c))).orElse(null);
        final Iterator<BlockPos> it = this.ownCrystalPositions.iterator();
        while (it.hasNext()) {
            final BlockPos pos = it.next();
            final boolean exists = HunbaoAura.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).anyMatch(entity -> entity.getPosition().equals((Object)pos));
            if (exists) {
                continue;
            }
            it.remove();
        }
        if (this.explode.getValue() && crystal != null && (!this.explodeOwnOnly.getValue() || this.ownCrystalPositions.contains(crystal.getPosition()))) {
            if (!HunbaoAura.mc.player.canEntityBeSeen((Entity)crystal) && HunbaoAura.mc.player.getDistance((Entity)crystal) > this.walls.getValue()) {
                return;
            }
            if (this.waitTick.getValue() > 0.0) {
                final int waitValue = (int)(this.autoTickDelay.getValue() ? Math.ceil(20.0 - LagCompensator.INSTANCE.getTickRate()) : this.waitTick.getValue());
                if (this.waitCounter < waitValue) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
            }
            if (this.antiWeakness.getValue() && HunbaoAura.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if (!this.isAttacking) {
                    this.oldSlot = HunbaoAura.mc.player.inventory.currentItem;
                    this.isAttacking = true;
                }
                this.newSlot = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stack = HunbaoAura.mc.player.inventory.getStackInSlot(i);
                    if (stack != ItemStack.EMPTY) {
                        if (stack.getItem() instanceof ItemSword) {
                            this.newSlot = i;
                            break;
                        }
                        if (stack.getItem() instanceof ItemTool) {
                            this.newSlot = i;
                            break;
                        }
                    }
                }
                if (this.newSlot != -1) {
                    HunbaoAura.mc.player.inventory.currentItem = this.newSlot;
                    this.switchCooldown = true;
                }
            }
            this.isActive = true;
            if (this.rotate.getValue()) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)HunbaoAura.mc.player);
            }
            HunbaoAura.mc.playerController.attackEntity((EntityPlayer)HunbaoAura.mc.player, (Entity)crystal);
            HunbaoAura.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.isActive = false;
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                HunbaoAura.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
            this.isActive = false;
            int crystalSlot = (HunbaoAura.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? HunbaoAura.mc.player.inventory.currentItem : -1;
            final int n;
            if ((n = crystalSlot) == -1) {
                int l = 0;
                while (l < 9) {
                    if (HunbaoAura.mc.player.inventory.getStackInSlot(l).getItem() != Items.END_CRYSTAL) {
                        ++l;
                    }
                    else {
                        crystalSlot = l;
                    }
                }
            }
            boolean offhand = false;
            if (HunbaoAura.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                offhand = true;
            }
            else if (crystalSlot == -1) {
                return;
            }
            final List<BlockPos> blocks = this.findCrystalBlocks();
            final ArrayList<Entity> entities = new ArrayList<Entity>();
            if (this.targetPlayers.getValue()) {
                entities.addAll((Collection<? extends Entity>)HunbaoAura.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted(Comparator.comparing(e -> HunbaoAura.mc.player.getDistance(e))).collect(Collectors.toList()));
            }
            if (this.targetAnimals.getValue()) {
                entities.addAll((Collection<? extends Entity>)HunbaoAura.mc.world.getLoadedEntityList().stream().filter(e -> e instanceof EntityAnimal).sorted(Comparator.comparing(e -> HunbaoAura.mc.player.getDistance(e))).collect(Collectors.toList()));
            }
            if (this.targetMobs.getValue()) {
                entities.addAll((Collection<? extends Entity>)HunbaoAura.mc.world.getLoadedEntityList().stream().filter(e -> e instanceof EntityMob).sorted(Comparator.comparing(e -> HunbaoAura.mc.player.getDistance(e))).collect(Collectors.toList()));
            }
            BlockPos q = null;
            double damage = 0.5;
            for (final Entity entity2 : entities) {
                if (entity2 != HunbaoAura.mc.player && ((EntityLivingBase)entity2).getHealth() > 0.0f && !entity2.isDead) {
                    if (HunbaoAura.mc.player == null) {
                        continue;
                    }
                    for (final BlockPos blockPos : blocks) {
                        final double b = entity2.getDistanceSq(blockPos);
                        final double d;
                        final double self;
                        if (b < 169.0 && ((d = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity2)) >= this.minDmg.getValue() || ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() <= this.facePlace.getValue()) && d > damage && ((self = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)HunbaoAura.mc.player)) <= d || d < ((EntityLivingBase)entity2).getHealth()) && self - 0.5 <= HunbaoAura.mc.player.getHealth()) {
                            if (self > this.maxSelfDmg.getValue()) {
                                continue;
                            }
                            damage = d;
                            q = blockPos;
                            this.renderEnt = entity2;
                        }
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
            if (this.place.getValue()) {
                if (HunbaoAura.mc.player == null) {
                    return;
                }
                this.isActive = true;
                if (this.rotate.getValue()) {
                    this.lookAtPacket(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5, (EntityPlayer)HunbaoAura.mc.player);
                }
                final RayTraceResult result = HunbaoAura.mc.world.rayTraceBlocks(new Vec3d(HunbaoAura.mc.player.posX, HunbaoAura.mc.player.posY + HunbaoAura.mc.player.getEyeHeight(), HunbaoAura.mc.player.posZ), new Vec3d(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5));
                if (this.raytrace.getValue()) {
                    if (result == null || result.sideHit == null) {
                        q = null;
                        this.f = null;
                        this.render = null;
                        resetRotation();
                        this.isActive = false;
                        return;
                    }
                    this.f = result.sideHit;
                }
                if (!offhand && HunbaoAura.mc.player.inventory.currentItem != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        if (this.noGappleSwitch.getValue() && this.isEatingGap()) {
                            this.isActive = false;
                            resetRotation();
                            return;
                        }
                        this.isActive = true;
                        HunbaoAura.mc.player.inventory.currentItem = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    return;
                }
                if (this.switchCooldown) {
                    this.switchCooldown = false;
                    return;
                }
                if (q != null && HunbaoAura.mc.player != null) {
                    this.isActive = true;
                    if (this.raytrace.getValue() && this.f != null) {
                        HunbaoAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, this.f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        if (this.explodeOwnOnly.getValue()) {
                            this.ownCrystalPositions.add(q);
                        }
                    }
                    else {
                        HunbaoAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        if (this.explodeOwnOnly.getValue()) {
                            this.ownCrystalPositions.add(q.up());
                        }
                    }
                }
                this.isActive = false;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color c = new Color(255, 0, 0, 255);
        if (this.render != null && HunbaoAura.mc.player != null) {
            KamiTessellator.prepare(7);
            this.drawCurrentBlock(this.render, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            KamiTessellator.release();
        }
    }
    
    private boolean isEatingGap() {
        return HunbaoAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && HunbaoAura.mc.player.isHandActive();
    }
    
    private void drawCurrentBlock(final BlockPos render, final int r, final int g, final int b, final int a) {
        KamiTessellator.drawBox(render, r, g, b, a, 63);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (HunbaoAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || HunbaoAura.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && HunbaoAura.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && HunbaoAura.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && HunbaoAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && HunbaoAura.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(HunbaoAura.mc.player.posX), Math.floor(HunbaoAura.mc.player.posY), Math.floor(HunbaoAura.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
        final double range = this.placeRange.getValue();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)range, (int)range, false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
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
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)HunbaoAura.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = HunbaoAura.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        HunbaoAura.yaw = yaw1;
        HunbaoAura.pitch = pitch1;
        HunbaoAura.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (HunbaoAura.isSpoofingAngles) {
            HunbaoAura.yaw = HunbaoAura.mc.player.rotationYaw;
            HunbaoAura.pitch = HunbaoAura.mc.player.rotationPitch;
            HunbaoAura.isSpoofingAngles = false;
        }
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        dirz /= len;
        double yaw = Math.atan2(dirz, dirx /= len);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        final double[] array = new double[2];
        yaw = (array[0] = yaw + 90.0);
        array[1] = pitch;
        return array;
    }
    
    public void onEnable() {
        KamiMod.EVENT_BUS.subscribe((Object)this);
        this.isActive = false;
    }
    
    public void onDisable() {
        KamiMod.EVENT_BUS.unsubscribe((Object)this);
        this.render = null;
        this.renderEnt = null;
        resetRotation();
        this.isActive = false;
    }
}
