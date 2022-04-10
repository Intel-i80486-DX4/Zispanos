//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook;

import com.google.gson.annotations.*;

public class Response
{
    boolean global;
    String message;
    @SerializedName("retry_after")
    int retryAfter;
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public int getRetryAfter() {
        return this.retryAfter;
    }
}
