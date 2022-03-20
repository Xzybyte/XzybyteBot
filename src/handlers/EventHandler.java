package handlers;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter {

    private GenericMessageHandler genericMessageHandler;
    private GenericGuildMemberHandler genericGuildMemberHandler;
    private GenericGuildHandler genericGuildHandler;
    private GenericUserHandler genericUserHandler;

    public void registerHandlers() {
        this.genericMessageHandler = new GenericMessageHandler();
        genericMessageHandler.getLog().LoadLogs();
        this.genericGuildMemberHandler = new GenericGuildMemberHandler();
        this.genericGuildHandler = new GenericGuildHandler();
        this.genericUserHandler = new GenericUserHandler();
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) {
        if (event instanceof MessageReceivedEvent) {
            genericMessageHandler.handleReceivedMessage((MessageReceivedEvent) event);
        } else if (event instanceof MessageDeleteEvent) {
            genericMessageHandler.handleDeletedMessage((MessageDeleteEvent) event);
        } else if (event instanceof MessageUpdateEvent) {
            genericMessageHandler.handleUpdatedMessage((MessageUpdateEvent) event);
        }
    }

    @Override
    public void onGenericGuildMember(GenericGuildMemberEvent event) {
        if (event instanceof GuildMemberJoinEvent) {
            genericGuildMemberHandler.handleGuildMemberJoin((GuildMemberJoinEvent) event);
        } else if (event instanceof GuildMemberUpdateNicknameEvent) {
            genericGuildMemberHandler.handleGuildMemberNickChange((GuildMemberUpdateNicknameEvent) event);
        } else if (event instanceof GuildMemberRoleAddEvent) {
            genericGuildMemberHandler.handleGuildMemberRoleAdd((GuildMemberRoleAddEvent) event);
        } else if (event instanceof GuildMemberRoleRemoveEvent) {
            genericGuildMemberHandler.handleGuildMemberRoleRemove((GuildMemberRoleRemoveEvent) event);
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        genericGuildMemberHandler.handleGuildMemberLeave(event);
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        genericGuildHandler.handleGuildMemberBan(event);
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        genericUserHandler.handleGuildMemberNameChange(event);
    }

    @Override
    public void onReady(ReadyEvent event) {
        registerHandlers();
        System.out.println("Bot is now online.");
    }
}
