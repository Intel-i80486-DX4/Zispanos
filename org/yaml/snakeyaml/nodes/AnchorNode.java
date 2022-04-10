//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.nodes;

public class AnchorNode extends Node
{
    private Node realNode;
    
    public AnchorNode(final Node realNode) {
        super(realNode.getTag(), realNode.getStartMark(), realNode.getEndMark());
        this.realNode = realNode;
    }
    
    @Override
    public NodeId getNodeId() {
        return NodeId.anchor;
    }
    
    public Node getRealNode() {
        return this.realNode;
    }
}
