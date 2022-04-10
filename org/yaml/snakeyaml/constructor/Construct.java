//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.constructor;

import org.yaml.snakeyaml.nodes.*;

public interface Construct
{
    Object construct(final Node p0);
    
    void construct2ndStep(final Node p0, final Object p1);
}
