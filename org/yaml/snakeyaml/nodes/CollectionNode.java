//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.nodes;

import org.yaml.snakeyaml.*;
import org.yaml.snakeyaml.error.*;
import java.util.*;

public abstract class CollectionNode<T> extends Node
{
    private DumperOptions.FlowStyle flowStyle;
    
    public CollectionNode(final Tag tag, final Mark startMark, final Mark endMark, final DumperOptions.FlowStyle flowStyle) {
        super(tag, startMark, endMark);
        this.setFlowStyle(flowStyle);
    }
    
    @Deprecated
    public CollectionNode(final Tag tag, final Mark startMark, final Mark endMark, final Boolean flowStyle) {
        this(tag, startMark, endMark, DumperOptions.FlowStyle.fromBoolean(flowStyle));
    }
    
    public abstract List<T> getValue();
    
    public DumperOptions.FlowStyle getFlowStyle() {
        return this.flowStyle;
    }
    
    public void setFlowStyle(final DumperOptions.FlowStyle flowStyle) {
        if (flowStyle == null) {
            throw new NullPointerException("Flow style must be provided.");
        }
        this.flowStyle = flowStyle;
    }
    
    public void setEndMark(final Mark endMark) {
        this.endMark = endMark;
    }
}
