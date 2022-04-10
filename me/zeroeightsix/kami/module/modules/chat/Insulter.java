//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.builder.*;
import me.zeroeightsix.kami.util.*;
import java.util.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.network.*;

@Info(name = "Insulter", category = Category.CHAT)
public class Insulter extends Module
{
    private Setting delay;
    private Setting clientSideOnly;
    private long lastTime;
    private String INSULTS_FILE;
    
    public Insulter() {
        this.delay = this.register(Settings.doubleBuilder("DelaySeconds").withMinimum(0.0).withValue(30.0).withMaximum(120.0));
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
        this.INSULTS_FILE = "ZispanosInsults.txt";
    }
    
    public void onEnable() {
        this.lastTime = System.currentTimeMillis();
    }
    
    @Override
    public void onUpdate() {
        final long time = System.currentTimeMillis();
        final long delayMillis = (long)(this.delay.getValue() * 1000.0);
        if (time - this.lastTime >= delayMillis) {
            this.insult();
            this.lastTime = time;
        }
    }
    
    private String getSingleInsult() {
        final String[] insults = Files.readFileAllLines(this.INSULTS_FILE);
        if (insults == null) {
            final String[] singleInsults = { "$ you're a fucking retard", "Fuck you $", "$ is a bitch", "$ is so EZ LOL", "$ EZ LOG", "Hey $, you're fucking adopted", "Hey $, you're so fucking bad at this game", "Kill yourself $", "Imagine being as retarded as $", "$ is a retarded 12 year old autist", "$ is fucking stupid", "$ is literally cancer", "Don't trust $, he's a fucking scammer", "$ is a fucking scammer nigger" };
            Files.writeFile(this.INSULTS_FILE, singleInsults);
            return this.getSingleInsult();
        }
        return (insults.length == 0) ? "Fuck you $" : insults[new Random().nextInt(insults.length)];
    }
    
    private void insult() {
        if (Insulter.mc.getConnection() != null) {
            final Collection playerInfos = Insulter.mc.getConnection().getPlayerInfoMap();
            if (playerInfos != null && playerInfos.size() != 0) {
                if (new Random().nextInt(4) == 0) {
                    this.getRandomPlayer(playerInfos);
                    this.getRandomPlayer(playerInfos);
                }
                else {
                    final String name = this.getRandomPlayer(playerInfos);
                    if (name.equals("")) {
                        return;
                    }
                    String randInsult = this.getSingleInsult();
                    randInsult = randInsult.replace("$", name);
                    this.writeChat(randInsult);
                }
            }
        }
    }
    
    private void writeChat(final String msg) {
        if (this.clientSideOnly.getValue()) {
            Command.sendChatMessage(msg);
        }
        else {
            Insulter.mc.playerController.connection.sendPacket((Packet)new CPacketChatMessage(msg));
        }
    }
    
    private String getRandomPlayer(final Collection playerInfos) {
        final int i = new Random().nextInt(playerInfos.size());
        final NetworkPlayerInfo playerInfo = (NetworkPlayerInfo)playerInfos.toArray()[i];
        return playerInfo.getGameProfile().getName().equals(Insulter.mc.player.getName()) ? "" : playerInfo.getGameProfile().getName();
    }
}
