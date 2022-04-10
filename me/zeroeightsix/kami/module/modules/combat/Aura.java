//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.combat;

import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.modules.misc.*;
import java.util.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.math.*;

@Info(name = "Aura", category = Category.COMBAT, description = "Hits entities around you")
public class Aura extends Module
{
    private Setting<Boolean> attackPlayers;
    private Setting<Boolean> attackMobs;
    private Setting<Boolean> attackAnimals;
    private Setting<Double> hitRange;
    private Setting<Boolean> ignoreWalls;
    private Setting<WaitMode> waitMode;
    private Setting<Double> waitTick;
    private Setting<Boolean> autoWait;
    private Setting<SwitchMode> switchMode;
    private Setting<HitMode> hitMode;
    private Setting<Boolean> infoMsg;
    private int waitCounter;
    
    public Aura() {
        this.attackPlayers = this.register(Settings.b("Players", true));
        this.attackMobs = this.register(Settings.b("Mobs", false));
        this.attackAnimals = this.register(Settings.b("Animals", false));
        this.hitRange = this.register(Settings.d("Hit Range", 5.5));
        this.ignoreWalls = this.register(Settings.b("Ignore Walls", true));
        this.waitMode = this.register(Settings.e("Mode", WaitMode.TICK));
        this.waitTick = this.register((Setting<Double>)Settings.doubleBuilder("Tick Delay").withMinimum(0.1).withValue(2.0).withMaximum(20.0).build());
        this.autoWait = this.register(Settings.b("Auto Tick Delay", true));
        this.switchMode = this.register(Settings.e("Autoswitch", SwitchMode.ALL));
        this.hitMode = this.register(Settings.e("Tool", HitMode.SWORD));
        this.infoMsg = this.register(Settings.b("Info Message", true));
    }
    
    public void onEnable() {
        if (Aura.mc.player == null) {
            return;
        }
        if (this.autoWait.getValue() && this.infoMsg.getValue()) {
            Command.sendWarningMessage(this.getChatName() + " When Auto Tick Delay is turned on whatever you give Tick Delay doesn't matter, it uses the current TPS instead");
        }
    }
    
    @Override
    public void onUpdate() {
        double autoWaitTick = 0.0;
        if (Aura.mc.player.isDead || Aura.mc.player == null) {
            return;
        }
        if (this.autoWait.getValue()) {
            autoWaitTick = 20.0 - Math.round(LagCompensator.INSTANCE.getTickRate() * 10.0f) / 10.0;
        }
        final boolean shield = Aura.mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && Aura.mc.player.getActiveHand() == EnumHand.OFF_HAND;
        if (Aura.mc.player.isHandActive() && !shield) {
            return;
        }
        if (this.waitMode.getValue().equals(WaitMode.CPS)) {
            if (Aura.mc.player.getCooledAttackStrength(this.getLagComp()) < 1.0f) {
                return;
            }
            if (Aura.mc.player.ticksExisted % 2 != 0) {
                return;
            }
        }
        if (this.autoWait.getValue()) {
            if (this.waitMode.getValue().equals(WaitMode.TICK) && autoWaitTick > 0.0) {
                if (this.waitCounter < autoWaitTick) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
            }
        }
        else if (this.waitMode.getValue().equals(WaitMode.TICK) && this.waitTick.getValue() > 0.0) {
            if (this.waitCounter < this.waitTick.getValue()) {
                ++this.waitCounter;
                return;
            }
            this.waitCounter = 0;
        }
        for (final Entity target : Minecraft.getMinecraft().world.loadedEntityList) {
            if (!EntityUtil.isLiving(target)) {
                continue;
            }
            if (target == Aura.mc.player) {
                continue;
            }
            if (Aura.mc.player.getDistance(target) > this.hitRange.getValue()) {
                continue;
            }
            if (((EntityLivingBase)target).getHealth() <= 0.0f) {
                continue;
            }
            if (this.waitMode.getValue().equals(WaitMode.CPS) && ((EntityLivingBase)target).hurtTime != 0) {
                continue;
            }
            if (!this.ignoreWalls.getValue() && !Aura.mc.player.canEntityBeSeen(target) && !this.canEntityFeetBeSeen(target)) {
                continue;
            }
            if (this.attackPlayers.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
                this.attack(target);
                return;
            }
            Label_0598: {
                if (EntityUtil.isPassive(target)) {
                    if (this.attackAnimals.getValue()) {
                        break Label_0598;
                    }
                    continue;
                }
                else {
                    if (EntityUtil.isMobAggressive(target) && this.attackMobs.getValue()) {
                        break Label_0598;
                    }
                    continue;
                }
                continue;
            }
            if ((!this.switchMode.getValue().equals(SwitchMode.ONLY32K) || this.switchMode.getValue().equals(SwitchMode.ALL)) && ModuleManager.isModuleEnabled("AutoTool")) {
                AutoTool.equipBestWeapon();
            }
            this.attack(target);
        }
    }
    
