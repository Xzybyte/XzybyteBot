package handlers;

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

        if (self || event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (!event.getAuthor().isBot()) {
            //
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
        String message = event.getMessageId();
        MessageChannel channel = event.getChannel();

        if (log != null) {
            if (log.getChannel(channel.getId()) != null) {
                int id = 0;
                for (Message m : log.getChannel(channel.getId()).getMessages()) {
                    if (m.getId().equals(message)) {
                        if (m.getContentDisplay().equals(event.getMessage().getContentDisplay())) { // same message?
                            return;
                        }
                        Date d = new Date();
                        EmbedBuilder eb = new EmbedBuilder();
                        eb.setAuthor(m.getAuthor().getName(), m.getAuthor().getAvatarUrl(), m.getAuthor().getAvatarUrl());
                        eb.setColor(Color.blue);
                        eb.setTitle("Message edited in #" + channel.getName(), null);
                        eb.addField("Original", m.getContentDisplay(), true);
                        eb.addField("Edited", event.getMessage().getContentDisplay(), false);
                        eb.setFooter("ID: " + m.getAuthor().getId() + " | " + d.toString(), null);
                        if (eb.isValidLength()) {
                            MessageEmbed build = eb.build();
                            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
                            if (logs != null) {
                                logs.sendMessageEmbeds(build).queue();
                            }
                            log.getChannel(channel.getId()).getMessages().set(id, event.getMessage());
                            return;
                        }
                    }
                    id++;
                }
            }
        }
    }

}
