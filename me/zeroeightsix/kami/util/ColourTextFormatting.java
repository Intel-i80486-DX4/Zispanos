//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import net.minecraft.util.text.*;
import java.util.*;
import java.awt.*;

public class ColourTextFormatting
{
    public static Map<TextFormatting, ColourEnum> colourEnumMap;
    public static Map<ColourCode, TextFormatting> toTextMap;
    
    static {
        ColourTextFormatting.colourEnumMap = new HashMap<TextFormatting, ColourEnum>() {
            {
                this.put(TextFormatting.BLACK, ColourEnum.BLACK);
                this.put(TextFormatting.DARK_BLUE, ColourEnum.DARK_BLUE);
                this.put(TextFormatting.DARK_GREEN, ColourEnum.DARK_GREEN);
                this.put(TextFormatting.DARK_AQUA, ColourEnum.DARK_AQUA);
                this.put(TextFormatting.DARK_RED, ColourEnum.DARK_RED);
                this.put(TextFormatting.DARK_PURPLE, ColourEnum.DARK_PURPLE);
                this.put(TextFormatting.GOLD, ColourEnum.GOLD);
                this.put(TextFormatting.GRAY, ColourEnum.GRAY);
                this.put(TextFormatting.DARK_GRAY, ColourEnum.DARK_GRAY);
                this.put(TextFormatting.BLUE, ColourEnum.BLUE);
                this.put(TextFormatting.GREEN, ColourEnum.GREEN);
                this.put(TextFormatting.AQUA, ColourEnum.AQUA);
                this.put(TextFormatting.RED, ColourEnum.RED);
                this.put(TextFormatting.LIGHT_PURPLE, ColourEnum.LIGHT_PURPLE);
                this.put(TextFormatting.YELLOW, ColourEnum.YELLOW);
                this.put(TextFormatting.WHITE, ColourEnum.WHITE);
            }
        };
        ColourTextFormatting.toTextMap = new HashMap<ColourCode, TextFormatting>() {
            {
                this.put(ColourCode.BLACK, TextFormatting.BLACK);
                this.put(ColourCode.DARK_BLUE, TextFormatting.DARK_BLUE);
                this.put(ColourCode.DARK_GREEN, TextFormatting.DARK_GREEN);
                this.put(ColourCode.DARK_AQUA, TextFormatting.DARK_AQUA);
                this.put(ColourCode.DARK_RED, TextFormatting.DARK_RED);
                this.put(ColourCode.DARK_PURPLE, TextFormatting.DARK_PURPLE);
                this.put(ColourCode.GOLD, TextFormatting.GOLD);
                this.put(ColourCode.GRAY, TextFormatting.GRAY);
                this.put(ColourCode.DARK_GRAY, TextFormatting.DARK_GRAY);
                this.put(ColourCode.BLUE, TextFormatting.BLUE);
                this.put(ColourCode.GREEN, TextFormatting.GREEN);
                this.put(ColourCode.AQUA, TextFormatting.AQUA);
                this.put(ColourCode.RED, TextFormatting.RED);
                this.put(ColourCode.LIGHT_PURPLE, TextFormatting.LIGHT_PURPLE);
                this.put(ColourCode.YELLOW, TextFormatting.YELLOW);
                this.put(ColourCode.WHITE, TextFormatting.WHITE);
            }
        };
    }
    
    public enum ColourEnum
    {
        BLACK(new Color(0, 0, 0)), 
        DARK_BLUE(new Color(0, 0, 170)), 
        DARK_GREEN(new Color(0, 170, 0)), 
        DARK_AQUA(new Color(0, 170, 170)), 
        DARK_RED(new Color(170, 0, 0)), 
        DARK_PURPLE(new Color(170, 0, 170)), 
        GOLD(new Color(255, 170, 0)), 
        GRAY(new Color(170, 170, 170)), 
        DARK_GRAY(new Color(85, 85, 85)), 
        BLUE(new Color(85, 85, 255)), 
        GREEN(new Color(85, 255, 85)), 
        AQUA(new Color(85, 225, 225)), 
        RED(new Color(255, 85, 85)), 
        LIGHT_PURPLE(new Color(255, 85, 255)), 
        YELLOW(new Color(255, 255, 85)), 
        WHITE(new Color(255, 255, 255));
        
        public Color colorLocal;
        
        private ColourEnum(final Color colorLocal) {
            this.colorLocal = colorLocal;
        }
    }
    
    public enum ColourCode
    {
        BLACK, 
        DARK_BLUE, 
        DARK_GREEN, 
        DARK_AQUA, 
        DARK_RED, 
        DARK_PURPLE, 
        GOLD, 
        GRAY, 
        DARK_GRAY, 
        BLUE, 
        GREEN, 
        AQUA, 
        RED, 
        LIGHT_PURPLE, 
        YELLOW, 
        WHITE;
    }
}
