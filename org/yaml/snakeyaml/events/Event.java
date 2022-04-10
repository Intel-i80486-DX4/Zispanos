//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.error.*;

public abstract class Event
{
    private final Mark startMark;
    private final Mark endMark;
    
    public Event(final Mark startMark, final Mark endMark) {
        this.startMark = startMark;
        this.endMark = endMark;
    }
    
    @Override
    public String toString() {
        return "<" + this.getClass().getName() + "(" + this.getArguments() + ")>";
    }
    
    public Mark getStartMark() {
        return this.startMark;
    }
    
    public Mark getEndMark() {
        return this.endMark;
    }
    
    protected String getArguments() {
        return "";
    }
    
    public abstract boolean is(final ID p0);
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Event && this.toString().equals(obj.toString());
    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public enum ID
    {
        Alias, 
        DocumentEnd, 
        DocumentStart, 
        MappingEnd, 
        MappingStart, 
        Scalar, 
        SequenceEnd, 
        SequenceStart, 
        StreamEnd, 
        StreamStart;
    }
}
