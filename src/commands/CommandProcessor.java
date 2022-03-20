package commands;

import commands.moderation.*;
import main.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;

public class CommandProcessor {

    private HashMap<String, Command> commands = new HashMap<>();

    public void loadAll() {
        commands.put("ban", new Ban());
        commands.put("unban", new Unban());
        commands.put("kick", new Kick());
        commands.put("timeout", new Timeout());
        commands.put("untimeout", new Untimeout());
        commands.put("prune", new Prune());
    }

    public CommandInfo parseCommand(String text, MessageReceivedEvent event) {
        String[] sp = text.split(" ");
        char head = sp[0].charAt(0);
        sp[0] = sp[0].substring(1).toLowerCase();
        String command = sp[0];
        return new CommandInfo(sp, head, command, event);
    }

    public void handleCommand(CommandInfo commandInfo) {
        if (!commands.containsKey(commandInfo.command)) {
            return;
        }

        if (commandInfo.head == Main.getInstance().getConfig().getPrefix().charAt(0)) {
            if (commandInfo.event.getMember() != null) {
                if (!commands.get(commandInfo.command).canUse(Main.getInstance().getGuild().getSelfMember().getPermissions())) {
                    commandInfo.event.getTextChannel().sendMessage("I am missing the right permissions to use this command!").queue();
                    return;
                }
                if (commands.get(commandInfo.command).canUse(commandInfo.event.getMember().getPermissions())) {
                    commands.get(commandInfo.command).runCommand(commandInfo.split, commandInfo.event);
                }
            }
        }
    }

    public class CommandInfo {

        private String[] split;
        private char head;
        private String command;
        private MessageReceivedEvent event;

        public CommandInfo(String[] split, char head, String command, MessageReceivedEvent event) {
            this.split = split;
            this.head = head;
            this.command = command;
            this.event = event;
        }

    }

}
