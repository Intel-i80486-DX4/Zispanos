//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.tokens.*;

public interface Scanner
{
    boolean checkToken(final Token.ID... p0);
    
    Token peekToken();
    
    Token getToken();
}
