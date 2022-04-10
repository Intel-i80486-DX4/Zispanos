//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import net.minecraft.client.*;
import java.io.*;

public class LogUtil
{
    public static void writePlayerCoords(final String locationName) {
        final Minecraft mc = Minecraft.getMinecraft();
        writeCoords((int)mc.player.posX, (int)mc.player.posY, (int)mc.player.posZ, locationName);
    }
    
    public static void writeCoords(final int x, final int y, final int z, final String locationName) {
        try {
            final FileWriter fW = new FileWriter("KAMIBlueCoords.txt");
            fW.write(formatter(x, y, z, locationName));
            fW.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static String formatter(final int x, final int y, final int z, final String locationName) {
        return x + ", " + y + ", " + z + ", " + locationName + "\n";
    }
}
