//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import java.util.logging.*;
import net.minecraft.entity.player.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.network.*;
import java.util.function.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.command.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.entity.item.*;
import me.zeroeightsix.kami.*;
import java.util.stream.*;
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
import net.minecraft.network.play.client.*;

@Info(name = "MegynCA", description = "leaked penis hack crystalaura 2", category = Category.CRYSTALS)
public class TestCA extends Module
{
    private Setting<Integer> tickPlaceDelay;
    private Setting<Integer> msPlaceDelay;
    private Setting<Integer> tickBreakDelay;
    private Setting<Integer> msBreakDelay;
    private Setting<Double> placeRange;
    private Setting<Double> breakRange;
    private Setting<Integer> enemyRange;
    private Setting<Integer> minDamage;
    private Setting<Double> placeThroughWallsRange;
    private Setting<Integer> ignoreMinDamageThreshold;
    private Setting<Integer> friendProtectThreshold;
    private Setting<Integer> selfProtectThreshold;
    private Setting<Double> breakThroughWallsRange;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<delayMode> delayMode;
    private Setting<Boolean> ignoreMinDamageOnBreak;
    private Setting<Boolean> antiSuicide;
    private Setting<Boolean> antiStuck;
    private Setting<Boolean> onlyBreakOwnCrystals;
    private Setting<Boolean> multiplace;
    private Setting<Boolean> friendProtect;
    private Setting<Boolean> selfProtect;
    private Setting<Boolean> lockOn;
    private Setting<Boolean> enemyPriority;
    private Setting<Boolean> chatAlert;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> autoOffhand;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> rainbow;
    private Setting<Boolean> antiWeaknessOffhand;
    private Setting<Double> breakYOffset;
    private Setting<Boolean> renderBreakTarget;
    public static Logger logger;
    private long breakSystemTime;
    private long placeSystemTime;
    private long antiStuckSystemTime;
    private static double yaw;
    private static double pitch;
    private static boolean isSpoofingAngles;
    private boolean switchCooldown;
    private static boolean togglePitch;
    private int placements;
    private EntityPlayer playerTarget;
    private EntityPlayer closestTarget;
    private BlockPos breakTarget;
    private BlockPos render;
    private Entity renderEnt;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public TestCA() {
        this.place = this.register(Settings.b("Place", true));
        this.explode = this.register(Settings.b("Explode", true));
        this.autoOffhand = this.register(Settings.b("Auto Offhand Crystal", false));
        this.chatAlert = this.register(Settings.b("Chat Alert", false));
        this.antiSuicide = this.register(Settings.b("Anti Suicide", true));
        this.antiStuck = this.register(Settings.b("Anti Stuck", true));
        this.raytrace = this.register(Settings.b("Raytrace", false));
        this.autoSwitch = this.register(Settings.b("Auto Switch", true));
        this.selfProtect = this.register(Settings.b("Self Protect", false));
        this.rainbow = this.register(Settings.b("Rainbow", false));
        final Setting<Boolean> rgb = this.register(Settings.b("RGB", true));
        this.red = this.register(Settings.integerBuilder("Red").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.green = this.register(Settings.integerBuilder("Green").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.blue = this.register(Settings.integerBuilder("Blue").withValue(255).withMaximum(255).withVisibility(b -> rgb.getValue()).build());
        this.antiWeaknessOffhand = this.register(Settings.b("Anti Weakness Offhand", false));
        this.renderBreakTarget = this.register(Settings.b("Render Break Target", true));
        this.onlyBreakOwnCrystals = this.register(Settings.b("Only Break Own Crystals", false));
        this.msBreakDelay = this.register((Setting<Integer>)Settings.integerBuilder("MS Break Delay").withMinimum(0).withMaximum(300).withValue(10).build());
        this.msPlaceDelay = this.register((Setting<Integer>)Settings.integerBuilder("MS Place Delay").withMinimum(0).withMaximum(300).withValue(10).build());
        this.placeRange = this.register((Setting<Double>)Settings.doubleBuilder("Place Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.breakRange = this.register((Setting<Double>)Settings.doubleBuilder("Break Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.breakThroughWallsRange = this.register((Setting<Double>)Settings.doubleBuilder("Through Walls Break Range").withMinimum(0.0).withMaximum(8.0).withValue(4.5).build());
        this.enemyRange = this.register((Setting<Integer>)Settings.integerBuilder("Enemy Range").withMinimum(0).withMaximum(36).withValue(10).build());
        this.minDamage = this.register((Setting<Integer>)Settings.integerBuilder("Min Damage").withMinimum(0).withMaximum(36).withValue(4).build());
        this.ignoreMinDamageThreshold = this.register((Setting<Integer>)Settings.integerBuilder("Ignore Min Damage").withMinimum(0).withMaximum(36).withValue(8).build());
        this.selfProtectThreshold = this.register((Setting<Integer>)Settings.integerBuilder("Max Self Damage").withMinimum(0).withMaximum(16).withValue(8).build());
        this.breakYOffset = this.register((Setting<Double>)Settings.doubleBuilder("Break Y Offset").withMinimum(0.0).withMaximum(0.5).withValue(0.0).build());
        this.breakSystemTime = -1L;
        final Packet[] packet = { null };
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            packet[0] = event.getPacket();
            if (packet[0] instanceof CPacketPlayer && TestCA.isSpoofingAngles) {
                ((CPacketPlayer)packet[0]).yaw = (float)TestCA.yaw;
                ((CPacketPlayer)packet[0]).pitch = (float)TestCA.pitch;
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        if (TestCA.mc.world == null) {
            return;
        }
        if (this.autoOffhand.getValue()) {
            ModuleManager.getModuleByName("AutoOffhandCrystal").enable();
        }
        if (this.chatAlert.getValue()) {
            Command.sendChatMessage("§aMegyn AutoCrystal ON");
        }
    }
    
    public void onDisable() {
        if (this.autoOffhand.getValue()) {
            ModuleManager.getModuleByName("AutoOffhandCrystal").disable();
        }
        if (this.chatAlert.getValue()) {
            Command.sendChatMessage("§aMegyn AutoCrystal" + ChatFormatting.RED.toString() + " OFF");
        }
        resetRotation();
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)TestCA.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> TestCA.mc.player.getDistance(c))).orElse(null);
        if (crystal != null && this.explode.getValue()) {
            final BlockPos breakTarget = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
            if (!canBlockBeSeen(breakTarget)) {
                if (TestCA.mc.player.getDistance((Entity)crystal) <= this.breakThroughWallsRange.getValue()) {
                    if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                            TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                            TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                    else if (calculateDamage(crystal, (Entity)TestCA.mc.player) <= this.selfProtectThreshold.getValue() && System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                        this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                        TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                        TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.breakSystemTime = System.nanoTime() / 1000000L;
                        KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                    }
                }
            }
            else if (TestCA.mc.player.getDistance((Entity)crystal) <= this.breakRange.getValue()) {
                if (this.selfProtect.getValue() && calculateDamage(crystal, (Entity)TestCA.mc.player) <= this.selfProtectThreshold.getValue()) {
                    if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                        this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                        TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                        TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.breakSystemTime = System.nanoTime() / 1000000L;
                        KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                    }
                }
                else if (!this.selfProtect.getValue() && System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                    this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                    TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                    TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.breakSystemTime = System.nanoTime() / 1000000L;
                    KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                }
            }
        }
        else if (crystal == null) {
            resetRotation();
        }
        int crystalSlot = (TestCA.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? TestCA.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (TestCA.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (TestCA.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
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
        entities.addAll((Collection<? extends Entity>)TestCA.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != TestCA.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    continue;
                }
                if (TestCA.mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    if (!canBlockBeSeen(blockPos) && TestCA.mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
                        continue;
                    }
                    final double b = entity2.getDistanceSq(blockPos);
                    if (b > 56.2) {
                        continue;
                    }
                    final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                    if (d < this.minDamage.getValue() && ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > this.ignoreMinDamageThreshold.getValue()) {
                        continue;
                    }
                    if (d <= damage) {
                        continue;
                    }
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)TestCA.mc.player);
                    if (this.antiSuicide.getValue()) {
                        if (TestCA.mc.player.getHealth() + TestCA.mc.player.getAbsorptionAmount() - self <= 7.0) {
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
            if (!offhand && TestCA.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    TestCA.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5, (EntityPlayer)TestCA.mc.player);
            final RayTraceResult result = TestCA.mc.world.rayTraceBlocks(new Vec3d(TestCA.mc.player.posX, TestCA.mc.player.posY + TestCA.mc.player.getEyeHeight(), TestCA.mc.player.posZ), new Vec3d(finalPos.x + 0.5, finalPos.y - 0.5, finalPos.z + 0.5));
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
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.msPlaceDelay.getValue()) {
                TestCA.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(finalPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
                KamiMod.log.info("Crystal Placed!");
            }
        }
        if (TestCA.isSpoofingAngles) {
            if (TestCA.togglePitch) {
                final EntityPlayerSP player;
                final EntityPlayerSP player = player = TestCA.mc.player;
                player.rotationPitch += 4.0E-4f;
                TestCA.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2;
                final EntityPlayerSP player2 = player2 = TestCA.mc.player;
                player2.rotationPitch -= 4.0E-4f;
                TestCA.togglePitch = true;
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
                KamiTessellator.drawBox(this.render, (int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), 77, 63);
                KamiTessellator.release();
                KamiTessellator.prepare(7);
                KamiTessellator.drawBoundingBoxBlockPos(this.render, 1.0f, (int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), 244);
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
        return (TestCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || TestCA.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && TestCA.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && TestCA.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && TestCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && TestCA.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(TestCA.mc.player.posX), Math.floor(TestCA.mc.player.posY), Math.floor(TestCA.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList positions = NonNullList.create();
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
    
    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)TestCA.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
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
        final int diff = TestCA.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    public static boolean canBlockBeSeen(final BlockPos blockPos) {
        return TestCA.mc.world.rayTraceBlocks(new Vec3d(TestCA.mc.player.posX, TestCA.mc.player.posY + TestCA.mc.player.getEyeHeight(), TestCA.mc.player.posZ), new Vec3d((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), false, true, false) == null;
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        TestCA.yaw = yaw1;
        TestCA.pitch = pitch1;
        TestCA.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (TestCA.isSpoofingAngles) {
            TestCA.yaw = TestCA.mc.player.rotationYaw;
            TestCA.pitch = TestCA.mc.player.rotationPitch;
            TestCA.isSpoofingAngles = false;
        }
    }
    
    public boolean isObbyOrBedrock(final BlockPos blockPos) {
        return TestCA.mc.world.getBlockState(blockPos) == Blocks.OBSIDIAN || TestCA.mc.world.getBlockState(blockPos) == Blocks.BEDROCK;
    }
    
    public boolean getObbyAndBedrockInRange() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), this.placeRange.getValue().floatValue() + 2.0f, this.placeRange.getValue().intValue() + 2, false, true, 0).stream().filter((Predicate<? super Object>)this::isObbyOrBedrock).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        if (!positions.isEmpty()) {
            KamiMod.log.info("Obisidan or Bedrock Found!");
            return true;
        }
        return false;
    }
    
    public void breakCrystal(final EntityEnderCrystal crystal) {
        if (crystal != null && this.explode.getValue()) {
            final BlockPos breakTarget = new BlockPos(crystal.posX, crystal.posY + 1.0, crystal.posZ);
            if (!canBlockBeSeen(breakTarget)) {
                if (TestCA.mc.player.getDistance((Entity)crystal) <= this.breakThroughWallsRange.getValue()) {
                    if (!this.selfProtect.getValue()) {
                        if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                            this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                            TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                            TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.breakSystemTime = System.nanoTime() / 1000000L;
                            KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                        }
                    }
                    else if (calculateDamage(crystal, (Entity)TestCA.mc.player) <= this.selfProtectThreshold.getValue() && System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                        this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                        TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                        TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.breakSystemTime = System.nanoTime() / 1000000L;
                        KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                    }
                }
            }
            else if (TestCA.mc.player.getDistance((Entity)crystal) <= this.breakRange.getValue()) {
                if (this.selfProtect.getValue() && calculateDamage(crystal, (Entity)TestCA.mc.player) <= this.selfProtectThreshold.getValue()) {
                    if (System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                        this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                        TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                        TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                        this.breakSystemTime = System.nanoTime() / 1000000L;
                        KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                    }
                }
                else if (!this.selfProtect.getValue() && System.nanoTime() / 1000000L - this.breakSystemTime >= this.msBreakDelay.getValue()) {
                    this.lookAtPacket(crystal.posX, crystal.posY + this.breakYOffset.getValue(), crystal.posZ, (EntityPlayer)TestCA.mc.player);
                    TestCA.mc.playerController.attackEntity((EntityPlayer)TestCA.mc.player, (Entity)crystal);
                    TestCA.mc.player.swingArm(EnumHand.MAIN_HAND);
                    this.breakSystemTime = System.nanoTime() / 1000000L;
                    KamiMod.log.info("Crystal Broken at " + crystal.posX + ", " + crystal.posY + ", " + crystal.posZ + "!");
                }
            }
        }
        else if (crystal == null) {
            resetRotation();
        }
    }
    
    public void placeCrystal(final BlockPos blockPos) {
        int crystalSlot = (TestCA.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? TestCA.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (TestCA.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (TestCA.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        if (this.place.getValue()) {
            if (!offhand && TestCA.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    TestCA.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCooldown = true;
                }
                return;
            }
            this.lookAtPacket(blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5, (EntityPlayer)TestCA.mc.player);
            final RayTraceResult result = TestCA.mc.world.rayTraceBlocks(new Vec3d(TestCA.mc.player.posX, TestCA.mc.player.posY + TestCA.mc.player.getEyeHeight(), TestCA.mc.player.posZ), new Vec3d(blockPos.x + 0.5, blockPos.y - 0.5, blockPos.z + 0.5));
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
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.msPlaceDelay.getValue()) {
                TestCA.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(blockPos, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.placements;
                this.antiStuckSystemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
                KamiMod.log.info("Crystal Placed!");
            }
        }
        if (TestCA.isSpoofingAngles) {
            if (TestCA.togglePitch) {
                final EntityPlayerSP player;
                final EntityPlayerSP player = player = TestCA.mc.player;
                player.rotationPitch += 4.0E-4f;
                TestCA.togglePitch = false;
            }
            else {
                final EntityPlayerSP player2;
                final EntityPlayerSP player2 = player2 = TestCA.mc.player;
                player2.rotationPitch -= 4.0E-4f;
                TestCA.togglePitch = true;
            }
        }
    }
    
    public BlockPos getPlaceTarget(final List<Entity> entities, final List<BlockPos> blocks) {
        Entity ent = null;
        BlockPos finalPos = null;
        double damage = 0.5;
        for (final Entity entity2 : entities) {
            if (entity2 != TestCA.mc.player) {
                if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                    KamiMod.log.info("Target Alive!");
                }
                else if (TestCA.mc.player.getDistanceSq(entity2) > this.enemyRange.getValue() * this.enemyRange.getValue()) {
                    KamiMod.log.info("Target In Range!");
                }
                else {
                    for (final BlockPos blockPos : blocks) {
                        if (!canBlockBeSeen(blockPos) && TestCA.mc.player.getDistanceSq(blockPos) > 25.0 && this.raytrace.getValue()) {
                            KamiMod.log.info("Raytrace Successful!");
                        }
                        else {
                            final double b = entity2.getDistanceSq(blockPos);
                            if (b > 56.2) {
                                KamiMod.log.info("Step 4 Complete!");
                            }
                            else {
                                final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                                if (d < this.minDamage.getValue() && ((EntityLivingBase)entity2).getHealth() > this.ignoreMinDamageThreshold.getValue()) {
                                    KamiMod.log.info("Crystal Will Deal More Than Min Damage!");
                                }
                                else if (d <= damage) {
                                    KamiMod.log.info("Crystal Will Deal More Than 0.5 Damage!");
                                }
                                else {
                                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)TestCA.mc.player);
                                    if (this.antiSuicide.getValue()) {
                                        if (TestCA.mc.player.getHealth() - self <= 7.0) {
                                            KamiMod.log.info("Crystal Will Not Kill The Player!");
                                            continue;
                                        }
                                        if (self > d) {
                                            KamiMod.log.info("Self Damage Exceeds 0.5!");
                                            continue;
                                        }
                                    }
                                    damage = d;
                                    finalPos = blockPos;
                                    ent = entity2;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (finalPos == null) {
            KamiMod.log.info("Place Target null! Will not place");
            return finalPos;
        }
        KamiMod.log.info("Place Target Gotten!");
        return finalPos;
    }
    
    private void findClosestTarget() {
        final List<EntityPlayer> playerList = (List<EntityPlayer>)TestCA.mc.world.playerEntities;
        this.closestTarget = null;
        for (final EntityPlayer target : playerList) {
            if (target == TestCA.mc.player) {
                continue;
            }
            if (Friends.isFriend(target.getName())) {
                continue;
            }
            if (!EntityUtil.isLiving((Entity)target)) {
                continue;
            }
            if (target.getHealth() <= 0.0f) {
                continue;
            }
            if (this.closestTarget == null) {
                this.closestTarget = target;
            }
            else {
                if (TestCA.mc.player.getDistance((Entity)target) >= TestCA.mc.player.getDistance((Entity)this.closestTarget)) {
                    continue;
                }
                this.closestTarget = target;
            }
        }
    }
    
    private enum delayMode
    {
        TICKS, 
        MILLISECONDS;
    }
}
