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
import net.minecraft.init.*;
import java.util.stream.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.module.modules.render.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import java.util.*;
import me.zeroeightsix.kami.module.modules.gui.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalDVDGod", category = Category.CRYSTALS, description = "Places End Crystals to kill enemies.")
public class AutoCrystalDVDGod extends Module
{
    private Setting<Boolean> defaultSetting;
    private Setting<Page> p;
    private Setting<ExplodeBehavior> explodeBehavior;
    private Setting<PlaceBehavior> placeBehavior;
    private Setting<Boolean> autoSwitch;
    private Setting<Boolean> place;
    private Setting<Boolean> explode;
    private Setting<Boolean> noToolExplode;
    private Setting<Boolean> antiWeakness;
    private Setting<Boolean> checkAbsorption;
    public Setting<Double> range;
    private Setting<Double> delay;
    private Setting<Double> minDmg;
    public Setting<Double> maxSelfDmg;
    public Setting<Double> facePlaceHealth;
    private Setting<Boolean> placePriority;
    private Setting<Boolean> facePlace;
    private Setting<Boolean> players;
    private Setting<Boolean> mobs;
    private Setting<Boolean> animals;
    private Setting<Boolean> tracer;
    private Setting<Boolean> customColours;
    private Setting<Integer> aBlock;
    private Setting<Integer> aTracer;
    private Setting<Integer> r;
    private Setting<Integer> g;
    private Setting<Integer> b;
    private Setting<Boolean> statusMessages;
    private BlockPos render;
    private Entity renderEnt;
    private long systemTime;
    private static boolean togglePitch;
    private boolean switchCoolDown;
    private boolean isAttacking;
    private int oldSlot;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @EventHandler
    private Listener<PacketEvent.Send> cPacketListener;
    
