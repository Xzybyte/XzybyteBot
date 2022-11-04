package commands.moderation;

import commands.Command;
import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.pagination.MessagePaginationAction;
import utils.Permissions;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Prune implements Command {

    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        User user = null;
        int num;
        try {
            num = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("**"+ args[1] + "** must be a number.").queue();
            return;
        }
        if (args.length >= 3) {
            List<User> mentions = event.getMessage().getMentions().getUsers();
            if(mentions.size() > 0) {
                user = event.getMessage().getMentions().getUsers().get(0);
            } else {
                event.getChannel().sendMessage("You did not mention the user you wish to prune.").queue();
                return;
            }
        }
        TextChannel ch = event.getTextChannel();
        MessagePaginationAction msgs = ch.getIterableHistory();
        List<Message> remove = new ArrayList<>();
        if (user != null) {
            for (Message message : msgs) {
                if(remove.size() >= num) {
                    break;
                } else {
                    if(message.getAuthor().equals(user)) {
                        remove.add(message);
                    }
                }
            }
        } else if(num >= 2 && num <= 100) {
            for (Message message : msgs) {
                if(remove.size() >= num) {
                    break;
                } else {
                    remove.add(message);
                }
            }
        } else {
            event.getChannel().sendMessage("Amount must be between 2 and 100.").queue();
            return;
        }
        if(remove.size() > 0) {
            event.getTextChannel().deleteMessageById(event.getMessage().getId()).complete();

            ch.getHistory().retrievePast(num).queue((List<Message> mess) -> ch.deleteMessages(mess).queue(success ->
                    ch.sendMessage("Deleted **" + remove.size() + "** messages.").queue(message ->
                            message.delete().queueAfter(2, TimeUnit.SECONDS))));
        }
    }

    @Override
    public String description() {
        return "Prune messages from the chat. Usage: " + Main.getInstance().getConfig().getPrefix() + "prune <amount> <@mention> or !prune <amount>";
    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        boolean canPrune = perms.toString().contains(Permissions.MESSAGE_MANAGE.name());
        boolean isAdmin = perms.toString().contains(Permissions.ADMINISTRATOR.name());
        return canPrune || isAdmin;
    }

}
