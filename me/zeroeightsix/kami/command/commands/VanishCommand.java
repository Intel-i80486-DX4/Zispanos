//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import net.minecraft.entity.*;
import net.minecraft.client.*;
import me.zeroeightsix.kami.command.syntax.*;

public class VanishCommand extends Command
{
    private static Entity vehicle;
    Minecraft mc;
    
    public VanishCommand() {
        super("vanish", (SyntaxChunk[])null, new String[0]);
        this.mc = Minecraft.getMinecraft();
        this.setDescription("Allows you to vanish using an entity");
    }
    
    public void call(final String[] args) {
        if (this.mc.player.getRidingEntity() != null && VanishCommand.vehicle == null) {
            VanishCommand.vehicle = this.mc.player.getRidingEntity();
            this.mc.player.dismountRidingEntity();
            this.mc.world.removeEntityFromWorld(VanishCommand.vehicle.getEntityId());
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " removed.");
        }
        else if (VanishCommand.vehicle != null) {
            VanishCommand.vehicle.isDead = false;
            this.mc.world.addEntityToWorld(VanishCommand.vehicle.getEntityId(), VanishCommand.vehicle);
            this.mc.player.startRiding(VanishCommand.vehicle, true);
            Command.sendChatMessage("Vehicle " + VanishCommand.vehicle.getName() + " created.");
            VanishCommand.vehicle = null;
        }
        else {
            Command.sendChatMessage("No Vehicle.");
        }
    }
}
