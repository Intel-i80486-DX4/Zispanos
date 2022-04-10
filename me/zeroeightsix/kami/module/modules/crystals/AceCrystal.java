//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.player.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.enchantment.*;
import me.zeroeightsix.kami.event.events.*;
import java.awt.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import java.util.stream.*;
import net.minecraft.network.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import java.util.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;

@Info(name = "Ace_CrystalSalHack", category = Category.CRYSTALS)
public class AceCrystal extends Module
{
    private static boolean togglePitch;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> antiWeakness;
    private Setting<Integer> hitTickDelay;
    private Setting<Double> hitRange;
    private Setting<Double> placeRange;
    private Setting<Double> minDamage;
    private Setting<Boolean> spoofRotations;
    private Setting<Boolean> rayTraceHit;
    private Setting<RenderMode> renderMode;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<Boolean> announceUsage;
    private BlockPos renderBlock;
    private EntityPlayer target;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int hitDelayCounter;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public AceCrystal() {
        this.place = this.register(Settings.b("Place", true));
        this.explode = this.register(Settings.b("Explode", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.hitTickDelay = this.register((Setting<Integer>)Settings.integerBuilder("Hit Delay").withMinimum(0).withValue(4).withMaximum(20).build());
        this.hitRange = this.register((Setting<Double>)Settings.doubleBuilder("Hit Range").withMinimum(0.0).withValue(5.5).build());
        this.placeRange = this.register((Setting<Double>)Settings.doubleBuilder("Place Range").withMinimum(0.0).withValue(3.5).build());
        this.minDamage = this.register((Setting<Double>)Settings.doubleBuilder("Min Damage").withMinimum(0.0).withValue(2.0).withMaximum(20.0).build());
        this.spoofRotations = this.register(Settings.b("Spoof Rotations", false));
        this.rayTraceHit = this.register(Settings.b("RayTraceHit", false));
        this.renderMode = this.register(Settings.e("Render Mode", RenderMode.UP));
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withMinimum(0).withValue(104).withMaximum(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withMinimum(0).withValue(12).withMaximum(255).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withMinimum(0).withValue(35).withMaximum(255).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Alpha").withMinimum(0).withValue(169).withMaximum(255).build());
        this.announceUsage = this.register(Settings.b("Announce Usage", true));
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (!this.spoofRotations.getValue()) {
                return;
            }
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && AceCrystal.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)AceCrystal.yaw;
                ((CPacketPlayer)packet).pitch = (float)AceCrystal.pitch;
            }
        }, new Predicate[0]);
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AceCrystal.mc.player.posX), Math.floor(AceCrystal.mc.player.posY), Math.floor(AceCrystal.mc.player.posZ));
    }
    
    static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AceCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AceCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AceCrystal.yaw = yaw1;
        AceCrystal.pitch = pitch1;
        AceCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AceCrystal.isSpoofingAngles) {
            AceCrystal.yaw = AceCrystal.mc.player.rotationYaw;
            AceCrystal.pitch = AceCrystal.mc.player.rotationPitch;
            AceCrystal.isSpoofingAngles = false;
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.renderBlock != null && !this.renderMode.getValue().equals(RenderMode.NONE)) {
            this.drawBlock(this.renderBlock, this.red.getValue(), this.green.getValue(), this.blue.getValue());
        }
    }
    
    private void drawBlock(final BlockPos blockPos, final int r, final int g, final int b) {
        final Color color = new Color(r, g, b, this.alpha.getValue());
        KamiTessellator.prepare(7);
        if (this.renderMode.getValue().equals(RenderMode.UP)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 2);
        }
        else if (this.renderMode.getValue().equals(RenderMode.BLOCK)) {
            KamiTessellator.drawBox(blockPos, color.getRGB(), 63);
        }
        KamiTessellator.release();
    }
    
    @Override
    public void onUpdate() {
        if (AceCrystal.mc.player == null) {
            return;
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AceCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AceCrystal.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && AceCrystal.mc.player.getDistance((Entity)crystal) <= this.hitRange.getValue() && this.rayTraceHitCheck(crystal)) {
            if (this.hitDelayCounter >= this.hitTickDelay.getValue()) {
                this.hitDelayCounter = 0;
                if (this.antiWeakness.getValue() && AceCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = AceCrystal.mc.player.inventory.currentItem;
                        this.isAttacking = true;
                    }
                    this.newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = AceCrystal.mc.player.inventory.getStackInSlot(i);
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
                        AceCrystal.mc.player.inventory.currentItem = this.newSlot;
                        this.switchCooldown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AceCrystal.mc.player);
                AceCrystal.mc.playerController.attackEntity((EntityPlayer)AceCrystal.mc.player, (Entity)crystal);
                AceCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
            ++this.hitDelayCounter;
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                AceCrystal.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
            int crystalSlot = (AceCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AceCrystal.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AceCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                        crystalSlot = l;
                        break;
                    }
                }
            }
            boolean offhand = false;
            if (AceCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                offhand = true;
            }
            else if (crystalSlot == -1) {
                return;
            }
            final List<Entity> entities = (List<Entity>)AceCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted((entity1, entity2) -> Float.compare(AceCrystal.mc.player.getDistance(entity1), AceCrystal.mc.player.getDistance(entity2))).collect(Collectors.toList());
            final List<BlockPos> blocks = this.findCrystalBlocks();
            BlockPos targetBlock = null;
            double targetBlockDamage = 0.0;
            this.target = null;
            for (final Entity entity3 : entities) {
                if (entity3 == AceCrystal.mc.player) {
                    continue;
                }
                if (!(entity3 instanceof EntityPlayer)) {
                    continue;
                }
                final EntityPlayer testTarget = (EntityPlayer)entity3;
                if (testTarget.isDead) {
                    continue;
                }
                if (testTarget.getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (testTarget.getDistanceSq(blockPos) >= 169.0) {
                        continue;
                    }
                    final double targetDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)testTarget);
                    final double selfDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AceCrystal.mc.player);
                    final float healthTarget = testTarget.getHealth() + testTarget.getAbsorptionAmount();
                    final float healthSelf = AceCrystal.mc.player.getHealth() + AceCrystal.mc.player.getAbsorptionAmount();
                    if (targetDamage < this.minDamage.getValue()) {
                        continue;
                    }
                    if (selfDamage >= healthSelf - 0.5) {
                        continue;
                    }
                    if (selfDamage > targetDamage && targetDamage < healthTarget) {
                        continue;
                    }
                    if (targetDamage <= targetBlockDamage) {
                        continue;
                    }
                    targetBlock = blockPos;
                    targetBlockDamage = targetDamage;
                    this.target = testTarget;
                }
                if (this.target != null) {
                    break;
                }
            }
            if (this.target == null) {
                this.renderBlock = null;
                resetRotation();
                return;
            }
            this.renderBlock = targetBlock;
            if (this.place.getValue()) {
                if (!offhand && AceCrystal.mc.player.inventory.currentItem != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        AceCrystal.mc.player.inventory.currentItem = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    return;
                }
                this.lookAtPacket(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5, (EntityPlayer)AceCrystal.mc.player);
                final RayTraceResult result = AceCrystal.mc.world.rayTraceBlocks(new Vec3d(AceCrystal.mc.player.posX, AceCrystal.mc.player.posY + AceCrystal.mc.player.getEyeHeight(), AceCrystal.mc.player.posZ), new Vec3d(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5));
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
                AceCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.spoofRotations.getValue() && AceCrystal.isSpoofingAngles) {
                if (AceCrystal.togglePitch) {
                    AceCrystal.mc.player.rotationPitch += (float)4.0E-4;
                    AceCrystal.togglePitch = false;
                }
                else {
                    AceCrystal.mc.player.rotationPitch -= (float)4.0E-4;
                    AceCrystal.togglePitch = true;
                }
            }
        }
    }
    
    private boolean rayTraceHitCheck(final EntityEnderCrystal crystal) {
        return !this.rayTraceHit.getValue() || AceCrystal.mc.player.canEntityBeSeen((Entity)crystal);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AceCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AceCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AceCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AceCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AceCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AceCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)AutoCrystalAtom3.getSphere(getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public void onEnable() {
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[AceCrystalAura] " + ChatFormatting.GREEN.toString() + "Enabled!");
        }
        this.hitDelayCounter = 0;
    }
    
    public void onDisable() {
        this.renderBlock = null;
        this.target = null;
        resetRotation();
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("[AceCrystalAura] " + ChatFormatting.RED.toString() + "Disabled!");
        }
    }
    
    @Override
    public String getHudInfo() {
        if (this.target == null) {
            return "";
        }
        return this.target.getName().toUpperCase();
    }
    
    static {
        AceCrystal.togglePitch = false;
    }
    
    private enum RenderMode
    {
        UP, 
        BLOCK, 
        NONE;
    }
}
