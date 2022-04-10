//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import net.minecraft.client.network.*;
import net.minecraft.scoreboard.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.command.*;

@Info(name = "TabFriends", description = "Highlights friends in the tab menu", category = Category.GUI, showOnArray = ShowOnArray.OFF)
public class TabFriends extends Module
{
    public Setting<Boolean> startupGlobal;
    public static TabFriends INSTANCE;
    
    public TabFriends() {
        this.startupGlobal = this.register(Settings.b("Enable Automatically", true));
        TabFriends.INSTANCE = this;
    }
    
    public static String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        final String dname = (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName((Team)networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (Friends.isFriend(dname)) {
            return String.format("%sa%s", '§', dname);
        }
        return dname;
    }
    
    public void onDisable() {
        Command.sendAutoDisableMessage(this.getName(), this.startupGlobal.getValue());
    }
}
