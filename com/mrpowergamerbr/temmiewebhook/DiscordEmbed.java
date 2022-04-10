//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook;

import com.mrpowergamerbr.temmiewebhook.embed.*;
import java.util.*;
import java.beans.*;

public class DiscordEmbed
{
    String title;
    String type;
    String description;
    String url;
    String timestamp;
    int color;
    FooterEmbed footer;
    ImageEmbed image;
    ThumbnailEmbed thumbnail;
    VideoEmbed video;
    ProviderEmbed provider;
    AuthorEmbed author;
    List<FieldEmbed> fields;
    
    public DiscordEmbed() {
        this.fields = new ArrayList<FieldEmbed>();
    }
    
    public DiscordEmbed(final String title, final String description) {
        this(title, description, null);
    }
    
    public DiscordEmbed(final String title, final String description, final String url) {
        this.fields = new ArrayList<FieldEmbed>();
        this.setTitle(title);
        this.setDescription(description);
        this.setUrl(url);
    }
    
    public static DiscordMessage toDiscordMessage(final DiscordEmbed embed, final String username, final String avatarUrl) {
        final DiscordMessage dm = DiscordMessage.builder().username(username).avatarUrl(avatarUrl).content("").embed(embed).build();
        return dm;
    }
    
    public DiscordMessage toDiscordMessage(final String username, final String avatarUrl) {
        return toDiscordMessage(this, username, avatarUrl);
    }
    
    public static DiscordEmbedBuilder builder() {
        return new DiscordEmbedBuilder();
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public String getTimestamp() {
        return this.timestamp;
    }
    
    public int getColor() {
        return this.color;
    }
    
    public FooterEmbed getFooter() {
        return this.footer;
    }
    
    public ImageEmbed getImage() {
        return this.image;
    }
    
    public ThumbnailEmbed getThumbnail() {
        return this.thumbnail;
    }
    
    public VideoEmbed getVideo() {
        return this.video;
    }
    
    public ProviderEmbed getProvider() {
        return this.provider;
    }
    
    public AuthorEmbed getAuthor() {
        return this.author;
    }
    
    public List<FieldEmbed> getFields() {
        return this.fields;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setUrl(final String url) {
        this.url = url;
    }
    
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setColor(final int color) {
        this.color = color;
    }
    
    public void setFooter(final FooterEmbed footer) {
        this.footer = footer;
    }
    
    public void setImage(final ImageEmbed image) {
        this.image = image;
    }
    
    public void setThumbnail(final ThumbnailEmbed thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public void setVideo(final VideoEmbed video) {
        this.video = video;
    }
    
    public void setProvider(final ProviderEmbed provider) {
        this.provider = provider;
    }
    
    public void setAuthor(final AuthorEmbed author) {
        this.author = author;
    }
    
    public void setFields(final List<FieldEmbed> fields) {
        this.fields = fields;
    }
    
    @ConstructorProperties({ "title", "type", "description", "url", "timestamp", "color", "footer", "image", "thumbnail", "video", "provider", "author", "fields" })
    public DiscordEmbed(final String title, final String type, final String description, final String url, final String timestamp, final int color, final FooterEmbed footer, final ImageEmbed image, final ThumbnailEmbed thumbnail, final VideoEmbed video, final ProviderEmbed provider, final AuthorEmbed author, final List<FieldEmbed> fields) {
        this.fields = new ArrayList<FieldEmbed>();
        this.title = title;
        this.type = type;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
        this.color = color;
        this.footer = footer;
        this.image = image;
        this.thumbnail = thumbnail;
        this.video = video;
        this.provider = provider;
        this.author = author;
        this.fields = fields;
    }
    
    public static class DiscordEmbedBuilder
    {
        private String title;
        private String type;
        private String description;
        private String url;
        private String timestamp;
        private int color;
        private FooterEmbed footer;
        private ImageEmbed image;
        private ThumbnailEmbed thumbnail;
        private VideoEmbed video;
        private ProviderEmbed provider;
        private AuthorEmbed author;
        List<FieldEmbed> fields;
        
        public DiscordEmbedBuilder field(final FieldEmbed field) {
            this.fields.add(field);
            return this;
        }
        
        DiscordEmbedBuilder() {
            this.fields = new ArrayList<FieldEmbed>();
        }
        
        public DiscordEmbedBuilder title(final String title) {
            this.title = title;
            return this;
        }
        
        public DiscordEmbedBuilder type(final String type) {
            this.type = type;
            return this;
        }
        
        public DiscordEmbedBuilder description(final String description) {
            this.description = description;
            return this;
        }
        
        public DiscordEmbedBuilder url(final String url) {
            this.url = url;
            return this;
        }
        
        public DiscordEmbedBuilder timestamp(final String timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public DiscordEmbedBuilder color(final int color) {
            this.color = color;
            return this;
        }
        
        public DiscordEmbedBuilder footer(final FooterEmbed footer) {
            this.footer = footer;
            return this;
        }
        
        public DiscordEmbedBuilder image(final ImageEmbed image) {
            this.image = image;
            return this;
        }
        
        public DiscordEmbedBuilder thumbnail(final ThumbnailEmbed thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }
        
        public DiscordEmbedBuilder video(final VideoEmbed video) {
            this.video = video;
            return this;
        }
        
        public DiscordEmbedBuilder provider(final ProviderEmbed provider) {
            this.provider = provider;
            return this;
        }
        
        public DiscordEmbedBuilder author(final AuthorEmbed author) {
            this.author = author;
            return this;
        }
        
        public DiscordEmbedBuilder fields(final List<FieldEmbed> fields) {
            this.fields = fields;
            return this;
        }
        
        public DiscordEmbed build() {
            return new DiscordEmbed(this.title, this.type, this.description, this.url, this.timestamp, this.color, this.footer, this.image, this.thumbnail, this.video, this.provider, this.author, this.fields);
        }
        
        @Override
        public String toString() {
            return "DiscordEmbed.DiscordEmbedBuilder(title=" + this.title + ", type=" + this.type + ", description=" + this.description + ", url=" + this.url + ", timestamp=" + this.timestamp + ", color=" + this.color + ", footer=" + this.footer + ", image=" + this.image + ", thumbnail=" + this.thumbnail + ", video=" + this.video + ", provider=" + this.provider + ", author=" + this.author + ", fields=" + this.fields + ")";
        }
    }
}
