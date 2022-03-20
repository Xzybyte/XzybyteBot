package handlers;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.user.update.GenericUserUpdateEvent;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public class GenericUserHandler {

    public void handleGuildMemberNameChange(GenericUserUpdateEvent event) {
        User user = event.getEntity();
        Date d = new Date();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        eb.setColor(Color.magenta);
        eb.setTitle(user.getName()+ " has changed their name.", null);
        eb.addField("Previous", Objects.requireNonNull(event.getOldValue()).toString() == null ? event.getUser().getName() : event.getOldValue().toString(), true);
        eb.addField("Current", Objects.requireNonNull(event.getNewValue()).toString() == null ? event.getUser().getName() : event.getNewValue().toString(), false);
        eb.setFooter("ID: " + user.getId() + " | " + d.toString(), null);
        if (eb.isValidLength()) {
            MessageEmbed build = eb.build();
            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
            if (logs != null) {
                logs.sendMessageEmbeds(build).queue();
            }
        }
    }

}
