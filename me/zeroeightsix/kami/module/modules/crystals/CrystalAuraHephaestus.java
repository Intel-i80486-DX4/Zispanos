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
import net.minecraft.client.entity.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import java.util.*;
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalHephaestus", category = Category.CRYSTALS)
public class CrystalAuraHephaestus extends Module
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
    
    public CrystalAuraHephaestus() {
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
            if (packet instanceof CPacketPlayer && CrystalAuraHephaestus.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)CrystalAuraHephaestus.yaw;
                ((CPacketPlayer)packet).pitch = (float)CrystalAuraHephaestus.pitch;
            }
        }, new Predicate[0]);
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAuraHephaestus.mc.player.posX), Math.floor(CrystalAuraHephaestus.mc.player.posY), Math.floor(CrystalAuraHephaestus.mc.player.posZ));
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalAuraHephaestus.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = CrystalAuraHephaestus.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        CrystalAuraHephaestus.yaw = yaw1;
        CrystalAuraHephaestus.pitch = pitch1;
        CrystalAuraHephaestus.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (CrystalAuraHephaestus.isSpoofingAngles) {
            CrystalAuraHephaestus.yaw = CrystalAuraHephaestus.mc.player.rotationYaw;
            CrystalAuraHephaestus.pitch = CrystalAuraHephaestus.mc.player.rotationPitch;
            CrystalAuraHephaestus.isSpoofingAngles = false;
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
        if (CrystalAuraHephaestus.mc.player == null) {
            return;
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)CrystalAuraHephaestus.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> CrystalAuraHephaestus.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && CrystalAuraHephaestus.mc.player.getDistance((Entity)crystal) <= this.hitRange.getValue() && this.rayTraceHitCheck(crystal)) {
            if (this.hitDelayCounter >= this.hitTickDelay.getValue()) {
                this.hitDelayCounter = 0;
                if (this.antiWeakness.getValue() && CrystalAuraHephaestus.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = CrystalAuraHephaestus.mc.player.inventory.currentItem;
                        this.isAttacking = true;
                    }
                    this.newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = CrystalAuraHephaestus.mc.player.inventory.getStackInSlot(i);
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
                        CrystalAuraHephaestus.mc.player.inventory.currentItem = this.newSlot;
                        this.switchCooldown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAuraHephaestus.mc.player);
                CrystalAuraHephaestus.mc.playerController.attackEntity((EntityPlayer)CrystalAuraHephaestus.mc.player, (Entity)crystal);
                CrystalAuraHephaestus.mc.player.swingArm(EnumHand.MAIN_HAND);
                return;
            }
            ++this.hitDelayCounter;
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                CrystalAuraHephaestus.mc.player.inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
            int crystalSlot = (CrystalAuraHephaestus.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? CrystalAuraHephaestus.mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (CrystalAuraHephaestus.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                        crystalSlot = l;
                        break;
                    }
                }
            }
            boolean offhand = false;
            if (CrystalAuraHephaestus.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                offhand = true;
            }
            else if (crystalSlot == -1) {
                return;
            }
            final List<Entity> entities = (List<Entity>)CrystalAuraHephaestus.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted((entity1, entity2) -> Float.compare(CrystalAuraHephaestus.mc.player.getDistance(entity1), CrystalAuraHephaestus.mc.player.getDistance(entity2))).collect(Collectors.toList());
            final List<BlockPos> blocks = this.findCrystalBlocks();
            BlockPos targetBlock = null;
            double targetBlockDamage = 0.0;
            this.target = null;
            for (final Entity entity3 : entities) {
                if (entity3 == CrystalAuraHephaestus.mc.player) {
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
                    final double selfDamage = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)CrystalAuraHephaestus.mc.player);
                    final float healthTarget = testTarget.getHealth() + testTarget.getAbsorptionAmount();
                    final float healthSelf = CrystalAuraHephaestus.mc.player.getHealth() + CrystalAuraHephaestus.mc.player.getAbsorptionAmount();
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
                if (!offhand && CrystalAuraHephaestus.mc.player.inventory.currentItem != crystalSlot) {
                    if (this.autoSwitch.getValue()) {
                        CrystalAuraHephaestus.mc.player.inventory.currentItem = crystalSlot;
                        resetRotation();
                        this.switchCooldown = true;
                    }
                    return;
                }
                this.lookAtPacket(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5, (EntityPlayer)CrystalAuraHephaestus.mc.player);
                final RayTraceResult result = CrystalAuraHephaestus.mc.world.rayTraceBlocks(new Vec3d(CrystalAuraHephaestus.mc.player.posX, CrystalAuraHephaestus.mc.player.posY + CrystalAuraHephaestus.mc.player.getEyeHeight(), CrystalAuraHephaestus.mc.player.posZ), new Vec3d(targetBlock.x + 0.5, targetBlock.y - 0.5, targetBlock.z + 0.5));
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
                CrystalAuraHephaestus.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(targetBlock, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
            }
            if (this.spoofRotations.getValue() && CrystalAuraHephaestus.isSpoofingAngles) {
                if (CrystalAuraHephaestus.togglePitch) {
                    final EntityPlayerSP player = CrystalAuraHephaestus.mc.player;
                    player.rotationPitch += (float)4.0E-4;
                    CrystalAuraHephaestus.togglePitch = false;
                }
                else {
                    final EntityPlayerSP player2 = CrystalAuraHephaestus.mc.player;
                    player2.rotationPitch -= (float)4.0E-4;
                    CrystalAuraHephaestus.togglePitch = true;
                }
            }
        }
    }
    
    private boolean rayTraceHitCheck(final EntityEnderCrystal crystal) {
        return !this.rayTraceHit.getValue() || CrystalAuraHephaestus.mc.player.canEntityBeSeen((Entity)crystal);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (CrystalAuraHephaestus.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAuraHephaestus.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAuraHephaestus.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAuraHephaestus.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAuraHephaestus.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAuraHephaestus.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.placeRange.getValue().floatValue(), this.placeRange.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
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
    
    public void onEnable() {
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("AutoCrystalHephaestus <- " + ChatFormatting.GREEN + "Enabled!");
        }
        this.hitDelayCounter = 0;
    }
    
    public void onDisable() {
        this.renderBlock = null;
        this.target = null;
        resetRotation();
        if (this.announceUsage.getValue()) {
            Command.sendChatMessage("AutoCrystalHephaestus -> " + ChatFormatting.RED + "Disabled!");
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
        CrystalAuraHephaestus.togglePitch = false;
    }
    
    private enum RenderMode
    {
        UP, 
        BLOCK, 
        NONE;
    }
}
