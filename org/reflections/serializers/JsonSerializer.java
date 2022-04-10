//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package org.reflections.serializers;

import org.reflections.*;
import org.reflections.util.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.io.*;
import com.google.gson.*;

public class JsonSerializer implements Serializer
{
    private Gson gson;
    
    @Override
    public Reflections read(final InputStream inputStream) {
        return (Reflections)this.getGson().fromJson((Reader)new InputStreamReader(inputStream), (Class)Reflections.class);
    }
    
    @Override
    public File save(final Reflections reflections, final String filename) {
        try {
            final File file = Utils.prepareFile(filename);
            Files.write(file.toPath(), this.toString(reflections).getBytes(Charset.defaultCharset()), new OpenOption[0]);
            return file;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String toString(final Reflections reflections) {
        return this.getGson().toJson((Object)reflections);
    }
    
    private Gson getGson() {
        if (this.gson == null) {
            this.gson = new GsonBuilder().setPrettyPrinting().create();
        }
        return this.gson;
    }
}
