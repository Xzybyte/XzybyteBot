package commands.moderation;

import commands.Command;
import commands.CommandProcessor;
import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;

public class Help implements Command {

    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        CommandProcessor processor = Main.getInstance().getCommandProcessor();
        if (args.length >= 2) {
            if (processor.getAllCommands().get(args[1]) == null) {
                event.getTextChannel().sendMessage("That command doesn't exist!").queue();
            } else {
                if (!args[1].equals("help")) {
                    event.getTextChannel().sendMessage(processor.getAllCommands().get(args[1]).description()).queue();
                }
            }
            return;
        }
        StringBuilder builder = new StringBuilder();
        processor.getAllCommands().forEach((key, value) -> {
            if (!key.equals("help"))
                builder.append(String.format("%s%s - %s\r\n", Main.getInstance().getConfig().getPrefix(), key, value.description()));
        });
        event.getTextChannel().sendMessage("```" + builder.toString() + "```").queue();
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        return true;
    }
}
