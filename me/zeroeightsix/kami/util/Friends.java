//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.util;

import me.zeroeightsix.kami.setting.*;
import com.google.common.base.*;
import java.util.*;
import java.util.regex.*;
import com.google.gson.*;
import java.io.*;
import java.net.*;

public class Friends
{
    public static final Friends INSTANCE;
    public static Setting<ArrayList<Friend>> friends;
    
    private Friends() {
    }
    
    public static void initFriends() {
        Friends.friends = Settings.custom("Friends", new ArrayList<Friend>(), new FriendListConverter()).buildAndRegister("friends");
    }
    
    public static boolean isFriend(final String name) {
        return Friends.friends.getValue().stream().anyMatch(friend -> friend.username.equalsIgnoreCase(name));
    }
    
    static {
        INSTANCE = new Friends();
    }
    
    public static class Friend
    {
        String username;
        UUID uuid;
        
        public Friend(final String username, final UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }
        
        public String getUsername() {
            return this.username;
        }
    }
    
    public static class FriendListConverter extends Converter<ArrayList<Friend>, JsonElement>
    {
        protected JsonElement doForward(final ArrayList<Friend> list) {
            final StringBuilder present = new StringBuilder();
            for (final Friend friend : list) {
                present.append(String.format("%s;%s$", friend.username, friend.uuid.toString()));
            }
            return (JsonElement)new JsonPrimitive(present.toString());
        }
        
        protected ArrayList<Friend> doBackward(final JsonElement jsonElement) {
            final String v = jsonElement.getAsString();
            final String[] pairs = v.split(Pattern.quote("$"));
            final ArrayList<Friend> friends = new ArrayList<Friend>();
            for (final String pair : pairs) {
                try {
                    final String[] split = pair.split(";");
                    final String username = split[0];
                    final UUID uuid = UUID.fromString(split[1]);
                    friends.add(new Friend(this.getUsernameByUUID(uuid, username), uuid));
                }
                catch (Exception ex) {}
            }
            return friends;
        }
        
        private String getUsernameByUUID(final UUID uuid, final String saved) {
            final String src = getSource("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.toString());
            if (src == null || src.isEmpty()) {
                return saved;
            }
            try {
                final JsonElement object = new JsonParser().parse(src);
                return object.getAsJsonObject().get("name").getAsString();
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println(src);
                return saved;
            }
        }
        
        private static String getSource(final String link) {
            try {
                final URL u = new URL(link);
                final URLConnection con = u.openConnection();
                final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                final StringBuilder buffer = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    buffer.append(inputLine);
                }
                in.close();
                return buffer.toString();
            }
            catch (Exception e) {
                return null;
            }
        }
    }
}
