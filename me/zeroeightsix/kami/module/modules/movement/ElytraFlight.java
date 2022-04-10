//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.client.network.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;

@Info(name = "ElytraFlight", description = "Modifies elytras to fly at custom velocities and fall speeds", category = Category.MOVEMENT)
public class ElytraFlight extends Module
{
    private Setting<ElytraFlightMode> mode;
    private Setting<Boolean> defaultSetting;
    private Setting<Boolean> easyTakeOff;
    private Setting<TakeoffMode> takeOffMode;
    private Setting<Boolean> overrideMaxSpeed;
    private Setting<Float> speedHighway;
    private Setting<Float> speedHighwayOverride;
    private Setting<Float> fallSpeedHighway;
    private Setting<Float> fallSpeed;
    private Setting<Float> upSpeedBoost;
    private Setting<Float> downSpeedBoost;
    
    public ElytraFlight() {
        this.mode = this.register(Settings.e("Mode", ElytraFlightMode.HIGHWAY));
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.easyTakeOff = this.register(Settings.booleanBuilder("Easy Takeoff").withValue(true).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.takeOffMode = this.register((Setting<TakeoffMode>)Settings.enumBuilder(TakeoffMode.class).withName("Takeoff Mode").withValue(TakeoffMode.PACKET).withVisibility(v -> this.easyTakeOff.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.overrideMaxSpeed = this.register(Settings.booleanBuilder("Over Max Speed").withValue(false).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.speedHighway = this.register(Settings.floatBuilder("Speed H").withValue(1.8f).withMaximum(1.8f).withVisibility(v -> !this.overrideMaxSpeed.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.speedHighwayOverride = this.register(Settings.floatBuilder("Speed H O").withValue(1.8f).withVisibility(v -> this.overrideMaxSpeed.getValue() && this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.fallSpeedHighway = this.register(Settings.floatBuilder("Fall Speed H").withValue(5.0000002E-5f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.fallSpeed = this.register(Settings.floatBuilder("Fall Speed").withValue(-0.003f).withVisibility(v -> !this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)).build());
        this.upSpeedBoost = this.register(Settings.floatBuilder("Up Speed B").withValue(0.08f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.BOOST)).build());
        this.downSpeedBoost = this.register(Settings.floatBuilder("Down Speed B").withValue(0.04f).withVisibility(v -> this.mode.getValue().equals(ElytraFlightMode.BOOST)).build());
    }
    
    @Override
    public void onUpdate() {
        if (ElytraFlight.mc.player == null) {
            return;
        }
        if (this.defaultSetting.getValue()) {
            this.easyTakeOff.setValue(true);
            this.takeOffMode.setValue(TakeoffMode.PACKET);
            this.overrideMaxSpeed.setValue(false);
            this.speedHighway.setValue(1.8f);
            this.speedHighwayOverride.setValue(1.8f);
            this.fallSpeedHighway.setValue(5.0000002E-5f);
            this.fallSpeed.setValue(-0.003f);
            this.upSpeedBoost.setValue(0.08f);
            this.downSpeedBoost.setValue(0.04f);
            this.defaultSetting.setValue(false);
            Command.sendChatMessage(this.getChatName() + " Set to defaults!");
            Command.sendChatMessage(this.getChatName() + " Close and reopen the " + this.getName() + " settings menu to see changes");
        }
        if (this.mode.getValue().equals(ElytraFlightMode.HIGHWAY) && this.easyTakeOff.getValue() && !ElytraFlight.mc.player.isElytraFlying() && !ElytraFlight.mc.player.onGround) {
            switch (this.takeOffMode.getValue()) {
                case CLIENT: {
                    ElytraFlight.mc.player.capabilities.isFlying = true;
                }
                case PACKET: {
                    Objects.requireNonNull(ElytraFlight.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    break;
                }
            }
        }
        if (this.mode.getValue().equals(ElytraFlightMode.HIGHWAY) && this.easyTakeOff.getValue() && ElytraFlight.mc.player.isElytraFlying()) {
            this.easyTakeOff.setValue(false);
            Command.sendChatMessage(this.getChatName() + "Disabled takeoff!");
        }
        if (ElytraFlight.mc.player.capabilities.isFlying) {
            if (this.mode.getValue().equals(ElytraFlightMode.HIGHWAY)) {
                ElytraFlight.mc.player.setSprinting(false);
                ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                ElytraFlight.mc.player.setPosition(ElytraFlight.mc.player.posX, ElytraFlight.mc.player.posY - this.fallSpeedHighway.getValue(), ElytraFlight.mc.player.posZ);
                ElytraFlight.mc.player.capabilities.setFlySpeed(this.getHighwaySpeed());
            }
            else {
                ElytraFlight.mc.player.setVelocity(0.0, 0.0, 0.0);
                ElytraFlight.mc.player.capabilities.setFlySpeed(0.915f);
                ElytraFlight.mc.player.setPosition(ElytraFlight.mc.player.posX, ElytraFlight.mc.player.posY - this.fallSpeed.getValue(), ElytraFlight.mc.player.posZ);
            }
        }
        if (ElytraFlight.mc.player.onGround) {
            ElytraFlight.mc.player.capabilities.allowFlying = false;
        }
        if (!ElytraFlight.mc.player.isElytraFlying()) {
            return;
        }
        switch (this.mode.getValue()) {
            case BOOST: {
                if (ElytraFlight.mc.player.isInWater()) {
                    Objects.requireNonNull(ElytraFlight.mc.getConnection()).sendPacket((Packet)new CPacketEntityAction((Entity)ElytraFlight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    return;
                }
                if (ElytraFlight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = ElytraFlight.mc.player;
                    player.motionY += this.upSpeedBoost.getValue();
                }
                else if (ElytraFlight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2 = ElytraFlight.mc.player;
                    player2.motionY -= this.downSpeedBoost.getValue();
                }
                if (ElytraFlight.mc.gameSettings.keyBindForward.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                    final EntityPlayerSP player3 = ElytraFlight.mc.player;
                    player3.motionX -= MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player4 = ElytraFlight.mc.player;
                    player4.motionZ += MathHelper.cos(yaw) * 0.05f;
                }
                else if (ElytraFlight.mc.gameSettings.keyBindBack.isKeyDown()) {
                    final float yaw = (float)Math.toRadians(ElytraFlight.mc.player.rotationYaw);
                    final EntityPlayerSP player5 = ElytraFlight.mc.player;
                    player5.motionX += MathHelper.sin(yaw) * 0.05f;
                    final EntityPlayerSP player6 = ElytraFlight.mc.player;
                    player6.motionZ -= MathHelper.cos(yaw) * 0.05f;
                }
            }
            default: {
                ElytraFlight.mc.player.capabilities.setFlySpeed(0.915f);
                ElytraFlight.mc.player.capabilities.isFlying = true;
                if (ElytraFlight.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                ElytraFlight.mc.player.capabilities.allowFlying = true;
            }
        }
    }
    
    @Override
    protected void onDisable() {
        ElytraFlight.mc.player.capabilities.isFlying = false;
        ElytraFlight.mc.player.capabilities.setFlySpeed(0.05f);
        if (ElytraFlight.mc.player.capabilities.isCreativeMode) {
            return;
        }
        ElytraFlight.mc.player.capabilities.allowFlying = false;
    }
    
    private float getHighwaySpeed() {
        if (this.overrideMaxSpeed.getValue()) {
            return this.speedHighwayOverride.getValue();
        }
        return this.speedHighway.getValue();
    }
    
    private enum ElytraFlightMode
    {
        BOOST, 
        FLY, 
        HIGHWAY;
    }
    
    private enum TakeoffMode
    {
        CLIENT, 
        PACKET;
    }
}
