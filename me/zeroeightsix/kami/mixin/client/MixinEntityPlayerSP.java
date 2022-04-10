//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.mixin.client;

import net.minecraft.entity.player.*;
import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import com.mojang.authlib.*;
import me.zeroeightsix.kami.module.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.inventory.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.world.*;
import me.zeroeightsix.kami.util.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.entity.*;
import me.zeroeightsix.kami.event.events.*;
import me.zeroeightsix.kami.*;

@Mixin({ EntityPlayerSP.class })
public abstract class MixinEntityPlayerSP extends EntityPlayer
{
    public MixinEntityPlayerSP(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"))
    public void closeScreen(final EntityPlayerSP entityPlayerSP) {
        if (ModuleManager.isModuleEnabled("PortalChat")) {
            return;
        }
    }
    
    @Redirect(method = { "onLivingUpdate" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"))
    public void closeScreen(final Minecraft minecraft, final GuiScreen screen) {
        if (ModuleManager.isModuleEnabled("PortalChat")) {
            return;
        }
    }
    
    @Inject(method = { "displayGUIChest" }, at = { @At("HEAD") }, cancellable = true)
    public void onDisplayGUIChest(final IInventory chestInventory, final CallbackInfo ci) {
        if (ModuleManager.getModuleByName("BeaconSelector").isEnabled() && chestInventory instanceof IInteractionObject && "minecraft:beacon".equals(((IInteractionObject)chestInventory).getGuiID())) {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen)new BeaconGui(this.inventory, chestInventory));
            ci.cancel();
        }
    }
    
    @Inject(method = { "move" }, at = { @At("HEAD") }, cancellable = true)
    public void move(final MoverType type, final double x, final double y, final double z, final CallbackInfo info) {
        final PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        KamiMod.EVENT_BUS.post((Object)event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }
}
