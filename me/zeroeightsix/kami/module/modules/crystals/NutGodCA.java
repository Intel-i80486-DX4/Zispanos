//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import net.minecraft.entity.player.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;
import net.minecraft.network.*;
import java.util.function.*;
import net.minecraft.entity.item.*;
import java.util.stream.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.modules.chat.*;
import java.util.*;
import net.minecraft.client.entity.*;
import me.zeroeightsix.kami.event.events.*;
import java.awt.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import me.zeroeightsix.kami.command.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.network.play.client.*;

@Info(name = "NutGodCA", category = Category.CRYSTALS)
public class NutGodCA extends Module
{
    private Setting<Boolean> place;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> antiStuck;
    private Setting<Boolean> multiPlace;
    private Setting<Boolean> alert;
    private Setting<Boolean> antiSui;
    private Setting<Integer> attackSpeed;
    private Setting<Integer> placeDelay;
    private Setting<Integer> enemyRange;
    private Setting<Integer> minDamage;
    private Setting<Integer> facePlace;
    private Setting<Integer> multiPlaceSpeed;
    private Setting<Integer> placeRange;
    private Setting<Integer> breakRange;
    private BlockPos render;
    public boolean isActive;
    private Entity renderEnt;
    private long placeSystemTime;
    private long breakSystemTime;
    private long chatSystemTime;
    private long multiPlaceSystemTime;
    private long antiStuckSystemTime;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private int newSlot;
    private int placements;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    private EntityPlayer target;
    private Setting<Integer> Red;
    private Setting<Integer> Green;
    private Setting<Integer> Blue;
    private Setting<Boolean> rainbow;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public NutGodCA() {
        this.isActive = false;
        this.Red = this.register(Settings.integerBuilder("Red").withMinimum(1).withMaximum(255).withValue(255));
        this.Green = this.register(Settings.integerBuilder("Green").withMinimum(1).withMaximum(255).withValue(255));
        this.Blue = this.register(Settings.integerBuilder("Blue").withMinimum(1).withMaximum(255).withValue(255));
        this.rainbow = this.register(Settings.b("Rainbow", true));
        this.place = this.register(Settings.b("Place", true));
        this.raytrace = this.register(Settings.b("RayTrace", false));
        this.autoSwitch = this.register(Settings.b("AutoSwitch", true));
        this.antiStuck = this.register(Settings.b("AntiStuck", true));
        this.multiPlace = this.register(Settings.b("MultiPlace", false));
        this.alert = this.register(Settings.b("ChatAlerts", true));
        this.antiSui = this.register(Settings.b("AntiSuicide", true));
        this.attackSpeed = this.register((Setting<Integer>)Settings.integerBuilder("AttackSpeed").withMinimum(0).withMaximum(20).withValue(17).build());
        this.placeDelay = this.register((Setting<Integer>)Settings.integerBuilder("PlaceDelay").withMinimum(0).withMaximum(50).withValue(0).build());
        this.enemyRange = this.register((Setting<Integer>)Settings.integerBuilder("EnemyRange").withMinimum(1).withMaximum(13).withValue(9).build());
        this.minDamage = this.register((Setting<Integer>)Settings.integerBuilder("MinDamage").withMinimum(0).withMaximum(16).withValue(4).build());
        this.facePlace = this.register((Setting<Integer>)Settings.integerBuilder("FacePlace").withMinimum(0).withMaximum(16).withValue(7).build());
        this.multiPlaceSpeed = this.register((Setting<Integer>)Settings.integerBuilder("MultiPlaceSpeed").withMinimum(1).withMaximum(10).withValue(4).build());
        this.placeRange = this.register((Setting<Integer>)Settings.integerBuilder("PlaceRange").withMinimum(1).withMaximum(6).withValue(6).build());
        this.breakRange = this.register((Setting<Integer>)Settings.integerBuilder("BreakRange").withMinimum(1).withMaximum(6).withValue(6).build());
        this.placeSystemTime = -1L;
        this.breakSystemTime = -1L;
        this.chatSystemTime = -1L;
        this.multiPlaceSystemTime = -1L;
        this.antiStuckSystemTime = -1L;
        this.switchCooldown = false;
        this.placements = 0;
        final Packet[] packet = { null };
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            packet[0] = event.getPacket();
            if (packet[0] instanceof CPacketPlayer && NutGodCA.isSpoofingAngles) {
                ((CPacketPlayer)packet[0]).yaw = (float)NutGodCA.yaw;
                ((CPacketPlayer)packet[0]).pitch = (float)NutGodCA.pitch;
            }
        }, (Predicate[])new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)NutGodCA.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> NutGodCA.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && NutGodCA.mc.player.getDistance((Entity)crystal) <= this.breakRange.getValue()) {
            if (System.nanoTime() / 1000000L - this.breakSystemTime >= 420 - this.attackSpeed.getValue() * 20) {
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)NutGodCA.mc.player);
                NutGodCA.mc.playerController.attackEntity((EntityPlayer)NutGodCA.mc.player, (Entity)crystal);
                NutGodCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.breakSystemTime = System.nanoTime() / 1000000L;
            }
            if (this.multiPlace.getValue()) {
                if (System.nanoTime() / 1000000L - this.multiPlaceSystemTime >= 20 * this.multiPlaceSpeed.getValue() && System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 400 + (400 - this.attackSpeed.getValue() * 20)) {
                    this.multiPlaceSystemTime = System.nanoTime() / 1000000L;
                    return;
                }
            }
            else if (System.nanoTime() / 1000000L - this.antiStuckSystemTime <= 400 + (400 - this.attackSpeed.getValue() * 20)) {
                return;
            }
        }
        else {
            resetRotation();
        }
        int crystalSlot = (NutGodCA.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? NutGodCA.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (NutGodCA.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (NutGodCA.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        Entity ent = null;
        Entity lastTarget = null;
        BlockPos finalPos = null;
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)NutGodCA.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != NutGodCA.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                if (NutGodCA.mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (!canBlockBeSeen(blockPos) && NutGodCA.mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
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
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)NutGodCA.mc.player);
                    if (this.antiSui.getValue()) {
                        if (NutGodCA.mc.player.getHealth() + NutGodCA.mc.player.getAbsorptionAmount() - self <= 7.0) {
                            continue;
                        }
                        if (self > d) {
                            continue;
                        }
                    }
                    damage = d;
                    finalPos = blockPos;
                    ent = entity2;
                    lastTarget = entity2;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.renderEnt = null;
            resetRotation();
            return;
        }
        if (lastTarget instanceof EntityPlayer && ModuleManager.getModuleByName("AutoGG").isEnabled()) {
            final AutoGG autogg = (AutoGG)ModuleManager.getModuleByName("AutoGG");
            autogg.addTargetedPlayer(lastTarget.getName());
        }
        this.render = finalPos;
        this.renderEnt = ent;
        if (this.place.getValue()) {
            if (!offhand && NutGodCA.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    NutGodCA.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5, (EntityPlayer)NutGodCA.mc.player);
            final RayTraceResult result = NutGodCA.mc.world.rayTraceBlocks(new Vec3d(NutGodCA.mc.player.posX, NutGodCA.mc.player.posY + NutGodCA.mc.player.getEyeHeight(), NutGodCA.mc.player.posZ), new Vec3d(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5));
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
                NutGodCA.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
            }
        }
        if (NutGodCA.isSpoofingAngles) {
            if (NutGodCA.togglePitch) {
                final EntityPlayerSP player;
                final EntityPlayerSP player = player = NutGodCA.mc.player;
                player.rotationPitch += 4.0E-4f;
                NutGodCA.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2;
                final EntityPlayerSP player2 = player2 = NutGodCA.mc.player;
                player2.rotationPitch -= 4.0E-4f;
                NutGodCA.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            final float[] hue = { System.currentTimeMillis() % 11520L / 11520.0f };
            final int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            if (this.rainbow.getValue()) {
                KamiTessellator.prepare(7);
                KamiTessellator.drawBox(this.render, r, g, b, 77, 63);
                KamiTessellator.release();
                KamiTessellator.prepare(7);
                KamiTessellator.drawBoundingBoxBlockPos(this.render, 1.0f, r, g, b, 255);
            }
            else {
                KamiTessellator.prepare(7);
                KamiTessellator.drawBox(this.render, (int)this.Red.getValue(), (int)this.Green.getValue(), (int)this.Blue.getValue(), 77, 63);
                KamiTessellator.release();
                KamiTessellator.prepare(7);
                KamiTessellator.drawBoundingBoxBlockPos(this.render, 1.0f, (int)this.Red.getValue(), (int)this.Green.getValue(), (int)this.Blue.getValue(), 244);
            }
            KamiTessellator.release();
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (NutGodCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || NutGodCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && NutGodCA.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && NutGodCA.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && NutGodCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && NutGodCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(NutGodCA.mc.player.posX), Math.floor(NutGodCA.mc.player.posY), Math.floor(NutGodCA.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
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
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)NutGodCA.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = NutGodCA.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    public static boolean canBlockBeSeen(final BlockPos blockPos) {
        return NutGodCA.mc.world.rayTraceBlocks(new Vec3d(NutGodCA.mc.player.posX, NutGodCA.mc.player.posY + NutGodCA.mc.player.getEyeHeight(), NutGodCA.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        NutGodCA.yaw = yaw1;
        NutGodCA.pitch = pitch1;
        NutGodCA.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (NutGodCA.isSpoofingAngles) {
            NutGodCA.yaw = NutGodCA.mc.player.rotationYaw;
            NutGodCA.pitch = NutGodCA.mc.player.rotationPitch;
            NutGodCA.isSpoofingAngles = false;
        }
    }
    
    @Override
    protected void onEnable() {
        if (this.alert.getValue() && NutGodCA.mc.world != null) {
            Command.sendRawChatMessage("§aAutoCrystal ON");
        }
    }
    
    public void onDisable() {
        if (this.alert.getValue() && NutGodCA.mc.world != null) {
            Command.sendRawChatMessage("§cAutoCrystal" + ChatFormatting.RED.toString() + "OFF");
        }
        this.render = null;
        resetRotation();
    }
    
    static {
        NutGodCA.togglePitch = false;
    }
}
