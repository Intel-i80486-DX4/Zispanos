//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.player;

import me.zeroeightsix.kami.module.*;

@Info(name = "TpsSync", description = "Synchronizes some actions with the server TPS", category = Category.PLAYER)
public class TpsSync extends Module
{
    private static TpsSync INSTANCE;
    
    public TpsSync() {
        TpsSync.INSTANCE = this;
    }
    
    public static boolean isSync() {
        return TpsSync.INSTANCE.isEnabled();
    }
}
