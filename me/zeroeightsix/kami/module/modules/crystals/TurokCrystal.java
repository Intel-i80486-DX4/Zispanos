//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.crystals;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;
import java.util.function.*;
import com.mojang.realmsclient.gui.*;
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
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;
import net.minecraft.potion.*;
import net.minecraft.network.play.client.*;

@Info(name = "AutoCrystalTurok", category = Category.CRYSTALS)
public class TurokCrystal extends Module
{
    private Setting<Integer> dano_minimo;
    private Setting<Integer> cor_red;
    private Setting<Integer> cor_green;
    private Setting<Integer> cor_blue;
    private Setting<Integer> cor_alfa;
    private Setting<Boolean> auto_switch;
    private Setting<Boolean> colocar;
    private Setting<Boolean> raytrace;
    private Setting<Boolean> explodir;
    private Setting<Boolean> varios;
    private Setting<Double> distancia;
    private Setting<Double> alcance;
    private Setting<Boolean> ant_fraqueza;
    private Setting<Double> tempo_de_colocar;
    private Setting<Double> tempo_de_ataque;
    private Setting<Boolean> prefixo_chat;
    private static boolean is_spoofing_angles;
    private static boolean toggle_pitch;
    private static double player_yaw;
    private static double player_pitch;
    private Entity render_ent;
    private BlockPos render;
    private boolean switch_cooldown;
    private boolean attack;
    private int old_slot;
    private int new_slot;
    private int places;
    private long system_time;
    @EventHandler
    private Listener<PacketEvent.Send> packetListener;
    
