//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public final class TagToken extends Token
{
    private final TagTuple value;
    
    public TagToken(final TagTuple value, final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
        this.value = value;
    }
    
    public TagTuple getValue() {
        return this.value;
    }
    
    @Override
    public ID getTokenId() {
        return ID.Tag;
    }
}
