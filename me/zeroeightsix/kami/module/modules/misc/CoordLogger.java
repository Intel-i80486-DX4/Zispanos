//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import me.zeroeightsix.kami.command.*;
import java.util.*;

@Info(name = "CoordLogger", category = Category.MISC, description = "Logs teleport coordinates to a file")
public class CoordLogger extends Module
{
    private Setting tpExploit;
    private HashMap knownPlayers;
    
    public CoordLogger() {
        this.tpExploit = this.register(Settings.b("TP Exploit", true));
        this.knownPlayers = new HashMap();
    }
    
    public void onEnable() {
    }
    
    @Override
    public void onUpdate() {
        if (this.tpExploit.getValue()) {
            final List tickEntityList = CoordLogger.mc.world.loadedEntityList;
            if (tickEntityList != null) {
                for (final Entity e : tickEntityList) {
                    if (e instanceof EntityPlayer && e != CoordLogger.mc.player) {
                        final Vec3d targetPos = new Vec3d(e.posX, e.posY, e.posZ);
                        if (this.knownPlayers.containsKey(e)) {
                            final double dist = Math.abs(CoordLogger.mc.player.getPositionVector().subtract(targetPos).length());
                            if (dist > 128.0 && Math.abs(this.knownPlayers.get(e).subtract(targetPos).length()) >= 64.0) {
                                this.knownPlayers.put(e, targetPos);
                                Command.sendChatMessage("[CoordLogger] Player " + e.getName() + " moved to position " + targetPos);
                                continue;
                            }
                        }
                        this.knownPlayers.put(e, targetPos);
                    }
                }
            }
        }
    }
}
