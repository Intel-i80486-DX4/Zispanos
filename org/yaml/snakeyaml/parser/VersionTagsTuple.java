//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.*;
import java.util.*;

class VersionTagsTuple
{
    private DumperOptions.Version version;
    private Map<String, String> tags;
    
    public VersionTagsTuple(final DumperOptions.Version version, final Map<String, String> tags) {
        this.version = version;
        this.tags = tags;
    }
    
    public DumperOptions.Version getVersion() {
        return this.version;
    }
    
    public Map<String, String> getTags() {
        return this.tags;
    }
    
    @Override
    public String toString() {
        return String.format("VersionTagsTuple<%s, %s>", this.version, this.tags);
    }
}
