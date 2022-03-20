package handlers;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;

import java.awt.*;
import java.util.Date;

public class GenericGuildHandler {

    public void handleGuildMemberBan(GuildBanEvent event) {
        User user = event.getUser();
        Date d = new Date();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        eb.setColor(Color.green);
        eb.setTitle(user.getName()+ " has been banned.", null);
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