    public AutoCrystalDVDGod() {
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.p = this.register((Setting<Page>)Settings.enumBuilder(Page.class).withName("Page").withValue(Page.ONE).build());
        this.explodeBehavior = this.register((Setting<ExplodeBehavior>)Settings.enumBuilder(ExplodeBehavior.class).withName("Explode Behavior").withValue(ExplodeBehavior.ALWAYS).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.placeBehavior = this.register((Setting<PlaceBehavior>)Settings.enumBuilder(PlaceBehavior.class).withName("Place Behavior").withValue(PlaceBehavior.SINGLE).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.autoSwitch = this.register(Settings.booleanBuilder("Auto Switch").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.place = this.register(Settings.booleanBuilder("Place").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.explode = this.register(Settings.booleanBuilder("Explode").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.noToolExplode = this.register(Settings.booleanBuilder("No Tool Explode").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.antiWeakness = this.register(Settings.booleanBuilder("Anti Weakness").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.checkAbsorption = this.register(Settings.booleanBuilder("Check Absorption").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.range = this.register(Settings.doubleBuilder("Range").withMinimum(1.0).withValue(4.0).withMaximum(10.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.delay = this.register(Settings.doubleBuilder("Hit Delay").withMinimum(0.0).withValue(5.0).withMaximum(10.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.minDmg = this.register(Settings.doubleBuilder("Minimum Damage").withMinimum(0.0).withValue(0.0).withMaximum(32.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.maxSelfDmg = this.register(Settings.doubleBuilder("Max Self Damage").withMinimum(1.0).withValue(10.0).withMaximum(36.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.facePlaceHealth = this.register(Settings.doubleBuilder("Face Place Health").withMinimum(1.0).withValue(10.0).withMaximum(36.0).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.placePriority = this.register(Settings.booleanBuilder("Prioritize Manual").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.facePlace = this.register(Settings.booleanBuilder("Face Place").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.players = this.register(Settings.booleanBuilder("Players").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.mobs = this.register(Settings.booleanBuilder("Mobs").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.animals = this.register(Settings.booleanBuilder("Animals").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.tracer = this.register(Settings.booleanBuilder("Tracer").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.customColours = this.register(Settings.booleanBuilder("Custom Colours").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.aBlock = this.register(Settings.integerBuilder("Block Transparency").withMinimum(0).withValue(44).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.aTracer = this.register(Settings.integerBuilder("Tracer Transparency").withMinimum(0).withValue(200).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.r = this.register(Settings.integerBuilder("Red").withMinimum(0).withValue(155).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.g = this.register(Settings.integerBuilder("Green").withMinimum(0).withValue(144).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.b = this.register(Settings.integerBuilder("Blue").withMinimum(0).withValue(255).withMaximum(255).withVisibility(v -> this.p.getValue().equals(Page.TWO) && this.customColours.getValue()).build());
        this.statusMessages = this.register(Settings.booleanBuilder("Status Messages").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.systemTime = -1L;
        this.switchCoolDown = false;
        this.isAttacking = false;
        this.oldSlot = -1;
        this.cPacketListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && AutoCrystalDVDGod.isSpoofingAngles) {
                ((CPacketPlayer)packet).yaw = (float)AutoCrystalDVDGod.yaw;
                ((CPacketPlayer)packet).pitch = (float)AutoCrystalDVDGod.pitch;
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.explodeBehavior.setValue(ExplodeBehavior.ALWAYS);
            this.placeBehavior.setValue(PlaceBehavior.SINGLE);
            this.autoSwitch.setValue(true);
            this.place.setValue(false);
            this.explode.setValue(false);
            this.noToolExplode.setValue(false);
            this.antiWeakness.setValue(false);
            this.checkAbsorption.setValue(true);
            this.range.setValue(4.0);
            this.delay.setValue(5.0);
            this.minDmg.setValue(0.0);
            this.placePriority.setValue(false);
            this.facePlace.setValue(false);
            this.players.setValue(true);
            this.mobs.setValue(false);
            this.animals.setValue(false);
            this.tracer.setValue(true);
            this.customColours.setValue(true);
            this.aBlock.setValue(44);
            this.aTracer.setValue(200);
            this.r.setValue(155);
            this.g.setValue(144);
            this.b.setValue(255);
            this.statusMessages.setValue(false);
            this.defaultSetting.setValue(false);
            Command.sendChatMessage(this.getChatName() + " Set to defaults!");
            Command.sendChatMessage(this.getChatName() + " Close and reopen the " + this.getName() + " settings menu to see changes");
        }
        int holeBlocks = 0;
        final Vec3d[] holeOffset = { AutoCrystalDVDGod.mc.player.getPositionVector().add(1.0, 0.0, 0.0), AutoCrystalDVDGod.mc.player.getPositionVector().add(-1.0, 0.0, 0.0), AutoCrystalDVDGod.mc.player.getPositionVector().add(0.0, 0.0, 1.0), AutoCrystalDVDGod.mc.player.getPositionVector().add(0.0, 0.0, -1.0), AutoCrystalDVDGod.mc.player.getPositionVector().add(0.0, -1.0, 0.0) };
        final EntityEnderCrystal crystal = (EntityEnderCrystal)AutoCrystalDVDGod.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(c -> AutoCrystalDVDGod.mc.player.getDistance(c))).orElse(null);
        if (this.explode.getValue() && crystal != null && AutoCrystalDVDGod.mc.player.getDistance((Entity)crystal) <= this.range.getValue() && this.passSwordCheck()) {
            if (System.nanoTime() / 1000000.0f - this.systemTime >= 25.0 * this.delay.getValue()) {
                if (this.antiWeakness.getValue() && AutoCrystalDVDGod.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.isAttacking) {
                        this.oldSlot = Wrapper.getPlayer().inventory.currentItem;
                        this.isAttacking = true;
                    }
                    int newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                newSlot = i;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                newSlot = i;
                                break;
                            }
                        }
                    }
                    if (newSlot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = newSlot;
                        this.switchCoolDown = true;
                    }
                }
                if (this.placePriority.getValue()) {
                    final boolean wasPlacing = this.place.getValue();
                    if (AutoCrystalDVDGod.mc.gameSettings.keyBindUseItem.isKeyDown() && this.place.getValue()) {
                        this.place.setValue(false);
                        this.explode(crystal);
                    }
                    if (!this.place.getValue() && wasPlacing) {
                        this.place.setValue(true);
                    }
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.ALWAYS) {
                    this.explode(crystal);
                }
                for (final Vec3d vecOffset : holeOffset) {
                    final BlockPos offset = new BlockPos(vecOffset.x, vecOffset.y, vecOffset.z);
                    if (AutoCrystalDVDGod.mc.world.getBlockState(offset).getBlock() == Blocks.OBSIDIAN || AutoCrystalDVDGod.mc.world.getBlockState(offset).getBlock() == Blocks.BEDROCK) {
                        ++holeBlocks;
                    }
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.HOLE_ONLY && holeBlocks == 5) {
                    this.explode(crystal);
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.PREVENT_SUICIDE && ((AutoCrystalDVDGod.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) <= 0.5 && AutoCrystalDVDGod.mc.player.getPosition().getY() == crystal.getPosition().getY()) || (AutoCrystalDVDGod.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) >= 2.3 && AutoCrystalDVDGod.mc.player.getPosition().getY() == crystal.getPosition().getY()) || (AutoCrystalDVDGod.mc.player.getPositionVector().distanceTo(crystal.getPositionVector()) >= 0.5 && AutoCrystalDVDGod.mc.player.getPosition().getY() != crystal.getPosition().getY()))) {
                    this.explode(crystal);
                }
                if (this.explodeBehavior.getValue() == ExplodeBehavior.LEFT_CLICK_ONLY && AutoCrystalDVDGod.mc.gameSettings.keyBindAttack.isKeyDown()) {
                    this.explode(crystal);
                }
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
        int crystalSlot = (AutoCrystalDVDGod.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? AutoCrystalDVDGod.mc.player.inventory.currentItem : -1;
        if (crystalSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (AutoCrystalDVDGod.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystalSlot = l;
                    break;
                }
            }
        }
        boolean offhand = false;
        if (AutoCrystalDVDGod.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            offhand = true;
        }
        else if (crystalSlot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.findCrystalBlocks();
        final List<Entity> entities = new ArrayList<Entity>();
        if (this.players.getValue()) {
            entities.addAll((Collection<? extends Entity>)AutoCrystalDVDGod.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        }
        final boolean b2;
        entities.addAll((Collection<? extends Entity>)AutoCrystalDVDGod.mc.world.loadedEntityList.stream().filter(entity -> {
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
            if (entity2 != AutoCrystalDVDGod.mc.player && ((EntityLivingBase)entity2).getHealth() > 0.0f) {
                if (this.placeBehavior.getValue() == PlaceBehavior.MULTI) {
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
                    final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AutoCrystalDVDGod.mc.player);
                    float enemyAbsorption = 0.0f;
                    float playerAbsorption = 0.0f;
                    if (this.checkAbsorption.getValue()) {
                        enemyAbsorption = ((EntityLivingBase)entity2).getAbsorptionAmount();
                        playerAbsorption = AutoCrystalDVDGod.mc.player.getAbsorptionAmount();
                    }
                    if (self > d && d >= ((EntityLivingBase)entity2).getHealth() + enemyAbsorption) {
                        continue;
                    }
                    if (self - 0.5 > AutoCrystalDVDGod.mc.player.getHealth() + playerAbsorption) {
                        continue;
                    }
                    damage = d;
                    q = blockPos;
                    this.renderEnt = entity2;
                }
            }
        }
        if (this.place.getValue() && this.placeBehavior.getValue() == PlaceBehavior.MULTI) {
            for (final Entity entity2 : entities) {
                if (entity2 != AutoCrystalDVDGod.mc.player) {
                    if (((EntityLivingBase)entity2).getHealth() <= 0.0f) {
                        continue;
                    }
                    for (final BlockPos blockPos : blocks) {
                        final double b = entity2.getDistanceSq(blockPos);
                        if (b > 75.0) {
                            continue;
                        }
                        final double d = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity2);
                        final double self = calculateDamage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)AutoCrystalDVDGod.mc.player);
                        if (self >= AutoCrystalDVDGod.mc.player.getHealth() + AutoCrystalDVDGod.mc.player.getAbsorptionAmount() || self > d || self > this.maxSelfDmg.getValue() || ((b >= 10.0 || d < 15.0) && d < ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() && (4.0f < ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() || d < 4.0) && (b >= 2.0 || blockPos.y != entity2.getPosition().y || this.facePlace.getValue() || ((EntityLivingBase)entity2).getHealth() + ((EntityLivingBase)entity2).getAbsorptionAmount() > this.facePlaceHealth.getValue()) && (b >= 9.0 || d < this.minDmg.getValue() || this.minDmg.getValue() <= 0.0))) {
                            continue;
                        }
                        q = blockPos;
                        damage = d;
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
        if (this.place.getValue()) {
            this.render = q;
            if (!offhand && AutoCrystalDVDGod.mc.player.inventory.currentItem != crystalSlot) {
                if (this.autoSwitch.getValue()) {
                    AutoCrystalDVDGod.mc.player.inventory.currentItem = crystalSlot;
                    resetRotation();
                    this.switchCoolDown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)AutoCrystalDVDGod.mc.player);
            final RayTraceResult result = AutoCrystalDVDGod.mc.world.rayTraceBlocks(new Vec3d(AutoCrystalDVDGod.mc.player.posX, AutoCrystalDVDGod.mc.player.posY + AutoCrystalDVDGod.mc.player.getEyeHeight(), AutoCrystalDVDGod.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
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
            AutoCrystalDVDGod.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        }
        if (AutoCrystalDVDGod.isSpoofingAngles) {
            if (AutoCrystalDVDGod.togglePitch) {
                AutoCrystalDVDGod.mc.player.rotationPitch += (float)4.0E-4;
                AutoCrystalDVDGod.togglePitch = false;
            }
            else {
                AutoCrystalDVDGod.mc.player.rotationPitch -= (float)4.0E-4;
                AutoCrystalDVDGod.togglePitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            int colour = 1157627903;
            if (this.customColours.getValue()) {
                colour = ColourConverter.rgbToInt((int)this.r.getValue(), (int)this.g.getValue(), (int)this.b.getValue(), (int)this.aBlock.getValue());
            }
            KamiTessellator.drawBox(this.render, colour, 63);
            KamiTessellator.release();
            if (this.renderEnt != null && this.tracer.getValue()) {
                final Vec3d p = EntityUtil.getInterpolatedRenderPos(this.renderEnt, AutoCrystalDVDGod.mc.getRenderPartialTicks());
                float rL = 1.0f;
                float gL = 1.0f;
                float bL = 1.0f;
                float aL = 1.0f;
                if (this.customColours.getValue()) {
                    rL = ColourConverter.toF((int)this.r.getValue());
                    gL = ColourConverter.toF((int)this.g.getValue());
                    bL = ColourConverter.toF((int)this.b.getValue());
                    aL = ColourConverter.toF((int)this.aTracer.getValue());
                }
                Tracers.drawLineFromPosToPos(this.render.x - AutoCrystalDVDGod.mc.getRenderManager().renderPosX + 0.5, this.render.y - AutoCrystalDVDGod.mc.getRenderManager().renderPosY + 1.0, this.render.z - AutoCrystalDVDGod.mc.getRenderManager().renderPosZ + 0.5, p.x, p.y, p.z, (double)this.renderEnt.getEyeHeight(), rL, gL, bL, aL);
            }
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1] + 1.0f);
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (AutoCrystalDVDGod.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || AutoCrystalDVDGod.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && AutoCrystalDVDGod.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && AutoCrystalDVDGod.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && AutoCrystalDVDGod.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystalDVDGod.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(AutoCrystalDVDGod.mc.player.posX), Math.floor(AutoCrystalDVDGod.mc.player.posY), Math.floor(AutoCrystalDVDGod.mc.player.posZ));
    }
    
    private List<BlockPos> findCrystalBlocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)getSphere(getPlayerPos(), this.range.getValue().floatValue(), this.range.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::canPlaceCrystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)positions;
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
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
        final double distancedSize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedSize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finalD = 1.0;
        if (entity instanceof EntityLivingBase) {
            finalD = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)AutoCrystalDVDGod.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finalD;
    }
    
    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive((Potion)Objects.requireNonNull(Potion.getPotionById(11)))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage - ep.getAbsorptionAmount(), 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }
    
    private static float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystalDVDGod.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static void setYawAndPitch(final float yaw1, final float pitch1) {
        AutoCrystalDVDGod.yaw = yaw1;
        AutoCrystalDVDGod.pitch = pitch1;
        AutoCrystalDVDGod.isSpoofingAngles = true;
    }
    
    private static void resetRotation() {
        if (AutoCrystalDVDGod.isSpoofingAngles) {
            AutoCrystalDVDGod.yaw = AutoCrystalDVDGod.mc.player.rotationYaw;
            AutoCrystalDVDGod.pitch = AutoCrystalDVDGod.mc.player.rotationPitch;
            AutoCrystalDVDGod.isSpoofingAngles = false;
        }
    }
    
    public void onEnable() {
        if (this.statusMessages.getValue()) {
            Command.sendChatMessage(this.getChatName() + "&aEnabled&r");
        }
    }
    
    public void onDisable() {
        if (this.statusMessages.getValue()) {
            Command.sendChatMessage(this.getChatName() + "&cDisabled&r");
        }
        this.render = null;
        this.renderEnt = null;
        resetRotation();
    }
    
    public void explode(final EntityEnderCrystal crystal) {
        this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)AutoCrystalDVDGod.mc.player);
        AutoCrystalDVDGod.mc.playerController.attackEntity((EntityPlayer)AutoCrystalDVDGod.mc.player, (Entity)crystal);
        AutoCrystalDVDGod.mc.player.swingArm(EnumHand.MAIN_HAND);
        this.systemTime = System.nanoTime() / 1000000L;
    }
    
    private boolean passSwordCheck() {
        return !(AutoCrystalDVDGod.mc.player.getHeldItemMainhand().getItem() instanceof ItemTool) || !this.noToolExplode.getValue();
    }
    
    @Override
    public String getHudInfo() {
        return String.valueOf(InfoOverlay.getItems(Items.END_CRYSTAL));
    }
    
    static {
        AutoCrystalDVDGod.togglePitch = false;
    }
    
    private enum ExplodeBehavior
    {
        HOLE_ONLY, 
        PREVENT_SUICIDE, 
        LEFT_CLICK_ONLY, 
        ALWAYS;
    }
    
    private enum PlaceBehavior
    {
        MULTI, 
        SINGLE;
    }
    
    private enum Page
    {
        ONE, 
        TWO;
    }
}
