//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.parsers.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.client.*;
import net.minecraft.client.network.*;
import com.mojang.util.*;
import com.google.gson.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new ChunkBuilder().append("mode", true, new EnumParser(new String[] { "add", "del" })).append("name").build(), new String[] { "f" });
        this.setDescription("Add someone as your friend!");
    }
    
    public void call(final String[] args) {
        if (args[0] == null) {
            final Friends instance = Friends.INSTANCE;
            if (Friends.friends.getValue().isEmpty()) {
                Command.sendChatMessage("You currently don't have any friends added. &bfriend add <name>&r to add one.");
                return;
            }
            String f = "";
            final Friends instance2 = Friends.INSTANCE;
            for (final Friends.Friend friend2 : Friends.friends.getValue()) {
                f = f + friend2.getUsername() + ", ";
            }
            f = f.substring(0, f.length() - 2);
            Command.sendChatMessage("Your friends: " + f);
        }
        else {
            if (args[1] == null) {
                Command.sendChatMessage(String.format(Friends.isFriend(args[0]) ? "Yes, %s is your friend." : "No, %s isn't a friend of yours.", args[0]));
                return;
            }
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("new")) {
                if (Friends.isFriend(args[1])) {
                    Command.sendChatMessage("That player is already your friend.");
                    return;
                }
                final Friends.Friend f2;
                Friends instance3;
                new Thread(() -> {
                    f2 = this.getFriendByName(args[1]);
                    if (f2 == null) {
                        Command.sendChatMessage("Failed to find UUID of " + args[1]);
                    }
                    else {
                        instance3 = Friends.INSTANCE;
                        Friends.friends.getValue().add(f2);
                        Command.sendChatMessage("&b" + f2.getUsername() + "&r has been friended.");
                    }
                }).start();
            }
            else {
                if (!args[0].equalsIgnoreCase("del") && !args[0].equalsIgnoreCase("remove") && !args[0].equalsIgnoreCase("delete")) {
                    Command.sendChatMessage("Please specify either &6add&r or &6remove");
                    return;
                }
                if (!Friends.isFriend(args[1])) {
                    Command.sendChatMessage("That player isn't your friend.");
                    return;
                }
                final Friends instance4 = Friends.INSTANCE;
                final Friends.Friend friend3 = Friends.friends.getValue().stream().filter(friend1 -> friend1.getUsername().equalsIgnoreCase(args[1])).findFirst().get();
                final Friends instance5 = Friends.INSTANCE;
                Friends.friends.getValue().remove(friend3);
                Command.sendChatMessage("&b" + friend3.getUsername() + "&r has been unfriended.");
            }
        }
    }
    
    public Friends.Friend getFriendByName(final String input) {
        final ArrayList<NetworkPlayerInfo> infoMap = new ArrayList<NetworkPlayerInfo>(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        final NetworkPlayerInfo profile = infoMap.stream().filter(networkPlayerInfo -> networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(input)).findFirst().orElse(null);
        if (profile == null) {
            Command.sendChatMessage("Player isn't online. Looking up UUID..");
            final String s = requestIDs("[\"" + input + "\"]");
            if (s == null || s.isEmpty()) {
                Command.sendChatMessage("Couldn't find player ID. Are you connected to the internet? (0)");
            }
            else {
                final JsonElement element = new JsonParser().parse(s);
                if (element.getAsJsonArray().size() == 0) {
                    Command.sendChatMessage("Couldn't find player ID. (1)");
                }
                else {
                    try {
                        final String id = element.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                        final String username = element.getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString();
                        final Friends.Friend friend = new Friends.Friend(username, UUIDTypeAdapter.fromString(id));
                        return friend;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Command.sendChatMessage("Couldn't find player ID. (2)");
                    }
                }
            }
            return null;
        }
        final Friends.Friend f = new Friends.Friend(profile.getGameProfile().getName(), profile.getGameProfile().getId());
        return f;
    }
    
    private static String requestIDs(final String data) {
        try {
            final String query = "https://api.mojang.com/profiles/minecraft";
            final String json = data;
            final URL url = new URL(query);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            final OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();
            final InputStream in = new BufferedInputStream(conn.getInputStream());
            final String res = convertStreamToString(in);
            in.close();
            conn.disconnect();
            return res;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private static String convertStreamToString(final InputStream is) {
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        final String r = s.hasNext() ? s.next() : "/";
        return r;
    }
}
