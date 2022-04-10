//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import java.util.stream.*;
import net.minecraft.network.*;
import java.util.*;
import net.minecraft.client.entity.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.module.modules.render.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import me.zeroeightsix.kami.module.modules.gui.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalKAMI", category = Category.CRYSTALS, description = "Places End Crystals to kill enemies")
public class CrystalAuraKAMI extends Module
{
    private Setting<Boolean> defaultSetting;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Double> range;
    private Setting<Boolean> tracer;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> checkAbsorption;
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCoolDown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public CrystalAuraKAMI() {
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.players = this.register(Settings.b("Players", true));
        this.mobs = this.register(Settings.b("Mobs", false));
        this.animals = this.register(Settings.b("Animals", false));
        this.place = this.register(Settings.b("Place", false));
        this.explode = this.register(Settings.b("Explode", false));
        this.range = this.register(Settings.d("Range", 4.0));
        this.tracer = this.register(Settings.b("Tracer", true));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", false));
        this.checkAbsorption = this.register(Settings.b("Check Absorption", true));
        this.systemTime = -1L;
        this.switchCoolDown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && CrystalAuraKAMI.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)CrystalAuraKAMI.yaw;
                ((CPacketPlayer)packet).pitch = (float)CrystalAuraKAMI.pitch;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.autoSwitch.setValue(true);
            this.players.setValue(true);
            this.mobs.setValue(false);
            this.animals.setValue(false);
            this.place.setValue(false);
            this.explode.setValue(false);
            this.range.setValue(4.0);
            this.tracer.setValue(true);
            this.antiWeakness.setValue(false);
            this.checkAbsorption.setValue(true);
            this.defaultSetting.setValue(false);
            Command.sendChatMessage(this.getChatName() + " Set to defaults!");
            Command.sendChatMessage(this.getChatName() + " Close and reopen the " + this.getName() + " settings menu to see changes");
        }
        final EntityEnderCrystal crystal = (EntityEnderCrystal)CrystalAuraKAMI.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> CrystalAuraKAMI.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && CrystalAuraKAMI.mc.player.getDistance((Entity)crystal) <= this.range.getValue()) {
            if (System.nanoTime() / 1000000L - this.systemTime >= 250L) {
                if (this.antiWeakness.getValue() && CrystalAuraKAMI.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
                        this.isAttacking = true;
                    }
                    this.newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
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
                        Wrapper.getPlayer().inventory.currentItem = this.newSlot;
                        this.switchCoolDown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)CrystalAuraKAMI.mc.player);
                CrystalAuraKAMI.mc.playerController.attackEntity((EntityPlayer)CrystalAuraKAMI.mc.player, (Entity)crystal);
                CrystalAuraKAMI.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.systemTime = System.nanoTime() / 1000000L;
            }
            return;
        }
        resetRotation();
        if (this.oldSlot != -1) {
            Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
        }
        this.isAttacking = false;
        int crystalSlot = (CrystalAuraKAMI.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? CrystalAuraKAMI.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (CrystalAuraKAMI.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (CrystalAuraKAMI.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        if (this.players.getValue()) {
            entities.addAll((Collection<? extends Entity>)CrystalAuraKAMI.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        final boolean b2;
        entities.addAll((Collection<? extends Entity>)CrystalAuraKAMI.mc.world.loadedEntityList.stream().filter(entity -> {
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
            if (entity2 != CrystalAuraKAMI.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b >= 169.0) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)CrystalAuraKAMI.mc.player);
                    float enemyAbsorption = 0.0f;
                    float playerAbsorption = 0.0f;
                    if (this.checkAbsorption.getValue()) {
                        enemyAbsorption = ((EntityLivingBase)entity2).getAbsorptionAmount();
                        playerAbsorption = CrystalAuraKAMI.mc.player.getAbsorptionAmount();
                    }
                    if (self > d && d >= ((EntityLivingBase)entity2).getHealth() + enemyAbsorption) {
                        continue;
                    }
                    if (self - 0.5 > CrystalAuraKAMI.mc.player.getHealth() + playerAbsorption) {
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
            resetRotation();
            return;
        }
        this.render = q;
        if (this.place.getValue()) {
            if (!offhand && CrystalAuraKAMI.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    CrystalAuraKAMI.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCoolDown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)CrystalAuraKAMI.mc.player);
            final RayTraceResult result = CrystalAuraKAMI.mc.world.rayTraceBlocks(new Vec3d(CrystalAuraKAMI.mc.player.posX, CrystalAuraKAMI.mc.player.posY + CrystalAuraKAMI.mc.player.getEyeHeight(), CrystalAuraKAMI.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            }
            else {
                f = result.sideHit;
            }
            if (this.switchCoolDown) {
                this.switchCoolDown = false;
                return;
            }
            CrystalAuraKAMI.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (CrystalAuraKAMI.isSpoofingAngles) {
            if (CrystalAuraKAMI.togglePitch) {
                final EntityPlayerSP player = CrystalAuraKAMI.mc.player;
                player.rotationPitch += (float)4.0E-4;
                CrystalAuraKAMI.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2 = CrystalAuraKAMI.mc.player;
                player2.rotationPitch -= (float)4.0E-4;
                CrystalAuraKAMI.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, 1157627903, 63);
            KamiTessellator.release();
            if (this.renderEnt != null && this.tracer.getValue()) {
                final Vec3d p = EntityUtil.getInterpolatedRenderPos(this.renderEnt, CrystalAuraKAMI.mc.getRenderPartialTicks());
                Tracers.drawLineFromPosToPos(this.render.x - CrystalAuraKAMI.mc.getRenderManager().renderPosX + 0.5, this.render.y - CrystalAuraKAMI.mc.getRenderManager().renderPosY + 1.0, this.render.z - CrystalAuraKAMI.mc.getRenderManager().renderPosZ + 0.5, p.x, p.y, p.z, (double)this.renderEnt.getEyeHeight(), 1.0f, 1.0f, 1.0f, 1.0f);
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
        return (CrystalAuraKAMI.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || CrystalAuraKAMI.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && CrystalAuraKAMI.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && CrystalAuraKAMI.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && CrystalAuraKAMI.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && CrystalAuraKAMI.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(CrystalAuraKAMI.mc.player.posX), Math.floor(CrystalAuraKAMI.mc.player.posY), Math.floor(CrystalAuraKAMI.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)CrystalAuraKAMI.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = CrystalAuraKAMI.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        CrystalAuraKAMI.yaw = yaw1;
        CrystalAuraKAMI.pitch = pitch1;
        CrystalAuraKAMI.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (CrystalAuraKAMI.isSpoofingAngles) {
            CrystalAuraKAMI.yaw = CrystalAuraKAMI.mc.player.rotationYaw;
            CrystalAuraKAMI.pitch = CrystalAuraKAMI.mc.player.rotationPitch;
            CrystalAuraKAMI.isSpoofingAngles = false;
        }
    }
    
    public void onDisable() {
        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(InfoOverlay.getItems(Items.END_CRYSTAL));
    }
    
    static {
        CrystalAuraKAMI.togglePitch = false;
    }
}
