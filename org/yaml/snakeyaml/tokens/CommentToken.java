//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.*;

public class CommentToken extends Token
{
    public CommentToken(final Mark startMark, final Mark endMark) {
        super(startMark, endMark);
    }
    
    public Token.ID getTokenId() {
        return Token.ID.Comment;
    }
}
