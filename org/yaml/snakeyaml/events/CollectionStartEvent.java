//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.events;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.error.*;

public abstract class CollectionStartEvent extends NodeEvent
{
    private final String tag;
    private final boolean implicit;
    private final DumperOptions.FlowStyle flowStyle;
    
    public CollectionStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark, final Mark endMark, final DumperOptions.FlowStyle flowStyle) {
        super(anchor, startMark, endMark);
        this.tag = tag;
        this.implicit = implicit;
        if (flowStyle == null) {
            throw new NullPointerException("Flow style must be provided.");
        }
        this.flowStyle = flowStyle;
    }
    
    @Deprecated
    public CollectionStartEvent(final String anchor, final String tag, final boolean implicit, final Mark startMark, final Mark endMark, final Boolean flowStyle) {
        this(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
    }
    
    public String getTag() {
        return this.tag;
    }
    
    public boolean getImplicit() {
        return this.implicit;
    }
    
    public DumperOptions.FlowStyle getFlowStyle() {
        return this.flowStyle;
    }
    
    protected String getArguments() {
        return super.getArguments() + ", tag=" + this.tag + ", implicit=" + this.implicit;
    }
    
    public boolean isFlow() {
        return DumperOptions.FlowStyle.FLOW == this.flowStyle;
    }
}