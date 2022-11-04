package handlers;

import commands.CommandProcessor;
import discord.User;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import utils.Log;

import java.awt.*;
import java.util.Date;

public class GenericMessageHandler {

    private Log log;

    public GenericMessageHandler() {
        this.log = new Log();
    }

    public void handleReceivedMessage(MessageReceivedEvent event) {
        boolean self = event.getAuthor().getId().equals(event.getJDA().getSelfUser().getId());
        Message message = event.getMessage();
        String msg = message.getContentDisplay();
        MessageChannel channel = event.getChannel();
        Main main = Main.getInstance();

        if (self || event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (!event.getAuthor().isBot()) {
            if (msg.isEmpty()) {
                return;
            }
            if (main.getUserStorage().getUserById(event.getMember().getId()) == null) {
                User user = User.LoadUser(event.getMember().getId());
                if (user != null) {
                    main.getUserStorage().addUser(user);
                } else {
                    User.InsertUser(event.getMember().getId());
                }
            }
            main.getCommandProcessor().handleCommand(main.getCommandProcessor().parseCommand(msg, event));
            main.getGameManager().processText(msg, event, main.getUserStorage().getUserById(event.getMember().getId()));
        }
        if (log != null) {
            if (log.getChannel(channel.getId()) != null) {
                log.getChannel(channel.getId()).addMessage(message);
            } else {
                log.addChannel(channel.getId(), message);
            }
        }
    }

    public void handleDeletedMessage(MessageDeleteEvent event) {
        String message = event.getMessageId();
        MessageChannel channel = event.getChannel();

        if (log != null) {
            if (log.getChannel(channel.getId()) != null) {
                if (log.getChannel(channel.getId()).getMessage(message) != null) {
                    Message m = log.getChannel(channel.getId()).getMessage(message);
                    Date d = new Date();
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setAuthor(m.getAuthor().getName(), m.getAuthor().getAvatarUrl(), m.getAuthor().getAvatarUrl());
                    eb.setColor(Color.red);
                    eb.setTitle("Message sent by " + m.getAuthor().getName() + " deleted in #" + channel.getName(), null);
                    eb.setDescription(m.getContentDisplay());
                    eb.setFooter("ID: " + m.getAuthor().getId() + " | " + d.toString(), null);
                    if (eb.isValidLength()) {
                        MessageEmbed build = eb.build();
                        TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
                        if (logs != null) {
                            logs.sendMessageEmbeds(build).queue();
                        }
                    }
                }
            }
        }
    }

    public void handleUpdatedMessage(MessageUpdateEvent event) {
        String mId = event.getMessageId();
        Message m = event.getMessage();
        MessageChannel channel = event.getChannel();

        if (log != null) {
            if (log.getChannel(channel.getId()) != null) {
                Message old = log.getChannel(channel.getId()).getMessage(mId);
                if (old == null) {
                    return;
                }
                int id = log.getChannel(channel.getId()).getMessages().indexOf(old);
                Date d = new Date();
                EmbedBuilder eb = new EmbedBuilder();
                eb.setAuthor(m.getAuthor().getName(), m.getAuthor().getAvatarUrl(), m.getAuthor().getAvatarUrl());
                eb.setColor(Color.blue);
                eb.setTitle("Message edited in #" + channel.getName(), null);
                eb.addField("Original", old.getContentDisplay(), true);
                eb.addField("Edited", event.getMessage().getContentDisplay(), false);
                eb.setFooter("ID: " + m.getAuthor().getId() + " | " + d.toString(), null);
                if (eb.isValidLength()) {
                    MessageEmbed build = eb.build();
                    TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
                    if (logs != null) {
                        logs.sendMessageEmbeds(build).queue();
                    }
                    log.getChannel(channel.getId()).getMessages().set(id, event.getMessage());
                }
            }
        }
    }

    public Log getLog() {
        return log;
    }

}
