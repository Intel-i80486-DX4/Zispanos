//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook.embed;

import java.beans.*;

public class VideoEmbed
{
    String url;
    int height;
    int width;
    
    public static VideoEmbedBuilder builder() {
        return new VideoEmbedBuilder();
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    @ConstructorProperties({ "url", "height", "width" })
    public VideoEmbed(final String url, final int height, final int width) {
        this.url = url;
        this.height = height;
        this.width = width;
    }
    
    public VideoEmbed() {
    }
    
    public static class VideoEmbedBuilder
    {
        private String url;
        private int height;
        private int width;
        
        VideoEmbedBuilder() {
        }
        
        public VideoEmbedBuilder url(final String url) {
            this.url = url;
            return this;
        }
        
        public VideoEmbedBuilder height(final int height) {
            this.height = height;
            return this;
        }
        
        public VideoEmbedBuilder width(final int width) {
            this.width = width;
            return this;
        }
        
        public VideoEmbed build() {
            return new VideoEmbed(this.url, this.height, this.width);
        }
        
        @Override
        public String toString() {
            return "VideoEmbed.VideoEmbedBuilder(url=" + this.url + ", height=" + this.height + ", width=" + this.width + ")";
        }
    }
}
