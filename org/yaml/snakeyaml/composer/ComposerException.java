//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.composer;

import org.yaml.snakeyaml.error.*;

public class ComposerException extends MarkedYAMLException
{
    private static final long serialVersionUID = 2146314636913113935L;
    
    protected ComposerException(final String context, final Mark contextMark, final String problem, final Mark problemMark) {
        super(context, contextMark, problem, problemMark);
    }
}
