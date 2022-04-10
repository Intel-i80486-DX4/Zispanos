//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.module.modules.chat;

import me.zeroeightsix.kami.module.*;
import java.util.concurrent.*;
import me.zeroeightsix.kami.event.events.*;
import me.zero.alpine.listener.*;
import me.zeroeightsix.kami.setting.*;
import java.util.function.*;
import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.*;
import java.util.regex.*;
import net.minecraft.network.play.server.*;
import java.util.*;
import java.util.stream.*;

@Info(name = "AntiSpam", category = Category.CHAT, description = "Removes spam and advertising from the chat", showOnArray = ShowOnArray.OFF)
public class AntiSpam extends Module
{
    private Setting<Page> p;
    private Setting<Boolean> discordLinks;
    private Setting<Boolean> announcers;
    private Setting<Boolean> spammers;
    private Setting<Boolean> insulters;
    private Setting<Boolean> greeters;
    private Setting<Boolean> ips;
    private Setting<Boolean> wordsLongerThen;
    private Setting<Boolean> specialCharEnding;
    private Setting<Boolean> specialCharBegin;
    private Setting<Boolean> iJustThanksTo;
    private Setting<Boolean> ownsMeAndAll;
    private Setting<Boolean> greenText;
    private Setting<Boolean> numberSuffix;
    private Setting<Boolean> numberPrefix;
    private Setting<Boolean> tradeChat;
    private Setting<Boolean> hypixelShills;
    private Setting<Boolean> ipsAgr;
    private Setting<Boolean> duplicates;
    private Setting<Integer> duplicatesTimeout;
    private Setting<Boolean> webLinks;
    private Setting<Boolean> filterOwn;
    private Setting<ShowBlocked> showBlocked;
    private ConcurrentHashMap<String, Long> messageHistory;
    @EventHandler
    public Listener<PacketEvent.Receive> listener;
    private Map<Setting<Boolean>, String[]> settingMap;
    
