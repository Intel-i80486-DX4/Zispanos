//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook.embed;

import java.beans.*;

public class ImageEmbed
{
    String url;
    String proxy_url;
    int height;
    int width;
    
    public static ImageEmbedBuilder builder() {
        return new ImageEmbedBuilder();
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getProxy_url() {
        return this.proxy_url;
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
    
    public void setProxy_url(final String proxy_url) {
        this.proxy_url = proxy_url;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    @ConstructorProperties({ "url", "proxy_url", "height", "width" })
    public ImageEmbed(final String url, final String proxy_url, final int height, final int width) {
        this.url = url;
        this.proxy_url = proxy_url;
        this.height = height;
        this.width = width;
    }
    
    public ImageEmbed() {
    }
    
    public static class ImageEmbedBuilder
    {
        private String url;
        private String proxy_url;
        private int height;
        private int width;
        
        ImageEmbedBuilder() {
        }
        
        public ImageEmbedBuilder url(final String url) {
            this.url = url;
            return this;
        }
        
        public ImageEmbedBuilder proxy_url(final String proxy_url) {
            this.proxy_url = proxy_url;
            return this;
        }
        
        public ImageEmbedBuilder height(final int height) {
            this.height = height;
            return this;
        }
        
        public ImageEmbedBuilder width(final int width) {
            this.width = width;
            return this;
        }
        
        public ImageEmbed build() {
            return new ImageEmbed(this.url, this.proxy_url, this.height, this.width);
        }
        
        @Override
        public String toString() {
            return "ImageEmbed.ImageEmbedBuilder(url=" + this.url + ", proxy_url=" + this.proxy_url + ", height=" + this.height + ", width=" + this.width + ")";
        }
    }
}