    private boolean checkSharpness(final ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return false;
        }
        if (stack.getItem().equals(Items.DIAMOND_AXE) && this.hitMode.getValue().equals(HitMode.SWORD)) {
            return false;
        }
        if (stack.getItem().equals(Items.DIAMOND_SWORD) && this.hitMode.getValue().equals(HitMode.AXE)) {
            return false;
        }
        final NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
        if (enchants == null) {
            return false;
        }
        int i = 0;
        while (i < enchants.tagCount()) {
            final NBTTagCompound enchant = enchants.getCompoundTagAt(i);
            if (enchant.getInteger("id") == 16) {
                final int lvl = enchant.getInteger("lvl");
                if (this.switchMode.getValue().equals(SwitchMode.ONLY32K)) {
                    if (lvl >= 42) {
                        return true;
                    }
                    break;
                }
                else if (this.switchMode.getValue().equals(SwitchMode.ALL)) {
                    if (lvl >= 4) {
                        return true;
                    }
                    break;
                }
                else {
                    if (this.switchMode.getValue().equals(SwitchMode.NONE)) {
                        return true;
                    }
                    break;
                }
            }
            else {
                ++i;
            }
        }
        return false;
    }
    
    private void attack(final Entity e) {
        boolean holding32k = false;
        if (this.checkSharpness(Aura.mc.player.getHeldItemMainhand())) {
            holding32k = true;
        }
        if ((this.switchMode.getValue().equals(SwitchMode.ONLY32K) || this.switchMode.getValue().equals(SwitchMode.ALL)) && !holding32k) {
            int newSlot = -1;
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Aura.mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY) {
                    if (this.checkSharpness(stack)) {
                        newSlot = i;
                        break;
                    }
                }
            }
            if (newSlot != -1) {
                Aura.mc.player.inventory.currentItem = newSlot;
                holding32k = true;
            }
        }
        if (this.switchMode.getValue().equals(SwitchMode.ONLY32K) && !holding32k) {
            return;
        }
        Aura.mc.playerController.attackEntity((EntityPlayer)Aura.mc.player, e);
        Aura.mc.player.swingArm(EnumHand.MAIN_HAND);
    }
    
    private float getLagComp() {
        if (this.waitMode.getValue().equals(WaitMode.CPS)) {
            return -(20.0f - LagCompensator.INSTANCE.getTickRate());
        }
        return 0.0f;
    }
    
    private boolean canEntityFeetBeSeen(final Entity entityIn) {
        return Aura.mc.world.rayTraceBlocks(new Vec3d(Aura.mc.player.posX, Aura.mc.player.posY + Aura.mc.player.getEyeHeight(), Aura.mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null;
    }
    
    private enum SwitchMode
    {
        NONE, 
        ALL, 
        ONLY32K;
    }
    
    private enum HitMode
    {
        SWORD, 
        AXE;
    }
    
    private enum WaitMode
    {
        CPS, 
        TICK;
    }
}
