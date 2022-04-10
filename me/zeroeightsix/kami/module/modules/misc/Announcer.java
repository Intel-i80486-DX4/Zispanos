//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import java.util.function.*;
import net.minecraft.entity.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.network.play.server.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;

@Info(name = "Announcer", category = Category.MISC, description = "How to get /ignored")
public class Announcer extends Module
{
    private Setting<Double> delay;
    private Setting<Boolean> thanks;
    private Setting<Boolean> visualRange;
    private Setting<Double> visualRangeDelay;
    private Setting<Boolean> movement;
    private Setting<Double> movementDelay;
    private Setting<Boolean> totemPops;
    private Setting<Double> totemPopDelay;
    private Setting<Boolean> eating;
    private Setting<Double> eatingDelay;
    private Setting<Boolean> placing;
    private Setting<Double> placingDelay;
    private Setting<Boolean> crystal;
    private Setting<Double> crystalDelay;
    private Setting<Boolean> trap;
    private Setting<Double> trapDelay;
    private Setting<Boolean> exp;
    private Setting<Double> expDelay;
    private long systemTime;
    private long systemTime2;
    private long visualRangeSystemTime;
    private long joinSystemTime;
    private long movementSystemTime;
    private long eatSystemTime;
    private long placeSystemTime;
    private long expSystemTime;
    private long attackSystemTime;
    private long totemPopSystemTime;
    private double distance;
    private int ticks;
    private int ticks2;
    private int eats;
    private int obsidianPlacements;
    private int expThrows;
    public ArrayList<String> names;
    public ArrayList<String> names2;
    public ArrayList<String> removal;
    private static Announcer INSTANCE;
    @EventHandler
    public Listener<PacketEvent.Send> placeListener;
    @EventHandler
    private Listener<PacketEvent.Receive> totemPopListener;
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (!this.visualRange.getValue()) {
            return;
        }
        this.names2.clear();
        Announcer.mc.world.loadedEntityList.stream().filter(EntityUtil::isLiving).filter(entity -> !EntityUtil.isFakeLocalPlayer(entity)).filter(entity -> entity instanceof EntityPlayer).filter(entity -> !(entity instanceof EntityPlayerSP)).forEach(this::testName);
        this.testLeave();
    }
    
    private void testName(final Entity entityIn) {
        this.names2.add(entityIn.getName());
        if (!this.names.contains(entityIn.getName()) && System.nanoTime() / 1000000L - this.visualRangeSystemTime >= this.visualRangeDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime >= this.delay.getValue()) {
            sendChatMessage("spotted " + entityIn.getName());
            this.names.add(entityIn.getName());
            this.systemTime = System.nanoTime() / 1000000L;
            this.visualRangeSystemTime = System.nanoTime() / 1000000L;
        }
    }
    
    private void testLeave() {
        this.names.forEach(name -> {
            if (!this.names2.contains(name)) {
                this.removal.add(name);
            }
            return;
        });
        this.removal.forEach(name -> this.names.remove(name));
        this.removal.clear();
    }
    
    public static boolean crystal() {
        return Announcer.INSTANCE.crystal.getValue();
    }
    
    public static double crystalDelay() {
        return Announcer.INSTANCE.crystalDelay.getValue();
    }
    
    public static boolean trap() {
        return Announcer.INSTANCE.trap.getValue();
    }
    
    public static double trapDelay() {
        return Announcer.INSTANCE.trapDelay.getValue();
    }
    
    @Override
    public void onUpdate() {
        if (this.movement.getValue()) {
            final double x = Announcer.mc.player.posX - Announcer.mc.player.prevPosX;
            final double z = Announcer.mc.player.posZ - Announcer.mc.player.prevPosZ;
            this.distance += Math.sqrt(x * x + z * z);
            if (this.distance / 2.0 > 1.0 && System.nanoTime() / 1000000L - this.movementSystemTime >= this.movementDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime2 >= this.delay.getValue()) {
                sendChatMessage("walked " + Math.round(this.distance / 2.0) + " blocks");
                this.distance = 0.0;
                this.systemTime2 = System.nanoTime() / 1000000L;
                this.movementSystemTime = System.nanoTime() / 1000000L;
            }
        }
        if (this.eating.getValue()) {
            if (Announcer.mc.player.isHandActive() && (Announcer.mc.player.getHeldItemMainhand().getItem() instanceof ItemAppleGold || Announcer.mc.player.getHeldItemOffhand().getItem() instanceof ItemAppleGold)) {
                ++this.ticks;
                if (this.ticks >= 77) {
                    ++this.eats;
                    this.ticks = 0;
                }
            }
            if (this.eats > 0 && System.nanoTime() / 1000000L - this.eatSystemTime >= this.eatingDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime >= this.delay.getValue()) {
                sendChatMessage("ate " + this.eats + ((this.eats == 1) ? " Golden Apple" : " Golden Apples"));
                this.eats = 0;
                this.systemTime = System.nanoTime() / 1000000L;
                this.eatSystemTime = System.nanoTime() / 1000000L;
            }
        }
        if (this.placing.getValue()) {
            ++this.ticks2;
            if (System.nanoTime() / 1000000L - this.placeSystemTime >= this.placingDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime >= this.delay.getValue() && this.obsidianPlacements > 0) {
                sendChatMessage("placed " + this.obsidianPlacements + " Obsidian");
                this.obsidianPlacements = 0;
                this.systemTime = System.nanoTime() / 1000000L;
                this.placeSystemTime = System.nanoTime() / 1000000L;
            }
        }
        if (this.exp.getValue() && System.nanoTime() / 1000000L - this.expSystemTime >= this.expDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime >= this.delay.getValue() && this.expThrows > 0) {
            sendChatMessage("threw " + this.expThrows + " Bottles Of Enchanting");
            this.expThrows = 0;
            this.systemTime = System.nanoTime() / 1000000L;
            this.expSystemTime = System.nanoTime() / 1000000L;
        }
    }
    
    public Announcer() {
        this.delay = this.register(Settings.d("Global Delay", 5000.0));
        this.thanks = this.register(Settings.b("Thanks to...", true));
        this.visualRange = this.register(Settings.b("Visual Range", true));
        this.visualRangeDelay = this.register(Settings.d("Visual Range Delay", 5000.0));
        this.movement = this.register(Settings.b("Movement", true));
        this.movementDelay = this.register(Settings.d("Movement Delay", 5000.0));
        this.totemPops = this.register(Settings.b("Totem Pops", true));
        this.totemPopDelay = this.register(Settings.d("Totem Pops Delay", 5000.0));
        this.eating = this.register(Settings.b("Gapple Eats", true));
        this.eatingDelay = this.register(Settings.d("Gapple Eats Delay", 5000.0));
        this.placing = this.register(Settings.b("Place Obsidian", true));
        this.placingDelay = this.register(Settings.d("Place Delay", 5000.0));
        this.crystal = this.register(Settings.b("AutoCrystal", true));
        this.crystalDelay = this.register(Settings.d("AutoCrystal Delay", 3000.0));
        this.trap = this.register(Settings.b("AutoTrap", true));
        this.trapDelay = this.register(Settings.d("AutoTrap Delay", 3000.0));
        this.exp = this.register(Settings.b("Enchantment Bottle Throw", true));
        this.expDelay = this.register(Settings.d("Throw Delay", 5000.0));
        this.systemTime = -1L;
        this.systemTime2 = -1L;
        this.visualRangeSystemTime = -1L;
        this.joinSystemTime = -1L;
        this.movementSystemTime = -1L;
        this.eatSystemTime = -1L;
        this.placeSystemTime = -1L;
        this.expSystemTime = -1L;
        this.attackSystemTime = -1L;
        this.totemPopSystemTime = -1L;
        this.distance = 0.0;
        this.ticks = 0;
        this.ticks2 = 0;
        this.eats = 0;
        this.obsidianPlacements = 0;
        this.expThrows = 0;
        this.names = new ArrayList<String>();
        this.names2 = new ArrayList<String>();
        this.removal = new ArrayList<String>();
        this.placeListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (Announcer.mc.world == null || Announcer.mc.player == null) {
                return;
            }
            if (!this.placing.getValue()) {
                return;
            }
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                if (Announcer.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
                    ++this.expThrows;
                }
                if (Announcer.mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) && this.ticks2 >= Announcer.mc.rightClickDelayTimer) {
                    ++this.obsidianPlacements;
                    this.ticks2 = 0;
                }
            }
            if (event.getPacket() instanceof CPacketPlayerTryUseItem && Announcer.mc.player.getHeldItemMainhand().getItem() instanceof ItemExpBottle) {
                ++this.expThrows;
            }
        }, new Predicate[0]);
        this.totemPopListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (Announcer.mc.world == null || Announcer.mc.player == null) {
                return;
            }
            if (!this.totemPops.getValue()) {
                return;
            }
            if (event.getPacket() instanceof SPacketEntityStatus) {
                final SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
                if (packet.getOpCode() == 35) {
                    final Entity entity = packet.getEntity((World)Announcer.mc.world);
                    int dist = 13;
                    for (final Entity ent : Announcer.mc.world.loadedEntityList) {
                        if (Announcer.mc.player.getDistance(ent) < 27.0f) {
                            dist = 6;
                        }
                    }
                    if (Announcer.mc.player.getDistance(entity) <= dist && entity != Announcer.mc.player && System.nanoTime() / 1000000L - this.totemPopSystemTime >= this.totemPopDelay.getValue() && System.nanoTime() / 1000000L - this.systemTime2 >= this.delay.getValue()) {
                        sendChatMessage("popped " + entity.getName());
                        this.systemTime2 = System.nanoTime() / 1000000L;
                        this.totemPopSystemTime = System.nanoTime() / 1000000L;
                    }
                }
            }
        }, new Predicate[0]);
        Announcer.INSTANCE = this;
    }
    
    public static void sendChatMessage(final String text) {
        if (Announcer.INSTANCE.thanks.getValue()) {
            Announcer.mc.player.sendChatMessage("I just " + text + ", all thanks to Lunar Lite!");
        }
        else {
            Announcer.mc.player.sendChatMessage("I just " + text + "!");
        }
    }
    
    static {
        Announcer.INSTANCE = new Announcer();
    }
}
