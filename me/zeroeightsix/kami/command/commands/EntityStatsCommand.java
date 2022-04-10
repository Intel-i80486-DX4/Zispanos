//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.*;
import java.math.*;

public class EntityStatsCommand extends Command
{
    public EntityStatsCommand() {
        super("entitystats", (SyntaxChunk[])null, new String[] { "estats" });
        this.setDescription("Print the statistics of the entity you're currently riding");
    }
    
    public void call(final String[] args) {
        if (this.mc.player.getRidingEntity() != null && this.mc.player.getRidingEntity() instanceof AbstractHorse) {
            final AbstractHorse horse = (AbstractHorse)this.mc.player.getRidingEntity();
            final float maxHealth = horse.getMaxHealth();
            final double speed = round(43.17 * horse.getAIMoveSpeed(), 2);
            final double jump = round(-0.1817584952 * Math.pow(horse.getHorseJumpStrength(), 3.0) + 3.689713992 * Math.pow(horse.getHorseJumpStrength(), 2.0) + 2.128599134 * horse.getHorseJumpStrength() - 0.343930367, 4);
            final String ownerId = (horse.getOwnerUniqueId() == null) ? "Not tamed." : horse.getOwnerUniqueId().toString();
            final StringBuilder builder = new StringBuilder("&6Entity Statistics:");
            builder.append("\n&cMax Health: ").append(maxHealth);
            builder.append("\n&cSpeed: ").append(speed);
            builder.append("\n&cJump: ").append(jump);
            builder.append("\n&cOwner: ").append(ownerId);
            Command.sendChatMessage(builder.toString());
        }
        else if (this.mc.player.getRidingEntity() instanceof EntityLivingBase) {
            final EntityLivingBase entity = (EntityLivingBase)this.mc.player.getRidingEntity();
            Command.sendChatMessage("&6Entity Stats:\n&cMax Health: &b" + entity.getMaxHealth() + " &2HP\n&cSpeed: &b" + round(43.17 * entity.getAIMoveSpeed(), 2) + " &2m/s");
        }
        else {
            Command.sendChatMessage("&4&lError: &cNot riding a compatible entity.");
        }
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
