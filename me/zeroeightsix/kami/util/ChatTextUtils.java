//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import java.util.*;

public class ChatTextUtils
{
    public static final String CHAT_SUFFIX = " \u23d0 \u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
    public static final String SECTIONSIGN = "§";
    private static Random rand;
    
    public static String appendChatSuffix(String message) {
        message = cropMaxLengthMessage(message, " \u23d0 \u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280".length());
        message += " \u23d0 \u1d22\u026a\ua731\u1d18\u1d00\u0274\u1d0f\ua731 \u0299\u1d00\u1d04\u1d0b\u1d05\u1d0f\u1d0f\u0280";
        return cropMaxLengthMessage(message);
    }
    
    public static String appendChatSuffix(String message, final String suffix) {
        message = cropMaxLengthMessage(message, suffix.length());
        message += suffix;
        return cropMaxLengthMessage(message);
    }
    
    public static String generateRandomHexSuffix(final int n) {
        final StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(Integer.toHexString((ChatTextUtils.rand.nextInt() + 11) * ChatTextUtils.rand.nextInt()).substring(0, n));
        sb.append(']');
        return sb.toString();
    }
    
    public static String cropMaxLengthMessage(String s, final int i) {
        if (s.length() > 255 - i) {
            s = s.substring(0, 255 - i);
        }
        return s;
    }
    
    public static String cropMaxLengthMessage(final String s) {
        return cropMaxLengthMessage(s, 0);
    }
    
    public static String transformPlainToFancy(final String input) {
        String output = input.toLowerCase();
        output = output.replace("a", "\u1d00");
        output = output.replace("b", "\u0299");
        output = output.replace("c", "\u1d04");
        output = output.replace("d", "\u1d05");
        output = output.replace("e", "\u1d07");
        output = output.replace("f", "\u0493");
        output = output.replace("g", "\u0262");
        output = output.replace("h", "\u029c");
        output = output.replace("i", "\u026a");
        output = output.replace("j", "\u1d0a");
        output = output.replace("k", "\u1d0b");
        output = output.replace("l", "\u029f");
        output = output.replace("m", "\u1d0d");
        output = output.replace("n", "\u0274");
        output = output.replace("o", "\u1d0f");
        output = output.replace("p", "\u1d18");
        output = output.replace("q", "\u01eb");
        output = output.replace("r", "\u0280");
        output = output.replace("s", "\u0455");
        output = output.replace("t", "\u1d1b");
        output = output.replace("u", "\u1d1c");
        output = output.replace("v", "\u1d20");
        output = output.replace("w", "\u1d21");
        output = output.replace("x", "\u0445");
        output = output.replace("y", "\u028f");
        output = output.replace("z", "\u1d22");
        return output;
    }
    
    static {
        ChatTextUtils.rand = new Random();
    }
}
