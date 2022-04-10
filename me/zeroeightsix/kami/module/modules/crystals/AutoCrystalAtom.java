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
import net.minecraft.entity.boss.*;
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
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalAtom", category = Category.CRYSTALS)
public class AutoCrystalAtom extends Module
{
    private Setting explode;
    private Setting autoTickDelay;
    private Setting waitTick;
    private Setting range;
    private Setting walls;
    private Setting antiWeakness;
    private Setting nodesync;
    private Setting place;
    private Setting autoSwitch;
    private Setting noGappleSwitch;
    private Setting placeRange;
    private Setting minDmg;
    private Setting facePlace;
    private Setting raytrace;
    private Setting rotate;
    private Setting spoofRotations;
    private Setting chat;
    private Setting maxSelfDmg;
    private Setting targetWithers;
    private BlockPos render;
    private Entity renderEnt;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int waitCounter;
    EnumFacing f;
    private boolean isActive;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Receive> packetSendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> packetReceiveListener;
    
    public AutoCrystalAtom() {
        this.explode = this.register(Settings.b("Explode"));
        this.autoTickDelay = this.register(Settings.b("Auto Tick Delay", false));
        this.waitTick = this.register(Settings.d("Tick Delay", 1.0));
        this.range = this.register(Settings.d("Hit Range", 5.0));
        this.walls = this.register(Settings.d("Walls Range", 3.5));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", true));
        this.nodesync = this.register(Settings.b("No Desync", true));
        this.place = this.register(Settings.b("Place", true));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.noGappleSwitch = this.register(Settings.b("No Gap Switch", false));
        this.placeRange = this.register(Settings.d("Place Range", 5.0));
        this.minDmg = this.register(Settings.d("Min Damage", 5.0));
        this.facePlace = this.register(Settings.d("Faceplace HP", 6.0));
        this.raytrace = this.register(Settings.b("Ray Trace", false));
        this.rotate = this.register(Settings.b("Rotate", true));
        this.spoofRotations = this.register(Settings.b("Spoof Angles", true));
        this.chat = this.register(Settings.b("Toggle Messages", true));
        this.maxSelfDmg = this.register(Settings.d("Max Self Dmg", 10.0));
        this.targetWithers = this.register(Settings.b("Withers", false));
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.packetSendListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && AutoCrystalAtom.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)AutoCrystalAtom.yaw;
                ((CPacketPlayer)packet).pitch = (float)AutoCrystalAtom.pitch;
            }
        }, new Predicate[0]);
        this.packetReceiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (event.getPacket() instanceof SPacketSoundEffect && this.nodesync.getValue()) {
                final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
                if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    for (final Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0) {
                            e.setDead();
                        }
                    }
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        this.isActive = false;
        if (AutoCrystalAtom.mc.player != null && !AutoCrystalAtom.mc.player.isDead && !ModuleManager.getModuleByName("Surround").isEnabled()) {
            final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystalAtom.mc.world.loadedEntityList.stream().filter(entityx -> entityx instanceof EntityEnderCrystal).filter(e -> AutoCrystalAtom.mc.player.getDistance(e) <= this.range.getValue()).map(entityx -> entityx).min(Comparator.comparing(c -> AutoCrystalAtom.mc.player.getDistance(c))).orElse(null);
            if (this.explode.getValue() && crystal != null) {
                if (AutoCrystalAtom.mc.player.canEntityBeSeen((Entity)crystal) || AutoCrystalAtom.mc.player.getDistance((Entity)crystal) <= this.walls.getValue()) {
                    if (this.waitTick.getValue() > 0.0) {
                        final int crystalSlot = (int)(this.autoTickDelay.getValue() ? Math.ceil(20.0 - LagCompensator.INSTANCE.getTickRate()) : this.waitTick.getValue());
                        if (this.waitCounter < crystalSlot) {
                            ++this.waitCounter;
                            return;
                        }
                        this.waitCounter = 0;
                    }
                    if (this.antiWeakness.getValue() && AutoCrystalAtom.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                        if (!this.isAttacking) {
                            this.oldSlot = AutoCrystalAtom.mc.player.inventory.currentItem;
                            this.isAttacking = true;
                        }
                        this.newSlot = -1;
                        for (int crystalSlot = 0; crystalSlot < 9; ++crystalSlot) {
                            final ItemStack stack = AutoCrystalAtom.mc.player.inventory.getStackInSlot(crystalSlot);
                            if (stack != ItemStack.EMPTY) {
                                if (stack.getItem() instanceof ItemSword) {
                                    this.newSlot = crystalSlot;
                                    break;
                                }
                                if (stack.getItem() instanceof ItemTool) {
                                    this.newSlot = crystalSlot;
                                    break;
                                }
                            }
                        }
                        if (this.newSlot != -1) {
                            AutoCrystalAtom.mc.player.inventory.currentItem = this.newSlot;
                            this.switchCooldown = true;
                        }
                    }
                    this.isActive = true;
                    if (this.rotate.getValue()) {
                        this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystalAtom.mc.player);
                    }
                    AutoCrystalAtom.mc.playerController.attackEntity((EntityPlayer)AutoCrystalAtom.mc.player, (Entity)crystal);
                    AutoCrystalAtom.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.isActive = false;
                }
            }
            else {
                resetRotation();
                if (this.oldSlot != -1) {
                    AutoCrystalAtom.mc.player.inventory.currentItem = this.oldSlot;
                    this.oldSlot = -1;
                }
                this.isAttacking = false;
                this.isActive = false;
                int crystalSlot = (AutoCrystalAtom.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalAtom.mc.player.inventory.currentItem : -1;
                if (crystalSlot == -1) {
                    for (int l = 0; l < 9; ++l) {
                        if (AutoCrystalAtom.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                            crystalSlot = l;
                            break;
                        }
                    }
                }
                boolean offhand = false;
                if (AutoCrystalAtom.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                    offhand = true;
                }
                else if (crystalSlot == -1) {
                    return;
                }
                final List blocks = this.findCrystalBlocks();
                final List entities = new ArrayList();
                entities.addAll((Collection)AutoCrystalAtom.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted(Comparator.comparing(e -> AutoCrystalAtom.mc.player.getDistance(e))).collect(Collectors.toList()));
                if (this.targetWithers.getValue()) {
                    entities.addAll((Collection)AutoCrystalAtom.mc.world.getLoadedEntityList().stream().filter(e -> e instanceof EntityWither).sorted(Comparator.comparing(e -> AutoCrystalAtom.mc.player.getDistance(e))).collect(Collectors.toList()));
                }
                BlockPos q = null;
                double damage = 0.5;
                for (final Entity entity : entities) {
                    if (entity != AutoCrystalAtom.mc.player && ((EntityLivingBase)entity).getHealth() > 0.0f && !entity.isDead && AutoCrystalAtom.mc.player != null) {
                        for (final BlockPos blockPos : blocks) {
                            final double b = entity.getDistanceSq(blockPos);
                            if (b < 169.0) {
                                final double d = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, entity);
                                if ((d < this.minDmg.getValue() && ((EntityLivingBase)entity).getHealth() + ((EntityLivingBase)entity).getAbsorptionAmount() > this.facePlace.getValue()) || d <= damage) {
                                    continue;
                                }
                                final double self = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5, (Entity)AutoCrystalAtom.mc.player);
                                if (self > d && d >= ((EntityLivingBase)entity).getHealth()) {
                                    continue;
                                }
                                if (self - 0.5 > AutoCrystalAtom.mc.player.getHealth() || self > this.maxSelfDmg.getValue()) {
                                    continue;
                                }
                                damage = d;
                                q = blockPos;
                                this.renderEnt = entity;
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
                    if (AutoCrystalAtom.mc.player == null) {
                        return;
                    }
                    this.isActive = true;
                    if (this.rotate.getValue()) {
                        this.lookAtPacket(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5, (EntityPlayer)AutoCrystalAtom.mc.player);
                    }
                    final RayTraceResult result = AutoCrystalAtom.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalAtom.mc.player.posX, AutoCrystalAtom.mc.player.posY + AutoCrystalAtom.mc.player.getEyeHeight(), AutoCrystalAtom.mc.player.posZ), new Vec3d(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5));
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
                    if (!offhand && AutoCrystalAtom.mc.player.inventory.currentItem != crystalSlot) {
                        if (this.autoSwitch.getValue()) {
                            if (this.noGappleSwitch.getValue() && this.isEatingGap()) {
                                this.isActive = false;
                                resetRotation();
                                return;
                            }
                            this.isActive = true;
                            AutoCrystalAtom.mc.player.inventory.currentItem = crystalSlot;
                            resetRotation();
                            this.switchCooldown = true;
                        }
                        return;
                    }
                    if (this.switchCooldown) {
                        this.switchCooldown = false;
                        return;
                    }
                    if (q != null && AutoCrystalAtom.mc.player != null) {
                        this.isActive = true;
                        if (this.raytrace.getValue() && this.f != null) {
                            AutoCrystalAtom.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, this.f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        }
                        else {
                            AutoCrystalAtom.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        }
                    }
                    this.isActive = false;
                }
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Color c = new Color(255, 0, 0, 255);
        if (this.render != null && AutoCrystalAtom.mc.player != null) {
            KamiTessellatorLunar.prepare(7);
            this.drawCurrentBlock(this.render, c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
            KamiTessellatorLunar.release();
        }
    }
    
    private boolean isEatingGap() {
        return AutoCrystalAtom.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold && AutoCrystalAtom.mc.player.isHandActive();
    }
    
    private void drawCurrentBlock(final BlockPos render, final int r, final int g, final int b, final int a) {
        KamiTessellatorLunar.drawBox(render, r, g, b, a, 63);
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystalAtom.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystalAtom.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystalAtom.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystalAtom.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystalAtom.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystalAtom.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystalAtom.mc.player.posX), Math.floor(AutoCrystalAtom.mc.player.posY), Math.floor(AutoCrystalAtom.mc.player.posZ));
    }
    
    private List findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
        final double range = this.placeRange.getValue();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), (float)range, (int)range, false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List)positions;
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystalAtom.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = AutoCrystalAtom.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystalAtom.yaw = yaw1;
        AutoCrystalAtom.pitch = pitch1;
        AutoCrystalAtom.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystalAtom.isSpoofingAngles) {
            AutoCrystalAtom.yaw = AutoCrystalAtom.mc.player.rotationYaw;
            AutoCrystalAtom.pitch = AutoCrystalAtom.mc.player.rotationPitch;
            AutoCrystalAtom.isSpoofingAngles = false;
        }
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        dirx /= len;
        diry /= len;
        dirz /= len;
        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        yaw += 90.0;
        return new double[] { yaw, pitch };
    }
    
    public void onEnable() {
        KamiMod.EVENT_BUS.subscribe((Object)this);
        this.isActive = false;
        if (this.chat.getValue() && AutoCrystalAtom.mc.player != null) {
            Command.sendChatMessage("AutoCrystalAtom3 <- " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        KamiMod.EVENT_BUS.unsubscribe((Object)this);
        this.render = null;
        this.renderEnt = null;
        resetRotation();
        this.isActive = false;
        if (this.chat.getValue()) {
            Command.sendChatMessage("AutoCrystalAtom -> " + ChatFormatting.RED + "Disabled!");
        }
    }
}
