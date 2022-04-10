//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import net.minecraftforge.client.event.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.pathfinding.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.entity.player.*;
import me.zeroeightsix.kami.module.modules.render.*;
import me.zeroeightsix.kami.module.*;

@Info(name = "AutoWalk", category = Category.HIDDEN, description = "Automatically walks forward")
public class AutoWalk extends Module
{
    private Setting<AutoWalkMode> mode;
    @EventHandler
    private Listener<InputUpdateEvent> inputUpdateEventListener;
    
    public AutoWalk() {
        this.mode = this.register(Settings.e("Mode", AutoWalkMode.FORWARD));
        this.inputUpdateEventListener = (Listener<InputUpdateEvent>)new Listener(event -> {
            switch (this.mode.getValue()) {
                case FORWARD: {
                    event.getMovementInput().moveForward = 1.0f;
                    break;
                }
                case BACKWARDS: {
                    event.getMovementInput().moveForward = -1.0f;
                    break;
                }
                case PATH: {
                    if (Pathfind.points.isEmpty()) {
                        return;
                    }
                    event.getMovementInput().moveForward = 1.0f;
                    if (AutoWalk.mc.player.isInWater() || AutoWalk.mc.player.isInLava()) {
                        AutoWalk.mc.player.movementInput.jump = true;
                    }
                    else if (AutoWalk.mc.player.collidedHorizontally && AutoWalk.mc.player.onGround) {
                        AutoWalk.mc.player.jump();
                    }
                    if (!ModuleManager.isModuleEnabled("Pathfind") || Pathfind.points.isEmpty()) {
                        return;
                    }
                    final PathPoint next = Pathfind.points.get(0);
                    this.lookAt(next);
                    break;
                }
            }
        }, new Predicate[0]);
    }
    
    private void lookAt(final PathPoint pathPoint) {
        final double[] v = EntityUtil.calculateLookAt((double)(pathPoint.x + 0.5f), (double)pathPoint.y, (double)(pathPoint.z + 0.5f), (EntityPlayer)AutoWalk.mc.player);
        AutoWalk.mc.player.rotationYaw = (float)v[0];
        AutoWalk.mc.player.rotationPitch = (float)v[1];
    }
    
    private enum AutoWalkMode
    {
        FORWARD, 
        BACKWARDS, 
        PATH;
    }
}
