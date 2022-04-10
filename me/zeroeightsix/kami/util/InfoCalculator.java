//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import java.text.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.item.*;

public class InfoCalculator
{
    private static DecimalFormat formatter;
    
    public static int ping(final Minecraft mc) {
        if (mc.getConnection() == null) {
            return 1;
        }
        if (mc.player == null) {
            return -1;
        }
        try {
            return mc.getConnection().getPlayerInfo(mc.player.getUniqueID()).getResponseTime();
        }
        catch (NullPointerException ex) {
            return -1;
        }
    }
    
    public static String speed(final boolean useUnitKmH, final Minecraft mc) {
        final float currentTps = mc.timer.tickLength / 1000.0f;
        double multiply = 1.0;
        if (useUnitKmH) {
            multiply = 3.6;
        }
        return InfoCalculator.formatter.format(MathHelper.sqrt(Math.pow(coordsDiff('x', mc), 2.0) + Math.pow(coordsDiff('z', mc), 2.0)) / currentTps * multiply);
    }
    
    private static double coordsDiff(final char s, final Minecraft mc) {
        switch (s) {
            case 'x': {
                return mc.player.posX - mc.player.prevPosX;
            }
            case 'z': {
                return mc.player.posZ - mc.player.prevPosZ;
            }
            default: {
                return 0.0;
            }
        }
    }
    
    public static int dura(final Minecraft mc) {
        final ItemStack itemStack = mc.player.getHeldItemMainhand();
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }
    
    public static String memory() {
        return "" + Runtime.getRuntime().freeMemory() / 1000000L;
    }
    
    public static String tps() {
        return "" + Math.round(LagCompensator.INSTANCE.getTickRate());
    }
    
    public static double round(final double value, final int places) {
        final double scale = Math.pow(10.0, places);
        return Math.round(value * scale) / scale;
    }
    
    public static boolean isNumberEven(final int i) {
        return (i & 0x1) == 0x0;
    }
    
    public static int reverseNumber(final int num, final int min, final int max) {
        return max + min - num;
    }
    
    public static String cardinalToAxis(final char cardinal) {
        switch (cardinal) {
            case 'N': {
                return "-Z";
            }
            case 'S': {
                return "+Z";
            }
            case 'E': {
                return "+X";
            }
            case 'W': {
                return "-X";
            }
            default: {
                return "invalid";
            }
        }
    }
    
    public static String playerDimension(final Minecraft mc) {
        switch (mc.player.dimension) {
            case -1: {
                return "Nether";
            }
            case 0: {
                return "Overworld";
            }
            case 1: {
                return "End";
            }
            default: {
                return "No Dimension";
            }
        }
    }
    
    static {
        InfoCalculator.formatter = new DecimalFormat("#.#");
    }
}
