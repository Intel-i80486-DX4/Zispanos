//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.util.*;
import net.minecraft.world.chunk.*;

@Info(name = "EntitySpeed", category = Category.MOVEMENT, description = "Abuse client-sided movement to shape sound barrier breaking rideables")
public class EntitySpeed extends Module
{
    private Setting<Float> speed;
    private Setting<Boolean> antiStuck;
    private Setting<Boolean> flight;
    private Setting<Boolean> wobble;
    private static Setting<Float> opacity;
    
    public EntitySpeed() {
        this.speed = this.register(Settings.f("Speed", 1.0f));
        this.antiStuck = this.register(Settings.b("AntiStuck"));
        this.flight = this.register(Settings.b("Flight", false));
        this.wobble = this.register(Settings.booleanBuilder("Wobble").withValue(true).withVisibility(b -> this.flight.getValue()).build());
        this.register(EntitySpeed.opacity);
    }
    
    @Override
    public void onUpdate() {
        if (EntitySpeed.mc.world != null && EntitySpeed.mc.player.getRidingEntity() != null) {
            final Entity riding = EntitySpeed.mc.player.getRidingEntity();
            if (riding instanceof EntityPig || riding instanceof AbstractHorse) {
                this.steerEntity(riding);
            }
            else if (riding instanceof EntityBoat) {
                this.steerBoat(this.getBoat());
            }
        }
    }
    
    private void steerEntity(final Entity entity) {
        if (!this.flight.getValue()) {
            entity.motionY = -0.4;
        }
        if (this.flight.getValue()) {
            if (EntitySpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
                entity.motionY = this.speed.getValue();
            }
            else if (EntitySpeed.mc.gameSettings.keyBindForward.isKeyDown() || EntitySpeed.mc.gameSettings.keyBindBack.isKeyDown()) {
                entity.motionY = (this.wobble.getValue() ? Math.sin(EntitySpeed.mc.player.ticksExisted) : 0.0);
            }
        }
        this.moveForward(entity, this.speed.getValue() * 3.8);
        if (entity instanceof EntityHorse) {
            entity.rotationYaw = EntitySpeed.mc.player.rotationYaw;
        }
    }
    
    private void steerBoat(final EntityBoat boat) {
        if (boat == null) {
            return;
        }
        final boolean forward = EntitySpeed.mc.gameSettings.keyBindForward.isKeyDown();
        final boolean left = EntitySpeed.mc.gameSettings.keyBindLeft.isKeyDown();
        final boolean right = EntitySpeed.mc.gameSettings.keyBindRight.isKeyDown();
        final boolean back = EntitySpeed.mc.gameSettings.keyBindBack.isKeyDown();
        if (!forward || !back) {
            boat.motionY = 0.0;
        }
        if (EntitySpeed.mc.gameSettings.keyBindJump.isKeyDown()) {
            boat.motionY += this.speed.getValue() / 2.0f;
        }
        if (!forward && !left && !right && !back) {
            return;
        }
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
        if (angle == -1) {
            return;
        }
        final float yaw = EntitySpeed.mc.player.rotationYaw + angle;
        boat.motionX = EntityUtil.getRelativeX(yaw) * this.speed.getValue();
        boat.motionZ = EntityUtil.getRelativeZ(yaw) * this.speed.getValue();
    }
    
    @Override
    public void onRender() {
        final EntityBoat boat = this.getBoat();
        if (boat == null) {
            return;
        }
        boat.rotationYaw = EntitySpeed.mc.player.rotationYaw;
        boat.updateInputs(false, false, false, false);
    }
    
    private EntityBoat getBoat() {
        if (EntitySpeed.mc.player.getRidingEntity() != null && EntitySpeed.mc.player.getRidingEntity() instanceof EntityBoat) {
            return (EntityBoat)EntitySpeed.mc.player.getRidingEntity();
        }
        return null;
    }
    
    private void moveForward(final Entity entity, final double speed) {
        if (entity != null) {
            final MovementInput movementInput = EntitySpeed.mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            final boolean movingForward = forward != 0.0;
            final boolean movingStrafe = strafe != 0.0;
            float yaw = EntitySpeed.mc.player.rotationYaw;
            if (!movingForward && !movingStrafe) {
                this.setEntitySpeed(entity, 0.0, 0.0);
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else {
                        forward = -1.0;
                    }
                }
                double motX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
                double motZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
                if (this.isBorderingChunk(entity, motX, motZ)) {
                    motZ = (motX = 0.0);
                }
                this.setEntitySpeed(entity, motX, motZ);
            }
        }
    }
    
    private void setEntitySpeed(final Entity entity, final double motX, final double motZ) {
        entity.motionX = motX;
        entity.motionZ = motZ;
    }
    
    private boolean isBorderingChunk(final Entity entity, final double motX, final double motZ) {
        return this.antiStuck.getValue() && EntitySpeed.mc.world.getChunk((int)(entity.posX + motX) >> 4, (int)(entity.posZ + motZ) >> 4) instanceof EmptyChunk;
    }
    
    public static float getOpacity() {
        return EntitySpeed.opacity.getValue();
    }
    
    static {
        EntitySpeed.opacity = Settings.f("Boat opacity", 0.5f);
    }
}
