//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.setting.*;

@Info(name = "Chams", category = Category.RENDER, description = "See entities through walls")
public class Chams extends Module
{
    private static Setting<Boolean> players;
    private static Setting<Boolean> animals;
    private static Setting<Boolean> mobs;
    
    public Chams() {
        this.registerAll(Chams.players, Chams.animals, Chams.mobs);
    }
    
    public static boolean renderChams(final Entity entity) {
        return (entity instanceof EntityPlayer) ? Chams.players.getValue() : (EntityUtil.isPassive(entity) ? Chams.animals.getValue() : ((boolean)Chams.mobs.getValue()));
    }
    
    static {
        Chams.players = Settings.b("Players", true);
        Chams.animals = Settings.b("Animals", false);
        Chams.mobs = Settings.b("Mobs", false);
    }
}
