//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.misc;

import me.zeroeightsix.kami.module.*;
import java.util.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.network.play.server.*;

@Info(name = "AutoFish", category = Category.HIDDEN, description = "Automatically catch fish")
public class AutoFish extends Module
{
    private Setting<Boolean> defaultSetting;
    private Setting<Integer> baseDelay;
    private Setting<Integer> extraDelay;
    private Setting<Integer> variation;
    Random random;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public AutoFish() {
        this.defaultSetting = this.register(Settings.b("Defaults", false));
        this.baseDelay = this.register((Setting<Integer>)Settings.integerBuilder("Throw Delay").withValue(450).withMinimum(50).withMaximum(1000).build());
        this.extraDelay = this.register((Setting<Integer>)Settings.integerBuilder("Catch Delay").withValue(300).withMinimum(0).withMaximum(1000).build());
        this.variation = this.register((Setting<Integer>)Settings.integerBuilder("Variation").withValue(50).withMinimum(0).withMaximum(1000).build());
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(e -> {
            if (e.getPacket() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect pck = (SPacketSoundEffect)e.getPacket();
                if (pck.getSound().getSoundName().toString().toLowerCase().contains("entity.bobber.splash")) {
                    if (AutoFish.mc.player.fishEntity == null) {
                        return;
                    }
                    final int soundX = (int)pck.getX();
                    final int soundZ = (int)pck.getZ();
                    final int fishX = (int)AutoFish.mc.player.fishEntity.posX;
                    final int fishZ = (int)AutoFish.mc.player.fishEntity.posZ;
                    if (this.kindaEquals(soundX, fishX) && this.kindaEquals(fishZ, soundZ)) {
                        new Thread(() -> {
                            this.random = new Random();
                            try {
                                Thread.sleep(this.extraDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                            }
                            catch (InterruptedException ex) {}
                            AutoFish.mc.rightClickMouse();
                            this.random = new Random();
                            try {
                                Thread.sleep(this.baseDelay.getValue() + this.random.ints(1L, -this.variation.getValue(), this.variation.getValue()).findFirst().getAsInt());
                            }
                            catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                            AutoFish.mc.rightClickMouse();
                        }).start();
                    }
                }
            }
        }, new Predicate[0]);
    }
    
    @Override
    public void onUpdate() {
        if (this.defaultSetting.getValue()) {
            this.baseDelay.setValue(450);
            this.extraDelay.setValue(300);
            this.variation.setValue(50);
            this.defaultSetting.setValue(false);
            Command.sendChatMessage(this.getChatName() + " Set to defaults!");
            Command.sendChatMessage(this.getChatName() + " Close and reopen the " + this.getName() + " settings menu to see changes");
        }
    }
    
    public boolean kindaEquals(final int kara, final int ni) {
        return ni == kara || ni == kara - 1 || ni == kara + 1;
    }
}
