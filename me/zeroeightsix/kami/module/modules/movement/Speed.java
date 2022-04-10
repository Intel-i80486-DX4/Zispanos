//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.util.math.*;
import net.minecraft.client.entity.*;

@Info(name = "Strafe", description = "Strafe", category = Category.MOVEMENT)
public class Speed extends Module
{
    private Setting<Boolean> jump;
    int waitCounter;
    int forward;
    private static final AxisAlignedBB WATER_WALK_AA;
    
    public Speed() {
        this.jump = this.register(Settings.b("AutoJump", true));
        this.forward = 1;
    }
    
    @Override
    public void onUpdate() {
        final boolean boost = Math.abs(Speed.mc.player.rotationYawHead - Speed.mc.player.rotationYaw) < 90.0f;
        if (Speed.mc.player.moveForward != 0.0f) {
            if (!Speed.mc.player.isSprinting()) {
                Speed.mc.player.setSprinting(true);
            }
            float yaw = Speed.mc.player.rotationYaw;
            if (Speed.mc.player.moveForward > 0.0f) {
                if (Speed.mc.player.movementInput.moveStrafe != 0.0f) {
                    yaw += ((Speed.mc.player.movementInput.moveStrafe > 0.0f) ? -45.0f : 45.0f);
                }
                this.forward = 1;
                Speed.mc.player.moveForward = 1.0f;
                Speed.mc.player.moveStrafing = 0.0f;
            }
            else if (Speed.mc.player.moveForward < 0.0f) {
                if (Speed.mc.player.movementInput.moveStrafe != 0.0f) {
                    yaw += ((Speed.mc.player.movementInput.moveStrafe > 0.0f) ? 45.0f : -45.0f);
                }
                this.forward = -1;
                Speed.mc.player.moveForward = -1.0f;
                Speed.mc.player.moveStrafing = 0.0f;
            }
            if (Speed.mc.player.onGround) {
                Speed.mc.player.setJumping(false);
                if (this.waitCounter < 1) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
                final float f = (float)Math.toRadians(yaw);
                if (this.jump.getValue()) {
                    Speed.mc.player.motionY = 0.405;
                    final EntityPlayerSP player = Speed.mc.player;
                    player.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                    final EntityPlayerSP player2 = Speed.mc.player;
                    player2.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                }
                else if (Speed.mc.gameSettings.keyBindJump.isPressed()) {
                    Speed.mc.player.motionY = 0.405;
                    final EntityPlayerSP player3 = Speed.mc.player;
                    player3.motionX -= MathHelper.sin(f) * 0.1f * (double)this.forward;
                    final EntityPlayerSP player4 = Speed.mc.player;
                    player4.motionZ += MathHelper.cos(f) * 0.1f * (double)this.forward;
                }
            }
            else {
                if (this.waitCounter < 1) {
                    ++this.waitCounter;
                    return;
                }
                this.waitCounter = 0;
                final double currentSpeed = Math.sqrt(Speed.mc.player.motionX * Speed.mc.player.motionX + Speed.mc.player.motionZ * Speed.mc.player.motionZ);
                double speed = boost ? 1.0064 : 1.001;
                if (Speed.mc.player.motionY < 0.0) {
                    speed = 1.0;
                }
                final double direction = Math.toRadians(yaw);
                Speed.mc.player.motionX = -Math.sin(direction) * speed * currentSpeed * this.forward;
                Speed.mc.player.motionZ = Math.cos(direction) * speed * currentSpeed * this.forward;
            }
        }
    }
    
    static {
        WATER_WALK_AA = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.99, 1.0);
    }
}