    public AntiSpam() {
        this.p = this.register(Settings.e("Page", Page.ONE));
        this.discordLinks = this.register(Settings.booleanBuilder("Discord Links").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.announcers = this.register(Settings.booleanBuilder("Announcers").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.spammers = this.register(Settings.booleanBuilder("Spammers").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.insulters = this.register(Settings.booleanBuilder("Insulters").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.greeters = this.register(Settings.booleanBuilder("Greeters").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.ips = this.register(Settings.booleanBuilder("Server Ips").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.wordsLongerThen = this.register(Settings.booleanBuilder("11+ long words").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.specialCharEnding = this.register(Settings.booleanBuilder("Special Ending").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.specialCharBegin = this.register(Settings.booleanBuilder("Special Begin").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.iJustThanksTo = this.register(Settings.booleanBuilder("I just...thanks to").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.ONE)).build());
        this.ownsMeAndAll = this.register(Settings.booleanBuilder("Owns Me And All").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.greenText = this.register(Settings.booleanBuilder("Green Text").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.numberSuffix = this.register(Settings.booleanBuilder("Number Ending").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.numberPrefix = this.register(Settings.booleanBuilder("Number Begin").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.tradeChat = this.register(Settings.booleanBuilder("Trade Chat").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.hypixelShills = this.register(Settings.booleanBuilder("Hypixel Shills").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.ipsAgr = this.register(Settings.booleanBuilder("Ips Aggressive").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.duplicates = this.register(Settings.booleanBuilder("Duplicates").withValue(true).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.duplicatesTimeout = this.register(Settings.integerBuilder("Duplicates Timeout").withMinimum(1).withValue(30).withMaximum(600).withVisibility(v -> this.duplicates.getValue() && this.p.getValue().equals(Page.TWO)).build());
        this.webLinks = this.register(Settings.booleanBuilder("Web Links").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.filterOwn = this.register(Settings.booleanBuilder("Filter Own").withValue(false).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.showBlocked = this.register((Setting<ShowBlocked>)Settings.enumBuilder(ShowBlocked.class).withName("Show Blocked").withValue(ShowBlocked.LOG_FILE).withVisibility(v -> this.p.getValue().equals(Page.TWO)).build());
        this.listener = (Listener<PacketEvent.Receive>)new Listener(event -> {
            if (AntiSpam.mc.player == null || this.isDisabled()) {
                return;
            }
            if (!(event.getPacket() instanceof SPacketChat)) {
                return;
            }
            final SPacketChat sPacketChat = (SPacketChat)event.getPacket();
            final Long n;
            this.messageHistory.entrySet().stream().filter(entry -> entry.getValue() < System.currentTimeMillis() - 600000L).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()).forEach(entry -> n = this.messageHistory.remove(entry.getKey()));
            if (this.isSpam(sPacketChat.getChatComponent().getUnformattedText())) {
                event.cancel();
            }
        }, new Predicate[0]);
        this.settingMap = new HashMap<Setting<Boolean>, String[]>() {
            {
                this.put(AntiSpam.this.wordsLongerThen, FilterPatterns.LONG_WORD);
                this.put(AntiSpam.this.greenText, FilterPatterns.GREEN_TEXT);
                this.put(AntiSpam.this.specialCharBegin, FilterPatterns.SPECIAL_BEGINNING);
                this.put(AntiSpam.this.specialCharEnding, FilterPatterns.SPECIAL_ENDING);
                this.put(AntiSpam.this.specialCharBegin, FilterPatterns.SPECIAL_BEGINNING);
                this.put(AntiSpam.this.ownsMeAndAll, FilterPatterns.OWNS_ME_AND_ALL);
                this.put(AntiSpam.this.iJustThanksTo, FilterPatterns.I_JUST_THANKS_TO);
                this.put(AntiSpam.this.numberSuffix, FilterPatterns.NUMBER_SUFFIX);
                this.put(AntiSpam.this.numberPrefix, FilterPatterns.NUMBER_PREFIX);
                this.put(AntiSpam.this.discordLinks, FilterPatterns.DISCORD);
                this.put(AntiSpam.this.webLinks, FilterPatterns.WEB_LINK);
                this.put(AntiSpam.this.ips, FilterPatterns.IP_ADDR);
                this.put(AntiSpam.this.ipsAgr, FilterPatterns.IP_ADDR_AGR);
                this.put(AntiSpam.this.announcers, FilterPatterns.ANNOUNCER);
                this.put(AntiSpam.this.spammers, FilterPatterns.SPAMMER);
                this.put(AntiSpam.this.insulters, FilterPatterns.INSULTER);
                this.put(AntiSpam.this.greeters, FilterPatterns.GREETER);
                this.put(AntiSpam.this.hypixelShills, FilterPatterns.HYPIXEL_SHILLS);
                this.put(AntiSpam.this.tradeChat, FilterPatterns.TRADE_CHAT);
            }
        };
    }
    
    public void onEnable() {
        this.messageHistory = new ConcurrentHashMap<String, Long>();
    }
    
    public void onDisable() {
        this.messageHistory = null;
    }
    
    private boolean isSpam(final String message) {
        final String[] OWN_MESSAGE = { "^<" + AntiSpam.mc.player.getName() + "> ", "^To .+: " };
        return (this.filterOwn.getValue() || !this.findPatterns(OWN_MESSAGE, message, false)) && this.detectSpam(this.removeUsername(message));
    }
    
    private String removeUsername(final String username) {
        return username.replaceAll("<[^>]*> ", "");
    }
    
    private boolean detectSpam(final String message) {
        for (final Map.Entry<Setting<Boolean>, String[]> entry : this.settingMap.entrySet()) {
            if (entry.getKey().getValue() && this.findPatterns(entry.getValue(), message, true)) {
                this.sendResult(entry.getKey().getName(), message);
                return true;
            }
        }
        if (this.duplicates.getValue()) {
            if (this.messageHistory == null) {
                this.messageHistory = new ConcurrentHashMap<String, Long>();
            }
            boolean isDuplicate = false;
            if (this.messageHistory.containsKey(message) && (System.currentTimeMillis() - this.messageHistory.get(message)) / 1000L < this.duplicatesTimeout.getValue()) {
                isDuplicate = true;
            }
            this.messageHistory.put(message, System.currentTimeMillis());
            if (isDuplicate) {
                if (this.showBlocked.getValue().equals(ShowBlocked.CHAT)) {
                    Command.sendChatMessage(this.getChatName() + "Duplicate: " + message);
                }
                else if (this.showBlocked.getValue().equals(ShowBlocked.LOG_FILE)) {
                    KamiMod.log.info(this.getChatName() + "Duplicate: " + message);
                }
            }
        }
        return false;
    }
    
    private boolean findPatterns(final String[] patterns, String string, final boolean removeUsername) {
        if (removeUsername) {
            string = string.replaceAll("<[^>]*> ", "");
        }
        for (final String pattern : patterns) {
            if (Pattern.compile(pattern, 2).matcher(string).find()) {
                return true;
            }
        }
        return false;
    }
    
    private void sendResult(final String name, final String message) {
        if (this.showBlocked.getValue().equals(ShowBlocked.CHAT)) {
            Command.sendChatMessage(this.getChatName() + name + ": " + message);
        }
        else if (this.showBlocked.getValue().equals(ShowBlocked.LOG_FILE)) {
            KamiMod.log.info(this.getChatName() + name + ": " + message);
        }
    }
    
    private enum Page
    {
        ONE, 
        TWO;
    }
    
    private enum ShowBlocked
    {
        NONE, 
        LOG_FILE, 
        CHAT;
    }
    
    private static class FilterPatterns
    {
        private static final String[] ANNOUNCER;
        private static final String[] SPAMMER;
        private static final String[] INSULTER;
        private static final String[] GREETER;
        private static final String[] HYPIXEL_SHILLS;
        private static final String[] DISCORD;
        private static final String[] NUMBER_SUFFIX;
        private static final String[] NUMBER_PREFIX;
        private static final String[] GREEN_TEXT;
        private static final String[] TRADE_CHAT;
        private static final String[] WEB_LINK;
        private static final String[] IP_ADDR;
        private static final String[] IP_ADDR_AGR;
        private static final String[] LONG_WORD;
        private static final String[] OWNS_ME_AND_ALL;
        private static final String[] I_JUST_THANKS_TO;
        private static final String[] SPECIAL_BEGINNING;
        private static final String[] SPECIAL_ENDING;
        
        static {
            ANNOUNCER = new String[] { "I just walked .+ feet!", "I just placed a .+!", "I just attacked .+ with a .+!", "I just dropped a .+!", "I just opened chat!", "I just opened my console!", "I just opened my GUI!", "I just went into full screen mode!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just took a screen shot!", "I just swaped hands!", "I just ducked!", "I just changed perspectives!", "I just jumped!", "I just ate a .+!", "I just crafted .+ .+!", "I just picked up a .+!", "I just smelted .+ .+!", "I just respawned!", "I just attacked .+ with my hands", "I just broke a .+!", "I recently walked .+ blocks", "I just droped a .+ called, .+!", "I just placed a block called, .+!", "Im currently breaking a block called, .+!", "I just broke a block called, .+!", "I just opened chat!", "I just opened chat and typed a slash!", "I just paused my game!", "I just opened my inventory!", "I just looked at the player list!", "I just changed perspectives, now im in .+!", "I just crouched!", "I just jumped!", "I just attacked a entity called, .+ with a .+", "Im currently eatting a peice of food called, .+!", "Im currently using a item called, .+!", "I just toggled full screen mode!", "I just took a screen shot!", "I just swaped hands and now theres a .+ in my main hand and a .+ in my off hand!", "I just used pick block on a block called, .+!", "Ra just completed his blazing ark", "Its a new day yes it is", "I just placed .+ thanks to (http:\\/\\/)?DotGod\\.CC!", "I just flew .+ meters like a butterfly thanks to (http:\\/\\/)?DotGod\\.CC!" };
            SPAMMER = new String[] { "WWE Client's spammer", "Lol get gud", "Future client is bad", "WWE > Future", "WWE > Impact", "Default Message", "IKnowImEZ is a god", "THEREALWWEFAN231 is a god", "WWE Client made by IKnowImEZ/THEREALWWEFAN231", "WWE Client was the first public client to have Path Finder/New Chunks", "WWE Client was the first public client to have color signs", "WWE Client was the first client to have Teleport Finder", "WWE Client was the first client to have Tunneller & Tunneller Back Fill" };
            INSULTER = new String[] { ".+ Download WWE utility mod, Its free!", ".+ 4b4t is da best mintscreft serber", ".+ dont abouse", ".+ you cuck", ".+ https://www.youtube.com/channel/UCJGCNPEjvsCn0FKw3zso0TA", ".+ is my step dad", ".+ again daddy!", "dont worry .+ it happens to every one", ".+ dont buy future it's crap, compared to WWE!", "What are you, fucking gay, .+?", "Did you know? .+ hates you, .+", "You are literally 10, .+", ".+ finally lost their virginity, sadly they lost it to .+... yeah, that's unfortunate.", ".+, don't be upset, it's not like anyone cares about you, fag.", ".+, see that rubbish bin over there? Get your ass in it, or I'll get .+ to whoop your ass.", ".+, may I borrow that dirt block? that guy named .+ needs it...", "Yo, .+, btfo you virgin", "Hey .+ want to play some High School RP with me and .+?", ".+ is an Archon player. Why is he on here? Fucking factions player.", "Did you know? .+ just joined The Vortex Coalition!", ".+ has successfully conducted the cactus dupe and duped a itemhand!", ".+, are you even human? You act like my dog, holy shit.", ".+, you were never loved by your family.", "Come on .+, you hurt .+'s feelings. You meany.", "Stop trying to meme .+, you can't do that. kek", ".+, .+ is gay. Don't go near him.", "Whoa .+ didn't mean to offend you, .+.", ".+ im not pvping .+, im WWE'ing .+.", "Did you know? .+ just joined The Vortex Coalition!", ".+, are you even human? You act like my dog, holy shit." };
            GREETER = new String[] { "Bye, Bye .+", "Farwell, .+", "See you next time, .+", "Catch ya later, .+", "Bye, .+", "Welcome, .+", "Hey, .+", ".+ joined the game", ".+ has joined", ".+ joined the lobby", "Welcome .+", ".+ left the game" };
            HYPIXEL_SHILLS = new String[] { "/p join", "/party join", "road to", "private games" };
            DISCORD = new String[] { "discord.gg", "discordapp.com", "discord.io", "invite.gg" };
            NUMBER_SUFFIX = new String[] { ".+\\d{3,}$" };
            NUMBER_PREFIX = new String[] { "\\d{3,}.*$" };
            GREEN_TEXT = new String[] { "^>.+$" };
            TRADE_CHAT = new String[] { "buy", "sell" };
            WEB_LINK = new String[] { "http:\\/\\/", "https:\\/\\/", "www." };
            IP_ADDR = new String[] { "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d{1,5}\\b", "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", "^(?:http(?:s)?:\\/\\/)?(?:[^\\.]+\\.)?.*\\..*\\..*$", ".*\\..*\\:\\d{1,5}$" };
            IP_ADDR_AGR = new String[] { ".*\\..*$" };
            LONG_WORD = new String[] { "\\b\\w{11,256}\\b" };
            OWNS_ME_AND_ALL = new String[] { "owns me and all" };
            I_JUST_THANKS_TO = new String[] { "i just.*thanks to" };
            SPECIAL_BEGINNING = new String[] { "^[.,/?!()\\[\\]{}<>|\\-+=\\\\]" };
            SPECIAL_ENDING = new String[] { "[/@#^()\\[\\]{}<>|\\-+=\\\\]" };
        }
    }
}
