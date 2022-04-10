//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;

@Info(name = "Sprint", description = "Automatically makes the player sprint", category = Category.MOVEMENT, showOnArray = ShowOnArray.OFF)
public class Sprint extends Module
{
    @Override
    public void onUpdate() {
        if (Sprint.mc.player == null) {
            return;
        }
        if (ModuleManager.getModuleByName("ElytraFlight").isEnabled() && (Sprint.mc.player.isElytraFlying() || Sprint.mc.player.capabilities.isFlying)) {
            return;
        }
        try {
            if (!Sprint.mc.player.collidedHorizontally && Sprint.mc.player.moveForward > 0.0f) {
                Sprint.mc.player.setSprinting(true);
            }
            else {
                Sprint.mc.player.setSprinting(false);
            }
        }
        catch (Exception ex) {}
    }
}
