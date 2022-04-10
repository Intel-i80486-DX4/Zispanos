//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import net.minecraft.util.text.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.server.*;

@Info(name = "ChatTimestamp", category = Category.HIDDEN, description = "Shows the time a message was sent beside the message", showOnArray = ShowOnArray.OFF)
public class ChatTimestamp extends Module
{
    private Setting<ColourTextFormatting.ColourCode> firstColour;
    private Setting<ColourTextFormatting.ColourCode> secondColour;
    private Setting<TimeUtil.TimeType> timeTypeSetting;
    private Setting<TimeUtil.TimeUnit> timeUnitSetting;
    private Setting<Boolean> doLocale;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    
    public ChatTimestamp() {
        this.firstColour = this.register(Settings.e("First Colour", (Enum)ColourTextFormatting.ColourCode.GRAY));
        this.secondColour = this.register(Settings.e("Second Colour", (Enum)ColourTextFormatting.ColourCode.WHITE));
        this.timeTypeSetting = this.register(Settings.e("Time Format", (Enum)TimeUtil.TimeType.HHMM));
        this.timeUnitSetting = this.register(Settings.e("Time Unit", (Enum)TimeUtil.TimeUnit.H12));
        this.doLocale = this.register(Settings.b("Show AMPM", true));
        this.listener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (ChatTimestamp.mc.player == null || this.isDisabled()) {
                return;
            }
            if (!(event.getPacket() instanceof SPacketChat)) {
                return;
            }
            final SPacketChat sPacketChat = (SPacketChat)event.getPacket();
            if (this.addTime(sPacketChat.getChatComponent().getUnformattedText())) {
                event.cancel();
            }
        }, new Predicate[0]);
    }
    
    private boolean addTime(final String message) {
        Command.sendRawChatMessage("<" + TimeUtil.getFinalTime(this.setToText(this.secondColour.getValue()), this.setToText(this.firstColour.getValue()), (TimeUtil.TimeUnit)this.timeUnitSetting.getValue(), (TimeUtil.TimeType)this.timeTypeSetting.getValue(), Boolean.valueOf(this.doLocale.getValue())) + TextFormatting.RESET + "> " + message);
        return true;
    }
    
    private TextFormatting setToText(final ColourTextFormatting.ColourCode colourCode) {
        return ColourTextFormatting.toTextMap.get(colourCode);
    }
}
