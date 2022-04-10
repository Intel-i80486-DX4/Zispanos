//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;
import java.util.function.*;
import net.minecraft.entity.item.*;
import net.minecraft.item.*;
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
import com.mojang.realmsclient.gui.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;

@Info(name = "StormCrystalSoul", category = Category.CRYSTALS)
public class StormCrystal extends Module
{
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> players;
    private Setting<Boolean> place;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> explode;
    private Setting<Boolean> thing;
    private Setting<Double> range;
    private Setting<Boolean> antiWeakness;
    private Setting<Double> Pdelay;
    private Setting<Double> Bdelay;
    private Setting<Double> distance;
    private Setting<Boolean> alert;
    private Setting<Integer> MinDmg;
    private Setting<Integer> Red;
    private Setting<Integer> Green;
    private Setting<Integer> Blue;
    private Setting<Integer> Alpha;
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCooldown;
    private boolean isAttacking;
    private int oldSlot;
    private int newSlot;
    private int placements;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public StormCrystal() {
        this.autoSwitch = this.register(Settings.b("Auto Switch"));
        this.players = this.register(Settings.b("Players"));
        this.place = this.register(Settings.b("Place", true));
        this.raytrace = this.register(Settings.b("RayTrace", false));
        this.explode = this.register(Settings.b("Explode", true));
        this.thing = this.register(Settings.b("MutltiPlace", true));
        this.range = this.register(Settings.d("Range", 6.0));
        this.antiWeakness = this.register(Settings.b("Anti Weakness", false));
        this.Pdelay = this.register(Settings.d("Place Delay", 1.0));
        this.Bdelay = this.register(Settings.d("Break Delay", 1.0));
        this.distance = this.register(Settings.d("Enemy Distance", 6.0));
        this.alert = this.register(Settings.b("Chat Alert", true));
        this.MinDmg = this.register(Settings.integerBuilder("Min Dmg").withMinimum(0).withMaximum(16).withValue(2));
        this.Red = this.register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(255));
        this.Green = this.register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
        this.Blue = this.register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(255));
        this.Alpha = this.register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(70).withValue(45));
        this.systemTime = -1L;
        this.switchCooldown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && StormCrystal.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)StormCrystal.yaw;
                ((CPacketPlayer)packet).pitch = (float)StormCrystal.pitch;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)StormCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> StormCrystal.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && StormCrystal.mc.player.getDistance((Entity)crystal) <= this.range.getValue()) {
            if (System.nanoTime() / 1000000L - this.systemTime >= this.Bdelay.getValue()) {
                if (this.antiWeakness.getValue() && StormCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
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
                        this.switchCooldown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)StormCrystal.mc.player);
                StormCrystal.mc.playerController.attackEntity((EntityPlayer)StormCrystal.mc.player, (Entity)crystal);
                StormCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.systemTime = System.nanoTime() / 1000000L;
            }
            if (!this.thing.getValue()) {
                return;
            }
            if (this.placements == 3) {
                this.placements = 0;
                return;
            }
        }
        else {
            resetRotation();
            if (this.oldSlot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.oldSlot;
                this.oldSlot = -1;
            }
            this.isAttacking = false;
        }
        int crystalSlot = (StormCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? StormCrystal.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (StormCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (StormCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        if (this.players.getValue()) {
            entities.addAll((Collection<? extends Entity>)StormCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        BlockPos q = null;
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != StormCrystal.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b >= this.distance.getValue() * this.distance.getValue()) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)StormCrystal.mc.player);
                    if (self > d && d >= ((EntityLivingBase)entity2).getHealth()) {
                        continue;
                    }
                    if (self - 0.5 > StormCrystal.mc.player.getHealth()) {
                        continue;
                    }
                    if (d < this.MinDmg.getValue()) {
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
            if (!offhand && StormCrystal.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    StormCrystal.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)StormCrystal.mc.player);
            EnumFacing f;
            if (this.raytrace.getValue()) {
                final RayTraceResult result = StormCrystal.mc.world.rayTraceBlocks(new Vec3d(StormCrystal.mc.player.posX, StormCrystal.mc.player.posY + StormCrystal.mc.player.getEyeHeight(), StormCrystal.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
                if (result == null || result.sideHit == null) {
                    f = EnumFacing.UP;
                }
                else {
                    f = result.sideHit;
                }
            }
            else {
                f = EnumFacing.DOWN;
            }
            if (this.switchCooldown) {
                this.switchCooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - this.systemTime >= this.Pdelay.getValue()) {
                StormCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.systemTime = System.nanoTime() / 1000000L;
            }
        }
        if (StormCrystal.isSpoofingAngles) {
            if (StormCrystal.togglePitch) {
                StormCrystal.mc.player.rotationPitch += (float)4.0E-4;
                StormCrystal.togglePitch = false;
            }
            else {
                StormCrystal.mc.player.rotationPitch -= (float)4.0E-4;
                StormCrystal.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, (int)this.Red.getValue(), (int)this.Green.getValue(), (int)this.Blue.getValue(), (int)this.Alpha.getValue(), 63);
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
        return (StormCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || StormCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && StormCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && StormCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && StormCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && StormCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(StormCrystal.mc.player.posX), Math.floor(StormCrystal.mc.player.posY), Math.floor(StormCrystal.mc.player.posZ));
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
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)StormCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = StormCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        StormCrystal.yaw = yaw1;
        StormCrystal.pitch = pitch1;
        StormCrystal.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (StormCrystal.isSpoofingAngles) {
            StormCrystal.yaw = StormCrystal.mc.player.rotationYaw;
            StormCrystal.pitch = StormCrystal.mc.player.rotationPitch;
            StormCrystal.isSpoofingAngles = false;
        }
    }
    
    @Override
    protected void onEnable() {
        if (this.alert.getValue()) {
            Command.sendChatMessage("StormCrystalSoul <- " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        if (this.alert.getValue()) {
            Command.sendChatMessage("StormCrystalSoul -> " + ChatFormatting.RED + "Disabled!");
        }
        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    static {
        StormCrystal.togglePitch = false;
    }
}
