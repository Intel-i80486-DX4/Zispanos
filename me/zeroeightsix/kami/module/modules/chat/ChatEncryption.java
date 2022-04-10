//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.nio.*;
import java.util.stream.*;
import java.util.*;
import java.util.function.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.*;
import java.util.regex.*;
import net.minecraft.network.play.client.*;
import me.zeroeightsix.kami.command.*;
import net.minecraft.util.text.*;

@Info(name = "ChatEncryption", description = "Encrypts and decrypts chat messages (Delimiter %)", category = Category.HIDDEN, showOnArray = ShowOnArray.OFF)
public class ChatEncryption extends Module
{
    private Setting<EncryptionMode> mode;
    private Setting<Integer> key;
    private Setting<Boolean> delim;
    private final Pattern CHAT_PATTERN;
    private static final char[] ORIGIN_CHARS;
    @EventHandler
    private Listener<PacketEvent.Send> sendListener;
    @EventHandler
    private Listener<PacketEvent.Receive> receiveListener;
    
    public ChatEncryption() {
        this.mode = this.register(Settings.e("Mode", EncryptionMode.SHUFFLE));
        this.key = this.register(Settings.i("Key", 6));
        this.delim = this.register(Settings.b("Delimiter", true));
        this.CHAT_PATTERN = Pattern.compile("<.*?> ");
        this.sendListener = (Listener<PacketEvent.Send>)new Listener(event -> {
            if (event.getPacket() instanceof CPacketChatMessage) {
                String s = ((CPacketChatMessage)event.getPacket()).getMessage();
                if (this.delim.getValue()) {
                    if (!s.startsWith("%")) {
                        return;
                    }
                    s = s.substring(1);
                }
                final StringBuilder builder = new StringBuilder();
                switch (this.mode.getValue()) {
                    case SHUFFLE: {
                        builder.append(this.shuffle(this.key.getValue(), s));
                        builder.append("\ud83d\ude4d");
                        break;
                    }
                    case SHIFT: {
                        s.chars().forEachOrdered(value -> builder.append((char)(value + (ChatAllowedCharacters.isAllowedCharacter((char)(value + this.key.getValue())) ? this.key.getValue() : 0))));
                        builder.append("\ud83d\ude48");
                        break;
                    }
                }
                s = builder.toString();
                if (s.length() > 256) {
                    Command.sendChatMessage("Encrypted message length was too long, couldn't send!");
                    event.cancel();
                    return;
                }
                ((CPacketChatMessage)event.getPacket()).message = s;
            }
        }, new Predicate[0]);
        this.receiveListener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (event.getPacket() instanceof SPacketChat) {
                String s = ((SPacketChat)event.getPacket()).getChatComponent().getUnformattedText();
                final Matcher matcher = this.CHAT_PATTERN.matcher(s);
                String username = "unnamed";
                if (matcher.find()) {
                    username = matcher.group();
                    username = username.substring(1, username.length() - 2);
                    s = matcher.replaceFirst("");
                }
                final StringBuilder builder = new StringBuilder();
                switch (this.mode.getValue()) {
                    case SHUFFLE: {
                        if (!s.endsWith("\ud83d\ude4d")) {
                            return;
                        }
                        s = s.substring(0, s.length() - 2);
                        builder.append(this.unshuffle(this.key.getValue(), s));
                        break;
                    }
                    case SHIFT: {
                        if (!s.endsWith("\ud83d\ude48")) {
                            return;
                        }
                        s = s.substring(0, s.length() - 2);
                        s.chars().forEachOrdered(value -> builder.append((char)(value + (ChatAllowedCharacters.isAllowedCharacter((char)value) ? (-this.key.getValue()) : 0))));
                        break;
                    }
                }
                ((SPacketChat)event.getPacket()).chatComponent = (ITextComponent)new TextComponentString("§c" + username + '§' + "c: " + builder.toString());
            }
        }, new Predicate[0]);
    }
    
    private Map<Character, Character> generateShuffleMap(final int seed) {
        final Random r = new Random(seed);
        final List<Character> characters = CharBuffer.wrap(ChatEncryption.ORIGIN_CHARS).chars().mapToObj(value -> Character.valueOf((char)value)).collect((Collector<? super Object, ?, List<Character>>)Collectors.toList());
        final List<Character> counter = new ArrayList<Character>(characters);
        Collections.shuffle(counter, r);
        final Map<Character, Character> map = new LinkedHashMap<Character, Character>();
        for (int i = 0; i < characters.size(); ++i) {
            map.put(characters.get(i), counter.get(i));
        }
        return map;
    }
    
    private String shuffle(final int seed, final String input) {
        final Map<Character, Character> s = this.generateShuffleMap(seed);
        final StringBuilder builder = new StringBuilder();
        this.swapCharacters(input, s, builder);
        return builder.toString();
    }
    
    private String unshuffle(final int seed, final String input) {
        final Map<Character, Character> s = this.generateShuffleMap(seed);
        final StringBuilder builder = new StringBuilder();
        this.swapCharacters(input, reverseMap(s), builder);
        return builder.toString();
    }
    
    private void swapCharacters(final String input, final Map<Character, Character> s, final StringBuilder builder) {
        final char c;
        CharBuffer.wrap(input.toCharArray()).chars().forEachOrdered(value -> {
            c = (char)value;
            if (s.containsKey(c)) {
                builder.append(s.get(c));
            }
            else {
                builder.append(c);
            }
        });
    }
    
    private static <K, V> Map<V, K> reverseMap(final Map<K, V> map) {
        return map.entrySet().stream().collect(Collectors.toMap((Function<? super Object, ? extends V>)Map.Entry::getValue, (Function<? super Object, ? extends K>)Map.Entry::getKey));
    }
    
    static {
        ORIGIN_CHARS = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '_', '/', ';', '=', '?', '+', 'µ', '£', '*', '^', '\u00f9', '$', '!', '{', '}', '\'', '\"', '|', '&' };
    }
    
    private enum EncryptionMode
    {
        SHUFFLE, 
        SHIFT;
    }
}
