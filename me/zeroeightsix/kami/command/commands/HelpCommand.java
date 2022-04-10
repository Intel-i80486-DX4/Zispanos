//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command.commands;

import me.zeroeightsix.kami.command.*;
import me.zeroeightsix.kami.command.syntax.*;
import me.zeroeightsix.kami.module.*;
import java.util.*;

public class HelpCommand extends Command
{
    private static final Subject[] subjects;
    private static String subjectsList;
    
    public HelpCommand() {
        super("help", new SyntaxChunk[0], new String[] { "?" });
        this.setDescription("Delivers help on certain subjects. Use &f" + Command.getCommandPrefix() + "help subjects&7 for a list.");
    }
    
    public void call(final String[] args) {
        final String commandPrefix = Command.getCommandPrefix();
        if (args[0] == null) {
            Command.sendStringChatMessage(new String[] { "KAMI Blue 0.5beta", "&7Press &r" + ModuleManager.getModuleByName("ClickGUI").getBindName() + "&7 to open GUI", "&7see &bhttps://blue.bella.wtf&7 for a full version of the faq", commandPrefix + "description&7 to see the description of a module", commandPrefix + "commands&7 to view all available commands", commandPrefix + "bind <module> <key>&7 to bind mods", commandPrefix + "prefix <prefix>&r to change the command prefix.", commandPrefix + "help &7<bind|subjects:[subject]>&r for more help." });
        }
        else {
            final String subject3 = args[0];
            if (subject3.equals("subjects")) {
                Command.sendChatMessage("Subjects: " + HelpCommand.subjectsList);
            }
            else if (subject3.equals("bind")) {
                Command.sendChatMessage("You can also use &7.bind&r modifiers on to allow modules to be bound to keybinds with modifiers, e.g &7ctrl + shift + w or ctrl + c.&r");
                Command.sendChatMessage("You can unbind modules with backspace in the GUI or by running &7.bind <module> none&r");
            }
            else {
                final String[] names;
                final int length;
                int i = 0;
                String name;
                final String s;
                final Subject subject4 = Arrays.stream(HelpCommand.subjects).filter(subject2 -> {
                    names = subject2.names;
                    length = names.length;
                    while (i < length) {
                        name = names[i];
                        if (name.equalsIgnoreCase(s)) {
                            return true;
                        }
                        else {
                            ++i;
                        }
                    }
                    return false;
                }).findFirst().orElse(null);
                if (subject4 == null) {
                    Command.sendChatMessage("No help found for &b" + args[0]);
                    return;
                }
                Command.sendStringChatMessage(subject4.info);
            }
        }
    }
    
    static {
        subjects = new Subject[] { new Subject(new String[] { "type", "int", "boolean", "double", "float" }, new String[] { "Every module has a value, and that value is always of a certain &btype.\n", "These types are displayed in kami as the ones java use. They mean the following:", "&bboolean&r: Enabled or not. Values &3true/false", "&bfloat&r: A number with a decimal point", "&bdouble&r: Like a float, but a more accurate decimal point", "&bint&r: A number with no decimal point" }) };
        HelpCommand.subjectsList = "";
        for (final Subject subject : HelpCommand.subjects) {
            HelpCommand.subjectsList = HelpCommand.subjectsList + subject.names[0] + ", ";
        }
        HelpCommand.subjectsList = HelpCommand.subjectsList.substring(0, HelpCommand.subjectsList.length() - 2);
    }
    
    private static class Subject
    {
        String[] names;
        String[] info;
        
        public Subject(final String[] names, final String[] info) {
            this.names = names;
            this.info = info;
        }
    }
}
