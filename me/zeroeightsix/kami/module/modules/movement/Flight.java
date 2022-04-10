//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;

@Info(category = Category.MOVEMENT, description = "Makes the player fly", name = "Flight")
public class Flight extends Module
{
    private Setting<Float> speed;
    private Setting<FlightMode> mode;
    
    public Flight() {
        this.speed = this.register(Settings.f("Speed", 10.0f));
        this.mode = this.register(Settings.e("Mode", FlightMode.VANILLA));
    }
    
    @Override
    protected void onEnable() {
        if (Flight.mc.player == null) {
            return;
        }
        switch (this.mode.getValue()) {
            case VANILLA: {
                Flight.mc.player.capabilities.isFlying = true;
                if (Flight.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                Flight.mc.player.capabilities.allowFlying = true;
                break;
            }
        }
    }
    
    @Override
    public void onUpdate() {
        switch (this.mode.getValue()) {
            case STATIC: {
                Flight.mc.player.capabilities.isFlying = false;
                Flight.mc.player.motionX = 0.0;
                Flight.mc.player.motionY = 0.0;
                Flight.mc.player.motionZ = 0.0;
                Flight.mc.player.jumpMovementFactor = this.speed.getValue();
                if (Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                    final EntityPlayerSP player = Flight.mc.player;
                    player.motionY += this.speed.getValue();
                }
                if (Flight.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    final EntityPlayerSP player2 = Flight.mc.player;
                    player2.motionY -= this.speed.getValue();
                    break;
                }
                break;
            }
            case VANILLA: {
                Flight.mc.player.capabilities.setFlySpeed(this.speed.getValue() / 100.0f);
                Flight.mc.player.capabilities.isFlying = true;
                if (Flight.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                Flight.mc.player.capabilities.allowFlying = true;
                break;
            }
            case PACKET: {
                final boolean forward = Flight.mc.gameSettings.keyBindForward.isKeyDown();
                final boolean left = Flight.mc.gameSettings.keyBindLeft.isKeyDown();
                final boolean right = Flight.mc.gameSettings.keyBindRight.isKeyDown();
                final boolean back = Flight.mc.gameSettings.keyBindBack.isKeyDown();
                int angle;
                if (left && right) {
                    angle = (forward ? 0 : (back ? 180 : -1));
                }
                else if (forward && back) {
                    angle = (left ? -90 : (right ? 90 : -1));
                }
                else {
                    angle = (left ? -90 : (right ? 90 : 0));
                    if (forward) {
                        angle /= 2;
                    }
                    else if (back) {
                        angle = 180 - angle / 2;
                    }
                }
                if (angle != -1 && (forward || left || right || back)) {
                    final float yaw = Flight.mc.player.rotationYaw + angle;
                    Flight.mc.player.motionX = EntityUtil.getRelativeX(yaw) * 0.20000000298023224;
                    Flight.mc.player.motionZ = EntityUtil.getRelativeZ(yaw) * 0.20000000298023224;
                }
                Flight.mc.player.motionY = 0.0;
                Flight.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Flight.mc.player.posX + Flight.mc.player.motionX, Flight.mc.player.posY + (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.0622 : 0.0) - (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() ? 0.0622 : 0.0), Flight.mc.player.posZ + Flight.mc.player.motionZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, false));
                Flight.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(Flight.mc.player.posX + Flight.mc.player.motionX, Flight.mc.player.posY - 42069.0, Flight.mc.player.posZ + Flight.mc.player.motionZ, Flight.mc.player.rotationYaw, Flight.mc.player.rotationPitch, true));
                break;
            }
        }
    }
    
    @Override
    protected void onDisable() {
        switch (this.mode.getValue()) {
            case VANILLA: {
                Flight.mc.player.capabilities.isFlying = false;
                Flight.mc.player.capabilities.setFlySpeed(0.05f);
                if (Flight.mc.player.capabilities.isCreativeMode) {
                    return;
                }
                Flight.mc.player.capabilities.allowFlying = false;
                break;
            }
        }
    }
    
    public double[] moveLooking() {
        return new double[] { Flight.mc.player.rotationYaw * 360.0f / 360.0f * 180.0f / 180.0f, 0.0 };
    }
    
    public enum FlightMode
    {
        VANILLA, 
        STATIC, 
        PACKET;
    }
}