    public TurokCrystal() {
        this.dano_minimo = this.register(Settings.integerBuilder("Min Dmg").withMinimum(0).withMaximum(16).withValue(2));
        this.cor_red = this.register(Settings.integerBuilder("Red").withMinimum(0).withMaximum(255).withValue(0));
        this.cor_green = this.register(Settings.integerBuilder("Green").withMinimum(0).withMaximum(255).withValue(0));
        this.cor_blue = this.register(Settings.integerBuilder("Blue").withMinimum(0).withMaximum(255).withValue(190));
        this.cor_alfa = this.register(Settings.integerBuilder("Alpha").withMinimum(0).withMaximum(255).withValue(70));
        this.auto_switch = this.register(Settings.b("Auto Switch"));
        this.colocar = this.register(Settings.b("Place", true));
        this.raytrace = this.register(Settings.b("RayTrace", false));
        this.explodir = this.register(Settings.b("Explode", true));
        this.varios = this.register(Settings.b("Multiple Places", true));
        this.distancia = this.register(Settings.d("Place Range", 6.0));
        this.alcance = this.register(Settings.d("Range", 6.0));
        this.ant_fraqueza = this.register(Settings.b("Anti Weakness", false));
        this.tempo_de_colocar = this.register(Settings.d("Place Delay", 0.0));
        this.tempo_de_ataque = this.register(Settings.d("Hit Delay", 0.0));
        this.prefixo_chat = this.register(Settings.b("Chat", true));
        this.switch_cooldown = false;
        this.attack = false;
        this.old_slot = -1;
        this.system_time = -1L;
        this.packetListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            final Packet packet = event.getPacket();
            if (packet instanceof CPacketPlayer && TurokCrystal.is_spoofing_angles) {
                ((CPacketPlayer)packet).yaw = (float)TurokCrystal.player_yaw;
                ((CPacketPlayer)packet).pitch = (float)TurokCrystal.player_pitch;
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        if (this.prefixo_chat.getValue()) {
            Command.sendChatMessage("TurokCrystal <- " + ChatFormatting.GREEN + "Enabled!");
        }
    }
    
    public void onDisable() {
        this.render = null;
        this.render_ent = null;
        reset_rotation();
        if (this.prefixo_chat.getValue()) {
            Command.sendChatMessage("TurokCrystal -> " + ChatFormatting.RED + "Disabled!");
        }
    }
    
    @Override
    public void onUpdate() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal)TurokCrystal.mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> entity).min(Comparator.comparing(d -> TurokCrystal.mc.player.getDistance(d))).orElse(null);
        if (this.explodir.getValue() && crystal != null && TurokCrystal.mc.player.getDistance((Entity)crystal) <= this.alcance.getValue()) {
            if (System.nanoTime() / 1000000L - this.system_time >= this.tempo_de_ataque.getValue()) {
                if (this.ant_fraqueza.getValue() && TurokCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    if (!this.attack) {
                        this.old_slot = Wrapper.getPlayer().inventory.currentItem;
                        this.attack = true;
                    }
                    this.new_slot = -1;
                    for (int i = 0; i < 9; ++i) {
                        final ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack != ItemStack.EMPTY) {
                            if (stack.getItem() instanceof ItemSword) {
                                this.new_slot = i;
                                break;
                            }
                            if (stack.getItem() instanceof ItemTool) {
                                this.new_slot = i;
                                break;
                            }
                        }
                    }
                    if (this.new_slot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = this.new_slot;
                        this.switch_cooldown = true;
                    }
                }
                this.lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)TurokCrystal.mc.player);
                TurokCrystal.mc.playerController.attackEntity((EntityPlayer)TurokCrystal.mc.player, (Entity)crystal);
                TurokCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.system_time = System.nanoTime() / 1000000L;
            }
            if (!this.varios.getValue()) {
                return;
            }
            if (this.places == 3) {
                this.places = 0;
                return;
            }
        }
        else {
            reset_rotation();
            if (this.old_slot != -1) {
                Wrapper.getPlayer().inventory.currentItem = this.old_slot;
                this.old_slot = -1;
            }
            this.attack = false;
        }
        int crystal_slot = (TurokCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? TurokCrystal.mc.player.inventory.currentItem : -1;
        if (crystal_slot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (TurokCrystal.mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                    crystal_slot = l;
                    break;
                }
            }
        }
        boolean off_hand = false;
        if (TurokCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            off_hand = true;
        }
        else if (crystal_slot == -1) {
            return;
        }
        final List<BlockPos> blocks = this.find_crystal_blocks();
        final List<Entity> entities = new ArrayList<Entity>();
        entities.addAll((Collection<? extends Entity>)TurokCrystal.mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        BlockPos q = null;
        double damage = 0.5;
        for (final Entity entity_two : entities) {
            if (entity_two != TurokCrystal.mc.player) {
                if (((EntityLivingBase)entity_two).getHealth() <= 0.0f) {
                    continue;
                }
                for (final BlockPos blockPos : blocks) {
                    final double b = entity_two.getDistanceSq(blockPos);
                    if (b >= this.distancia.getValue() * this.distancia.getValue()) {
                        continue;
                    }
                    final double d2 = calculate_damage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, entity_two);
                    if (d2 <= damage) {
                        continue;
                    }
                    final double self = calculate_damage(blockPos.x + 0.5, blockPos.y + 1, blockPos.z + 0.5, (Entity)TurokCrystal.mc.player);
                    if (self > d2 && d2 >= ((EntityLivingBase)entity_two).getHealth()) {
                        continue;
                    }
                    if (self - 0.5 > TurokCrystal.mc.player.getHealth()) {
                        continue;
                    }
                    if (d2 < this.dano_minimo.getValue()) {
                        continue;
                    }
                    damage = d2;
                    q = blockPos;
                    this.render_ent = entity_two;
                }
            }
        }
        if (damage == 0.5) {
            this.render = null;
            this.render_ent = null;
            reset_rotation();
            return;
        }
        this.render = q;
        if (this.colocar.getValue()) {
            if (!off_hand && TurokCrystal.mc.player.inventory.currentItem != crystal_slot) {
                if (this.auto_switch.getValue()) {
                    TurokCrystal.mc.player.inventory.currentItem = crystal_slot;
                    reset_rotation();
                    this.switch_cooldown = true;
                }
                return;
            }
            this.lookAtPacket(q.x + 0.5, q.y - 0.5, q.z + 0.5, (EntityPlayer)TurokCrystal.mc.player);
            EnumFacing f;
            if (this.raytrace.getValue()) {
                final RayTraceResult result = TurokCrystal.mc.world.rayTraceBlocks(new Vec3d(TurokCrystal.mc.player.posX, TurokCrystal.mc.player.posY + TurokCrystal.mc.player.getEyeHeight(), TurokCrystal.mc.player.posZ), new Vec3d(q.x + 0.5, q.y - 0.5, q.z + 0.5));
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
            if (this.switch_cooldown) {
                this.switch_cooldown = false;
                return;
            }
            if (System.nanoTime() / 1000000L - this.system_time >= this.tempo_de_colocar.getValue()) {
                TurokCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, f, off_hand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                ++this.places;
                this.system_time = System.nanoTime() / 1000000L;
            }
        }
        if (TurokCrystal.is_spoofing_angles) {
            if (TurokCrystal.toggle_pitch) {
                final EntityPlayerSP player;
                final EntityPlayerSP player = player = TurokCrystal.mc.player;
                player.rotationPitch += 4.0E-4f;
                TurokCrystal.toggle_pitch = false;
            }
            else {
                final EntityPlayerSP player2;
                final EntityPlayerSP player = player2 = TurokCrystal.mc.player;
                player2.rotationPitch -= 4.0E-4f;
                TurokCrystal.toggle_pitch = true;
            }
        }
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (this.render != null) {
            KamiTessellator.prepare(7);
            KamiTessellator.drawBox(this.render, (int)this.cor_red.getValue(), (int)this.cor_green.getValue(), (int)this.cor_blue.getValue(), (int)this.cor_alfa.getValue(), 63);
            KamiTessellator.release();
        }
    }
    
    private void lookAtPacket(final double px, final double py, final double pz, final EntityPlayer me) {
        final double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        set_yaw_pitch((float)v[0], (float)v[1]);
    }
    
    private boolean can_place_crystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.add(0, 1, 0);
        final BlockPos boost2 = blockPos.add(0, 2, 0);
        return (TurokCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || TurokCrystal.mc.world.getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && TurokCrystal.mc.world.getBlockState(boost).getBlock() == Blocks.AIR && TurokCrystal.mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && TurokCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && TurokCrystal.mc.world.getEntitiesWithinAABB((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static BlockPos get_player_pos() {
        return new BlockPos(Math.floor(TurokCrystal.mc.player.posX), Math.floor(TurokCrystal.mc.player.posY), Math.floor(TurokCrystal.mc.player.posZ));
    }
    
    private List<BlockPos> find_crystal_blocks() {
        final NonNullList<BlockPos> positions = (NonNullList<BlockPos>)NonNullList.create();
        positions.addAll((Collection)this.getSphere(get_player_pos(), this.alcance.getValue().floatValue(), this.alcance.getValue().intValue(), false, true, 0).stream().filter((Predicate<? super Object>)this::can_place_crystal).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
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
    
    public static float calculate_damage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.getDistance(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        final double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = get_blast_reduction((EntityLivingBase)entity, get_damage_multiplied(damage), new Explosion((World)TurokCrystal.mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    public static float get_blast_reduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
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
    
    private static void reset_rotation() {
        if (TurokCrystal.is_spoofing_angles) {
            TurokCrystal.player_yaw = TurokCrystal.mc.player.rotationYaw;
            TurokCrystal.player_pitch = TurokCrystal.mc.player.rotationPitch;
            TurokCrystal.is_spoofing_angles = false;
        }
    }
    
    private static void set_yaw_pitch(final float player_yaw_, final float player_pitch_) {
        TurokCrystal.player_yaw = player_yaw_;
        TurokCrystal.player_pitch = player_pitch_;
        TurokCrystal.is_spoofing_angles = true;
    }
    
    public static float calculate_damage(final EntityEnderCrystal crystal, final Entity entity) {
        return calculate_damage(crystal.posX, crystal.posY, crystal.posZ, entity);
    }
    
    private static float get_damage_multiplied(final float damage) {
        final int diff = TurokCrystal.mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
}
