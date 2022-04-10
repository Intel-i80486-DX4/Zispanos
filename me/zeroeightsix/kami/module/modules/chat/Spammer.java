//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.command.*;
import java.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

@Info(name = "Spammer", category = Category.CHAT)
public class Spammer extends Module
{
    private Setting<String> file;
    private Setting<Mode> mode;
    private Setting<Double> delaySeconds;
    private Setting<Boolean> greentext;
    private Setting<Boolean> clientSideOnly;
    private int currentLine;
    private String[] lines;
    private long lastTime;
    
    public Spammer() {
        this.file = this.register(Settings.stringBuilder("File").withValue("ZispanosSpam.txt").build());
        this.mode = this.register(Settings.e("Mode", Mode.IN_ORDER));
        this.delaySeconds = this.register(Settings.d("DelaySeconds", 5.0));
        this.greentext = this.register(Settings.b("Greentext", false));
        this.clientSideOnly = this.register(Settings.b("ClientSideOnly", true));
    }
    
    public void onEnable() {
        final String path = this.file.getValue();
        this.lines = Files.readFileAllLines(path);
        if (this.lines == null) {
            Command.sendErrorMessage("Cannot read file: " + path + ". Disabling Spammer.");
            this.disable();
            return;
        }
        if (this.lines.length == 0) {
            Command.sendErrorMessage("File empty: " + path + ". Disabling Spammer.");
            this.disable();
            return;
        }
        this.currentLine = 0;
        this.lastTime = 0L;
    }
    
    @Override
    public void onUpdate() {
        final long now = System.currentTimeMillis();
        final long delayMillis = (long)(this.delaySeconds.getValue() * 1000.0);
        if (now - this.lastTime >= delayMillis) {
            this.lastTime = now;
            switch (this.mode.getValue()) {
                case RANDOM: {
                    this.writeChat(this.lines[new Random().nextInt(this.lines.length)]);
                    break;
                }
                case IN_ORDER: {
                    this.writeChat(this.lines[this.currentLine]);
                    ++this.currentLine;
                    if (this.currentLine >= this.lines.length) {
                        Command.sendChatMessage("Spammer reached end of file. Restarting from beginning.");
                        this.currentLine = 0;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void writeChat(final String msg) {
        if (this.clientSideOnly.getValue()) {
            Command.sendChatMessage(msg);
        }
        else {
            String chatMsg = "";
            if (this.greentext.getValue()) {
                chatMsg += ">";
            }
            chatMsg += msg;
            Spammer.mc.playerController.connection.sendPacket((Packet)new CPacketChatMessage(chatMsg));
        }
    }
    
    @Override
    public String getHudInfo() {
        if (this.mode.getValue() == Mode.IN_ORDER) {
            return "" + this.currentLine + "/" + this.lines.length;
        }
        return "";
    }
    
    public enum Mode
    {
        RANDOM, 
        IN_ORDER;
    }
}
