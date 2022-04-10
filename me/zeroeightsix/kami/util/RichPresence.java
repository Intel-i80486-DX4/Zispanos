//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import javax.net.ssl.*;
import java.net.*;
import com.google.gson.*;
import java.io.*;
import me.zeroeightsix.kami.*;

public class RichPresence
{
    public static RichPresence INSTANCE;
    public CustomUser[] customUsers;
    
    public RichPresence() {
        RichPresence.INSTANCE = this;
        try {
            final HttpsURLConnection connection = (HttpsURLConnection)new URL("https://raw.githubusercontent.com/S-B99/kamiblue/assets/assets/donators.json").openConnection();
            connection.connect();
            this.customUsers = (CustomUser[])new Gson().fromJson((Reader)new InputStreamReader(connection.getInputStream()), (Class)CustomUser[].class);
            connection.disconnect();
        }
        catch (Exception e) {
            KamiMod.log.error("Failed to load donators");
            e.printStackTrace();
        }
    }
    
    public static class CustomUser
    {
        public String uuid;
        public String type;
    }
}
