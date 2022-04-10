//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import net.minecraft.pathfinding.*;
import me.zeroeightsix.kami.module.modules.render.*;

public class PathCommand extends Command
{
    int x;
    int y;
    int z;
    
    public PathCommand() {
        super("path", new ChunkBuilder().append("x").append("y").append("z").build(), new String[0]);
        this.x = Integer.MIN_VALUE;
        this.y = Integer.MIN_VALUE;
        this.z = Integer.MIN_VALUE;
        this.setDescription("Pathfinding for AutoWalk");
    }
    
    public void call(final String[] args) {
        if (args[0] != null && args[0].equalsIgnoreCase("retry")) {
            if (this.x != Integer.MIN_VALUE) {
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }
                return;
            }
            Command.sendChatMessage("No location to retry pathfinding to.");
        }
        else {
            if (args.length <= 3) {
                Command.sendChatMessage("&cMissing arguments: x, y, z");
                return;
            }
            try {
                this.x = Integer.parseInt(args[0]);
                this.y = Integer.parseInt(args[1]);
                this.z = Integer.parseInt(args[2]);
                final PathPoint end = new PathPoint(this.x, this.y, this.z);
                Pathfind.createPath(end);
                if (!Pathfind.points.isEmpty()) {
                    Command.sendChatMessage("Path created!");
                }
            }
            catch (NumberFormatException e) {
                Command.sendChatMessage("Error: input must be numerical");
            }
        }
    }
}
