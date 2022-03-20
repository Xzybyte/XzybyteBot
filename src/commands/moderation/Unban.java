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

public class Unban implements Command {

    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        if (args.length < 2) {
            event.getTextChannel().sendMessage("You must mention the user you wish to unban.").queue();
            return;
        }
        String id = args[1];

        Member mem = Main.getInstance().getGuild().getMemberById(id);
        if (mem != null) {
            event.getTextChannel().sendMessage("Failed to unban ``" + id + "``. User isn't banned!").queue();
            return;
        }
        event.getGuild().unban(id).queue(
                success -> event.getTextChannel().sendMessage("Successfully unbanned ``" + id + "``.").queue(),
                error -> event.getTextChannel().sendMessage("Failed to unban ``" + id + "``.").queue()
        );
    }

    @Override
    public void description(String[] args, MessageReceivedEvent event) {

    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        boolean canBan = perms.toString().contains(Permissions.BAN_MEMBERS.name());
        boolean isAdmin = perms.toString().contains(Permissions.ADMINISTRATOR.name());
        return canBan || isAdmin;
    }

}
