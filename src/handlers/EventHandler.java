package handlers;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter {

    private GenericMessageHandler genericMessageHandler;
    private GenericGuildMemberHandler genericGuildMemberHandler;

    public void registerHandlers() {
        this.genericMessageHandler = new GenericMessageHandler();
        this.genericGuildMemberHandler = new GenericGuildMemberHandler();
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
    public void onReady(ReadyEvent event) {
        registerHandlers();
        System.out.println("Bot is now online.");
    }
}
