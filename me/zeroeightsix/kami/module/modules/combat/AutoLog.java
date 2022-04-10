//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import net.minecraftforge.event.entity.living.*;
import me.zero.alpine.listener.*;
import net.minecraftforge.event.entity.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.item.*;
import me.zeroeightsix.kami.module.modules.crystals.*;
import net.minecraft.entity.*;

@Info(name = "AutoLog", description = "Automatically log when in danger or on low health", category = Category.COMBAT)
public class AutoLog extends Module
{
    private Setting<Integer> health;
    private boolean shouldLog;
    long lastLog;
    @EventHandler
    private Listener<LivingDamageEvent> livingDamageEventListener;
    @EventHandler
    private Listener<EntityJoinWorldEvent> entityJoinWorldEventListener;
    
    public AutoLog() {
        this.health = this.register((Setting<Integer>)Settings.integerBuilder("Health").withRange(0, 36).withValue(6).build());
        this.shouldLog = false;
        this.lastLog = System.currentTimeMillis();
        this.livingDamageEventListener = (Listener<LivingDamageEvent>)new Listener(event -> {
            if (AutoLog.mc.player == null) {
                return;
            }
            if (event.getEntity() == AutoLog.mc.player && AutoLog.mc.player.getHealth() - event.getAmount() < this.health.getValue()) {
                this.log();
            }
        }, new Predicate[0]);
        this.entityJoinWorldEventListener = (Listener<EntityJoinWorldEvent>)new Listener(event -> {
            if (AutoLog.mc.player == null) {
                return;
            }
            if (event.getEntity() instanceof EntityEnderCrystal && AutoLog.mc.player.getHealth() - CrystalAuraKAMI.calculateDamage((EntityEnderCrystal)event.getEntity(), (Entity)AutoLog.mc.player) < this.health.getValue()) {
                this.log();
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.shouldLog) {
            this.shouldLog = false;
            if (System.currentTimeMillis() - this.lastLog < 2000L) {
                return;
            }
            Minecraft.getMinecraft().getConnection().handleDisconnect(new SPacketDisconnect((ITextComponent)new TextComponentString("AutoLogged")));
        }
    }
    
    private void log() {
        ModuleManager.getModuleByName("AutoReconnect").disable();
        this.shouldLog = true;
        this.lastLog = System.currentTimeMillis();
    }
}
