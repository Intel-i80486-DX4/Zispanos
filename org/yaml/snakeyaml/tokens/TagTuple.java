//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.tokens;

public final class TagTuple
{
    private final String handle;
    private final String suffix;
    
    public TagTuple(final String handle, final String suffix) {
        if (suffix == null) {
            throw new NullPointerException("Suffix must be provided.");
        }
        this.handle = handle;
        this.suffix = suffix;
    }
    
    public String getHandle() {
        return this.handle;
    }
    
    public String getSuffix() {
        return this.suffix;
    }
}
