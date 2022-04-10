//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import net.minecraft.network.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.module.modules.render.*;
import me.zeroeightsix.kami.module.modules.misc.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalLunar", category = Category.CRYSTALS)
public class AutoCrystalLunar extends Module
{
    private Setting<Boolean> place;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> offhandSwitch;
    private Setting<Boolean> antiStuck;
    private Setting<Boolean> multiPlace;
    private Setting<Boolean> alert;
    private Setting<Boolean> antiSui;
    private Setting<Integer> attackSpeed;
    private Setting<Integer> placeDelay;
    private Setting<Integer> enemyRange;
    private Setting<Integer> minDamage;
    private Setting<Integer> maxDamage;
    private Setting<Integer> facePlace;
    private Setting<Integer> multiPlaceSpeed;
    private Setting<Integer> placeRange;
    private Setting<Integer> breakRange;
    private BlockPos render;
    private Entity renderEnt;
    private long placeSystemTime;
    private long breakSystemTime;
    private long chatSystemTime;
    private long antiStuckSystemTime;
    private long multiPlaceSystemTime;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private int newSlot;
    private int placements;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public AutoCrystalLunar() {
        this.place = this.register(Settings.b("Place", true));
        this.raytrace = this.register(Settings.b("RayTrace", false));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.offhandSwitch = this.register(Settings.b("Offhand Switch", false));
        this.antiStuck = this.register(Settings.b("Anti Stuck", true));
        this.multiPlace = this.register(Settings.b("Multi Place", false));
        this.alert = this.register(Settings.b("Chat Alerts", true));
        this.antiSui = this.register(Settings.b("Anti Suicide", true));
        this.attackSpeed = this.register((Setting<Integer>)Settings.integerBuilder("Attack Speed").withMinimum(0).withMaximum(20).withValue(17).build());
        this.placeDelay = this.register((Setting<Integer>)Settings.integerBuilder("Place Delay").withMinimum(0).withMaximum(50).withValue(0).build());
        this.enemyRange = this.register((Setting<Integer>)Settings.integerBuilder("Enemy Range").withMinimum(1).withMaximum(13).withValue(9).build());
        this.minDamage = this.register((Setting<Integer>)Settings.integerBuilder("Min Damage").withMinimum(0).withMaximum(16).withValue(4).build());
        this.maxDamage = this.register((Setting<Integer>)Settings.integerBuilder("Max Self Damage").withMinimum(0).withMaximum(20).withValue(11).build());
        this.facePlace = this.register((Setting<Integer>)Settings.integerBuilder("Min Health to Face Place").withMinimum(0).withMaximum(16).withValue(7).build());
        this.multiPlaceSpeed = this.register((Setting<Integer>)Settings.integerBuilder("Multi Place Speed").withMinimum(1).withMaximum(10).withValue(2).build());
        this.placeRange = this.register((Setting<Integer>)Settings.integerBuilder("Place Range").withMinimum(1).withMaximum(6).withValue(6).build());
        this.breakRange = this.register((Setting<Integer>)Settings.integerBuilder("Break Range").withMinimum(1).withMaximum(6).withValue(6).build());
        this.placeSystemTime = -1L;
        this.breakSystemTime = -1L;
        this.chatSystemTime = -1L;
        this.antiStuckSystemTime = -1L;
        this.multiPlaceSystemTime = -1L;
        this.switchCooldown = false;
        this.placements = 0;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && AutoCrystalLunar.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)AutoCrystalLunar.yaw;
                ((CPacketPlayer)packet).pitch = (float)AutoCrystalLunar.pitch;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystalLunar.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystalLunar.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && AutoCrystalLunar.mc.player.getDistance((Entity)crystal) <= this.breakRange.getValue()) {
            if (System.nanoTime() / 1000000L - this.breakSystemTime >= 420 - this.attackSpeed.getValue() * 20) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystalLunar.mc.player);
                AutoCrystalLunar.mc.playerController.attackEntity((EntityPlayer)AutoCrystalLunar.mc.player, (Entity)crystal);
                AutoCrystalLunar.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.breakSystemTime = System.nanoTime() / 1000000L;
            }
            if (this.multiPlace.getValue()) {
                if (System.nanoTime() / 1000000L - this.multiPlaceSystemTime <= this.multiPlaceSpeed.getValue() * 50 && this.multiPlaceSpeed.getValue() < 10) {
                    if (!this.antiStuck.getValue()) {
                        this.placements = 0;
                        return;
                    }
                    if (System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 700 - this.attackSpeed.getValue() * 20) {
                        this.multiPlaceSystemTime = System.nanoTime() / 1000000L;
                        return;
                    }
                }
            }
            else {
                if (!this.antiStuck.getValue()) {
                    return;
                }
                if (System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 700 - this.attackSpeed.getValue() * 20) {
                    return;
                }
            }
        }
        else {
            resetRotation();
        }
        int crystalSlot = (AutoCrystalLunar.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalLunar.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AutoCrystalLunar.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        if (this.offhandSwitch.getValue()) {
            InventoryUtils.OffhandCrystal();
        }
        boolean offhand = false;
        if (AutoCrystalLunar.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        Entity ent = null;
        BlockPos finalPos = null;
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)AutoCrystalLunar.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        double damage = 0.5;
        double prevSelf = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != AutoCrystalLunar.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                if (AutoCrystalLunar.mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (!canBlockBeSeen(blockPos) && AutoCrystalLunar.mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
                        continue;
                    }
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b > 56.2) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d < this.minDamage.getValue() && ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > this.facePlace.getValue()) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AutoCrystalLunar.mc.player);
                    if (this.maxDamage.getValue() <= self) {
                        continue;
                    }
                    if (this.antiSui.getValue()) {
                        if (AutoCrystalLunar.mc.player.getHealth() + AutoCrystalLunar.mc.player.getAbsorptionAmount() - self <= 7.0) {
                            continue;
                        }
                        if (self > d) {
                            continue;
                        }
                        if (self >= AutoCrystalLunar.mc.player.getHealth() + AutoCrystalLunar.mc.player.getAbsorptionAmount()) {
                            continue;
                        }
                    }
                    if (d <= damage && (Math.round(d) != Math.round(damage) || self >= prevSelf)) {
                        continue;
                    }
                    damage = d;
                    finalPos = blockPos;
                    ent = entity2;
                    prevSelf = self;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            resetRotation();
            return;
        }
        this.render = finalPos;
        this.renderEnt = ent;
        if (this.place.getValue()) {
            if (!offhand && AutoCrystalLunar.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    AutoCrystalLunar.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5, (EntityPlayer)AutoCrystalLunar.mc.player);
            final RayTraceResult result = AutoCrystalLunar.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalLunar.mc.player.posX, AutoCrystalLunar.mc.player.posY + AutoCrystalLunar.mc.player.getEyeHeight(), AutoCrystalLunar.mc.player.posZ), new Vec3d(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            }
            else {
                f = result.sideHit;
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.placeDelay.getValue() * 2) {
                AutoCrystalLunar.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
            }
        }
        if (AutoCrystalLunar.isSpoofingAngles) {
            if (AutoCrystalLunar.togglePitch) {
                AutoCrystalLunar.mc.player.rotationPitch += (float)4.0E-4;
                AutoCrystalLunar.togglePitch = false;
            }
            else {
                AutoCrystalLunar.mc.player.rotationPitch -= (float)4.0E-4;
                AutoCrystalLunar.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellatorLunar.prepare(7);
            KamiTessellatorLunar.drawBox(this.render, HUD.red(), HUD.green(), HUD.blue(), 83, 63);
            KamiTessellatorLunar.release();
            KamiTessellatorLunar.prepare(7);
            KamiTessellatorLunar.drawBoundingBoxBlockPos(this.render, 1.05f, HUD.red(), HUD.green(), HUD.blue(), 244);
            KamiTessellatorLunar.release();
        }
        if (this.renderEnt != null && Announcer.crystal() && ModuleManager.isModuleEnabled("Announcer") && System.nanoTime() / 1000000L - this.chatSystemTime >= Announcer.crystalDelay()) {
            Announcer.sendChatMessage("crystalled " + this.renderEnt.getName());
            this.chatSystemTime = System.nanoTime() / 1000000L;
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystalLunar.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystalLunar.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystalLunar.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystalLunar.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystalLunar.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystalLunar.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystalLunar.mc.player.posX), Math.floor(AutoCrystalLunar.mc.player.posY), Math.floor(AutoCrystalLunar.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.placeRange.getValue(), this.placeRange.getValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleblocks = new ArrayList<BlockPos>();
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystalLunar.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = AutoCrystalLunar.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    public static boolean canBlockBeSeen(final BlockPos blockPos) {
        return AutoCrystalLunar.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalLunar.mc.player.posX, AutoCrystalLunar.mc.player.posY + AutoCrystalLunar.mc.player.getEyeHeight(), AutoCrystalLunar.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystalLunar.yaw = yaw1;
        AutoCrystalLunar.pitch = pitch1;
        AutoCrystalLunar.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystalLunar.isSpoofingAngles) {
            AutoCrystalLunar.yaw = AutoCrystalLunar.mc.player.rotationYaw;
            AutoCrystalLunar.pitch = AutoCrystalLunar.mc.player.rotationPitch;
            AutoCrystalLunar.isSpoofingAngles = false;
        }
    }
    
    @Override
    protected void onEnable() {
        if (this.alert.getValue() && AutoCrystalLunar.mc.world != null) {
            Command.sendChatMessage("AutoCrystalLunar <- " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        if (this.alert.getValue() && AutoCrystalLunar.mc.world != null) {
            Command.sendChatMessage("AutoCrystalLunar -> " + ChatFormatting.RED + "Disabled!");
        }
        if (this.offhandSwitch.getValue()) {
            InventoryUtils.OffhandCrystalReset();
        }
        this.render = null;
        resetRotation();
    }
    
    static {
        AutoCrystalLunar.togglePitch = false;
    }
}
