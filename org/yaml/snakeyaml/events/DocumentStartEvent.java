//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.*;
import java.util.*;
import org.yaml.snakeyaml.error.*;

public final class DocumentStartEvent extends Event
{
    private final boolean explicit;
    private final DumperOptions.Version version;
    private final Map<String, String> tags;
    
    public DocumentStartEvent(final Mark startMark, final Mark endMark, final boolean explicit, final DumperOptions.Version version, final Map<String, String> tags) {
        super(startMark, endMark);
        this.explicit = explicit;
        this.version = version;
        this.tags = tags;
    }
    
    public boolean getExplicit() {
        return this.explicit;
    }
    
    public DumperOptions.Version getVersion() {
        return this.version;
    }
    
    public Map<String, String> getTags() {
        return this.tags;
    }
    
    public boolean is(final Event.ID id) {
        return Event.ID.DocumentStart == id;
    }
}
