package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.EnumSet;

public interface Command {

    void runCommand(String[] args, MessageReceivedEvent event);
    void description(String[] args, MessageReceivedEvent event);
    boolean canUse(EnumSet<Permission> perms);

}
