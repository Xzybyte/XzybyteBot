package commands.moderation;

import commands.Command;
import main.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Permissions;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Untimeout implements Command {

    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        if (args.length < 2) {
            event.getTextChannel().sendMessage("You must mention the user you wish to untimeout.").queue();
            return;
        }
        List<User> mentions = event.getMessage().getMentions().getUsers();
        Member memb = null;
        if (mentions.size() <= 0) {
            List<Member> list = Main.getInstance().getGuild().getMembersByName(args[1], true);
            if (list.size() > 0) {
                memb = list.get(0);
            }
        }
        User mention;
        if (mentions.size() > 0) {
            mention = event.getMessage().getMentions().getUsers().get(0);
        } else if (memb != null) {
            mention = memb.getUser();
        } else {
            event.getTextChannel().sendMessage("That user does not exist in this discord.").queue();
            return;
        }
        if (mention != null) {
            if (mention != event.getAuthor()) {
                Member mem = Main.getInstance().getGuild().getMember(mention);
                if (mem != null) {
                    EnumSet<Permission> memPerms = mem.getPermissions();
                    boolean memCanMute = memPerms.toString().contains(Permissions.MODERATE_MEMBERS.name());
                    boolean memIsAdmin = memPerms.toString().contains(Permissions.ADMINISTRATOR.name());

                    if (!mem.isTimedOut()) {
                        event.getTextChannel().sendMessage("User is not timed out.").queue();
                        return;
                    }

                    if (memIsAdmin || memCanMute) {
                        event.getTextChannel().sendMessage("You cannot timeout this User.").queue();
                    } else {
                        mem.removeTimeout().queue();
                        event.getTextChannel().sendMessage("Timeout cleared on ``" + mention.getName() + "#" + mention.getDiscriminator() + "``.").queue();
                    }
                }
            } else {
                event.getTextChannel().sendMessage("You cannot timeout yourself.").queue();
            }
        }
    }

    @Override
    public String description() {
        return "Untimeout a user from the server. Usage: " + Main.getInstance().getConfig().getPrefix() + "untimeout <@mention>";
    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        boolean canTimeout = perms.toString().contains(Permissions.MODERATE_MEMBERS.name());
        boolean isAdmin = perms.toString().contains(Permissions.ADMINISTRATOR.name());
        return canTimeout || isAdmin;
    }

}
