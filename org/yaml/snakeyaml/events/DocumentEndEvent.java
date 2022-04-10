//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.*;

public final class DocumentEndEvent extends Event
{
    private final boolean explicit;
    
    public DocumentEndEvent(final Mark startMark, final Mark endMark, final boolean explicit) {
        super(startMark, endMark);
        this.explicit = explicit;
    }
    
    public boolean getExplicit() {
        return this.explicit;
    }
    
    public boolean is(final Event.ID id) {
        return Event.ID.DocumentEnd == id;
    }
}
