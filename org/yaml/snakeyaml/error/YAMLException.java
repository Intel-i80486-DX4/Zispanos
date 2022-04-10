//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.error;

public class YAMLException extends RuntimeException
{
    private static final long serialVersionUID = -4738336175050337570L;
    
    public YAMLException(final String message) {
        super(message);
    }
    
    public YAMLException(final Throwable cause) {
        super(cause);
    }
    
    public YAMLException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
