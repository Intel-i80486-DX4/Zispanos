//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package me.zeroeightsix.kami.command;

import me.zeroeightsix.kami.command.commands.*;
import me.zeroeightsix.kami.util.*;
import me.zeroeightsix.kami.*;
import java.util.*;

public class CommandManager
{
    private ArrayList<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        final Set<Class> classList = (Set<Class>)ClassFinder.findClasses(BindCommand.class.getPackage().getName(), (Class)Command.class);
        for (final Class s : classList) {
            if (Command.class.isAssignableFrom(s)) {
                try {
                    final Command command = s.getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
                    this.commands.add(command);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Couldn't initiate command " + s.getSimpleName() + "! Err: " + e.getClass().getSimpleName() + ", message: " + e.getMessage());
                }
            }
        }
        KamiMod.log.info("Commands initialised");
    }
    
    public void callCommand(final String command) {
        final String[] parts = command.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        final String label = parts[0].contains(" ") ? parts[0].substring(parts[0].indexOf(" ")).substring(1) : parts[0].substring(1);
        final String[] args = removeElement(parts, 0);
        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                args[i] = strip(args[i], "\"");
            }
        }
        for (final Command c : this.commands) {
            if (c.getLabel().equalsIgnoreCase(label) || c.getAliases().stream().anyMatch(alias -> alias.equalsIgnoreCase(label))) {
                c.call(parts);
                return;
            }
        }
        Command.sendChatMessage("&7Unknown command. try '&f" + Command.getCommandPrefix() + "cmds&7' for a list of commands.");
    }
    
    public static String[] removeElement(final String[] input, final int indexToDelete) {
        final List result = new LinkedList();
        for (int i = 0; i < input.length; ++i) {
            if (i != indexToDelete) {
                result.add(input[i]);
            }
        }
        return result.toArray(input);
    }
    
    private static String strip(final String str, final String key) {
        if (str.startsWith(key) && str.endsWith(key)) {
            return str.substring(key.length(), str.length() - key.length());
        }
        return str;
    }
    
    public Command getCommandByLabel(final String commandLabel) {
        for (final Command c : this.commands) {
            if (c.getLabel().equals(commandLabel)) {
                return c;
            }
        }
        return null;
    }
    
    public ArrayList<Command> getCommands() {
        return this.commands;
    }
}
