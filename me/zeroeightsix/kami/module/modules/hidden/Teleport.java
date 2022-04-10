//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.hidden;

import me.zeroeightsix.kami.module.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "Teleport", description = "Library for teleport command", category = Category.HIDDEN)
public class Teleport extends Module
{
    private long lastTp;
    private Vec3d lastPos;
    public static Vec3d finalPos;
    public static double blocksPerTeleport;
    
    @Override
    public void onUpdate() {
        if (Teleport.finalPos == null) {
            Command.sendErrorMessage("Position not set, use .tp");
            this.disable();
            return;
        }
        final Vec3d tpDirectionVec = Teleport.finalPos.subtract(Teleport.mc.player.posX, Teleport.mc.player.posY, Teleport.mc.player.posZ).normalize();
        if (Teleport.mc.world.isBlockLoaded(Teleport.mc.player.getPosition())) {
            this.lastPos = new Vec3d(Teleport.mc.player.posX, Teleport.mc.player.posY, Teleport.mc.player.posZ);
            if (Teleport.finalPos.distanceTo(new Vec3d(Teleport.mc.player.posX, Teleport.mc.player.posY, Teleport.mc.player.posZ)) < 0.3 || Teleport.blocksPerTeleport == 0.0) {
                Command.sendChatMessage("Teleport Finished!");
                this.disable();
            }
            else {
                Teleport.mc.player.setVelocity(0.0, 0.0, 0.0);
            }
            if (Teleport.finalPos.distanceTo(new Vec3d(Teleport.mc.player.posX, Teleport.mc.player.posY, Teleport.mc.player.posZ)) >= Teleport.blocksPerTeleport) {
                final Vec3d vec = tpDirectionVec.scale(Teleport.blocksPerTeleport);
                Teleport.mc.player.setPosition(Teleport.mc.player.posX + vec.x, Teleport.mc.player.posY + vec.y, Teleport.mc.player.posZ + vec.z);
            }
            else {
                final Vec3d vec = tpDirectionVec.scale(Teleport.finalPos.distanceTo(new Vec3d(Teleport.mc.player.posX, Teleport.mc.player.posY, Teleport.mc.player.posZ)));
                Teleport.mc.player.setPosition(Teleport.mc.player.posX + vec.x, Teleport.mc.player.posY + vec.y, Teleport.mc.player.posZ + vec.z);
                this.disable();
            }
            this.lastTp = System.currentTimeMillis();
        }
        else if (this.lastTp + 2000L > System.currentTimeMillis()) {
            Teleport.mc.player.setPosition(this.lastPos.x, this.lastPos.y, this.lastPos.z);
        }
    }
}
