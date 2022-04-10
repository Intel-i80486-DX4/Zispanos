//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook.embed;

import java.beans.*;

public class FooterEmbed
{
    String text;
    String icon_url;
    String proxy_icon_url;
    
    public static FooterEmbedBuilder builder() {
        return new FooterEmbedBuilder();
    }
    
    public String getText() {
        return this.text;
    }
    
    public String getIcon_url() {
        return this.icon_url;
    }
    
    public String getProxy_icon_url() {
        return this.proxy_icon_url;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    public void setIcon_url(final String icon_url) {
        this.icon_url = icon_url;
    }
    
    public void setProxy_icon_url(final String proxy_icon_url) {
        this.proxy_icon_url = proxy_icon_url;
    }
    
    @ConstructorProperties({ "text", "icon_url", "proxy_icon_url" })
    public FooterEmbed(final String text, final String icon_url, final String proxy_icon_url) {
        this.text = text;
        this.icon_url = icon_url;
        this.proxy_icon_url = proxy_icon_url;
    }
    
    public FooterEmbed() {
    }
    
    public static class FooterEmbedBuilder
    {
        private String text;
        private String icon_url;
        private String proxy_icon_url;
        
        FooterEmbedBuilder() {
        }
        
        public FooterEmbedBuilder text(final String text) {
            this.text = text;
            return this;
        }
        
        public FooterEmbedBuilder icon_url(final String icon_url) {
            this.icon_url = icon_url;
            return this;
        }
        
        public FooterEmbedBuilder proxy_icon_url(final String proxy_icon_url) {
            this.proxy_icon_url = proxy_icon_url;
            return this;
        }
        
        public FooterEmbed build() {
            return new FooterEmbed(this.text, this.icon_url, this.proxy_icon_url);
        }
        
        @Override
        public String toString() {
            return "FooterEmbed.FooterEmbedBuilder(text=" + this.text + ", icon_url=" + this.icon_url + ", proxy_icon_url=" + this.proxy_icon_url + ")";
        }
    }
}
