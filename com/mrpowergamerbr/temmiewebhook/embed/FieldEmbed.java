//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package com.mrpowergamerbr.temmiewebhook.embed;

import java.beans.*;

public class FieldEmbed
{
    String name;
    String value;
    boolean inline;
    
    public static FieldEmbedBuilder builder() {
        return new FieldEmbedBuilder();
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public boolean isInline() {
        return this.inline;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public void setInline(final boolean inline) {
        this.inline = inline;
    }
    
    @ConstructorProperties({ "name", "value", "inline" })
    public FieldEmbed(final String name, final String value, final boolean inline) {
        this.name = name;
        this.value = value;
        this.inline = inline;
    }
    
    public FieldEmbed() {
    }
    
    public static class FieldEmbedBuilder
    {
        private String name;
        private String value;
        private boolean inline;
        
        FieldEmbedBuilder() {
        }
        
        public FieldEmbedBuilder name(final String name) {
            this.name = name;
            return this;
        }
        
        public FieldEmbedBuilder value(final String value) {
            this.value = value;
            return this;
        }
        
        public FieldEmbedBuilder inline(final boolean inline) {
            this.inline = inline;
            return this;
        }
        
        public FieldEmbed build() {
            return new FieldEmbed(this.name, this.value, this.inline);
        }
        
        @Override
        public String toString() {
            return "FieldEmbed.FieldEmbedBuilder(name=" + this.name + ", value=" + this.value + ", inline=" + this.inline + ")";
        }
    }
}
