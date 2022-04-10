//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.extensions.compactnotation;

import java.util.*;

public class CompactData
{
    private String prefix;
    private List<String> arguments;
    private Map<String, String> properties;
    
    public CompactData(final String prefix) {
        this.arguments = new ArrayList<String>();
        this.properties = new HashMap<String, String>();
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return this.prefix;
    }
    
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public List<String> getArguments() {
        return this.arguments;
    }
    
    @Override
    public String toString() {
        return "CompactData: " + this.prefix + " " + this.properties;
    }
}
