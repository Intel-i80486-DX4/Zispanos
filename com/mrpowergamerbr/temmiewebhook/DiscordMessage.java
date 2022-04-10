//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook;

import com.google.gson.annotations.*;
import java.util.*;
import java.beans.*;

public class DiscordMessage
{
    String username;
    String content;
    @SerializedName("avatar_url")
    String avatarUrl;
    @SerializedName("tts")
    boolean textToSpeech;
    List<DiscordEmbed> embeds;
    
    public DiscordMessage() {
        this.embeds = new ArrayList<DiscordEmbed>();
    }
    
    public DiscordMessage(final String username, final String content, final String avatar_url) {
        this(username, content, avatar_url, false);
    }
    
    public DiscordMessage(final String username, final String content, final String avatar_url, final boolean tts) {
        this.embeds = new ArrayList<DiscordEmbed>();
        this.setUsername(username);
        this.setContent(content);
        this.setAvatarUrl(avatar_url);
        this.setTextToSpeech(tts);
    }
    
    public void setUsername(final String username) {
        if (username != null) {
            this.username = username.substring(0, Math.min(31, username.length()));
        }
        else {
            this.username = null;
        }
    }
    
    public static DiscordMessageBuilder builder() {
        return new DiscordMessageBuilder();
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    
    public boolean isTextToSpeech() {
        return this.textToSpeech;
    }
    
    public List<DiscordEmbed> getEmbeds() {
        return this.embeds;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public void setTextToSpeech(final boolean textToSpeech) {
        this.textToSpeech = textToSpeech;
    }
    
    public void setEmbeds(final List<DiscordEmbed> embeds) {
        this.embeds = embeds;
    }
    
    @ConstructorProperties({ "username", "content", "avatarUrl", "textToSpeech", "embeds" })
    public DiscordMessage(final String username, final String content, final String avatarUrl, final boolean textToSpeech, final List<DiscordEmbed> embeds) {
        this.embeds = new ArrayList<DiscordEmbed>();
        this.username = username;
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.textToSpeech = textToSpeech;
        this.embeds = embeds;
    }
    
    public static class DiscordMessageBuilder
    {
        private String username;
        private String content;
        private String avatarUrl;
        private boolean textToSpeech;
        List<DiscordEmbed> embeds;
        
        public DiscordMessageBuilder embed(final DiscordEmbed embed) {
            this.embeds.add(embed);
            return this;
        }
        
        DiscordMessageBuilder() {
            this.embeds = new ArrayList<DiscordEmbed>();
        }
        
        public DiscordMessageBuilder username(final String username) {
            this.username = username;
            return this;
        }
        
        public DiscordMessageBuilder content(final String content) {
            this.content = content;
            return this;
        }
        
        public DiscordMessageBuilder avatarUrl(final String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }
        
        public DiscordMessageBuilder textToSpeech(final boolean textToSpeech) {
            this.textToSpeech = textToSpeech;
            return this;
        }
        
        public DiscordMessageBuilder embeds(final List<DiscordEmbed> embeds) {
            this.embeds = embeds;
            return this;
        }
        
        public DiscordMessage build() {
            return new DiscordMessage(this.username, this.content, this.avatarUrl, this.textToSpeech, this.embeds);
        }
        
        @Override
        public String toString() {
            return "DiscordMessage.DiscordMessageBuilder(username=" + this.username + ", content=" + this.content + ", avatarUrl=" + this.avatarUrl + ", textToSpeech=" + this.textToSpeech + ", embeds=" + this.embeds + ")";
        }
    }
}
