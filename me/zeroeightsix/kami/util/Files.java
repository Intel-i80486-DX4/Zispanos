//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import java.nio.file.*;
import java.util.*;
import java.io.*;

public class Files
{
    public static String[] readFileAllLines(final String path) {
        try {
            final List<String> lines = java.nio.file.Files.readAllLines(Paths.get(path, new String[0]));
            final String[] array = new String[lines.size()];
            lines.toArray(array);
            return array;
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static void writeFile(final String path, final String[] lines) {
        try {
            final FileWriter fw = new FileWriter(path);
            for (final String l : lines) {
                fw.write(l + "\r");
            }
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
