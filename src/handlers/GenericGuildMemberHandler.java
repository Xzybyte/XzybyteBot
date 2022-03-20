package handlers;

import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;

import java.awt.*;
import java.time.Month;
import java.util.Date;

public class GenericGuildMemberHandler {

    public void handleGuildMemberJoin(GuildMemberJoinEvent event) {
        Member mem = event.getMember();
        Date d = new Date();
        int year = mem.getUser().getTimeCreated().getYear();
        Month month = mem.getUser().getTimeCreated().getMonth();
        int day = mem.getUser().getTimeCreated().getDayOfMonth();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(mem.getUser().getName(), mem.getUser().getAvatarUrl(), mem.getUser().getAvatarUrl());
        eb.setColor(Color.green);
        eb.setTitle(mem.getUser().getName() + " has joined the server. ", null);
        eb.setDescription("User created: " + month.name() + " / " + day + " / " + year);
        eb.setFooter("ID: " + mem.getUser().getId() + " | " + d.toString(), null);

        String roleId = Main.getInstance().getConfig().getDefaultRoleID();
        if (roleId != null && !roleId.isEmpty()) {
            Role role = Main.getInstance().getGuild().getRoleById(roleId);
            if (role != null) {
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
            }
        }
        TextChannel welcome = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getWelcomeChannel());
        if (welcome != null) {
            welcome.sendMessage(mem.getAsMention() + "" + Main.getInstance().getConfig().getWelcomeMessage()).queue();
        }
        if (eb.isValidLength()) {
            MessageEmbed build = eb.build();
            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
            if (logs != null) {
                logs.sendMessageEmbeds(build).queue();
            }
        }
    }

    public void handleGuildMemberLeave(GuildMemberRemoveEvent event) {
        Member mem = event.getMember();
        if (mem != null) {
            Date d = new Date();
            EmbedBuilder eb = new EmbedBuilder();
            eb.setAuthor(mem.getUser().getName(), mem.getUser().getAvatarUrl(), mem.getUser().getAvatarUrl());
            eb.setColor(Color.green);
            eb.setTitle(mem.getUser().getName() + " has left the server or was kicked.", null);
            eb.setFooter("ID: " + mem.getUser().getId() + " | " + d.toString(), null);
            if (eb.isValidLength()) {
                MessageEmbed build = eb.build();
                TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
                if (logs != null) {
                    logs.sendMessageEmbeds(build).queue();
                }
            }
        }
    }

    public void handleGuildMemberNickChange(GuildMemberUpdateNicknameEvent event) {
        Member mem = event.getMember();
        Date d = new Date();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(mem.getUser().getName(), mem.getUser().getAvatarUrl(), mem.getUser().getAvatarUrl());
        eb.setColor(Color.magenta);
        eb.setTitle(mem.getUser().getName()+ " has changed their nickname.", null);
        eb.addField("Previous", event.getOldNickname() == null ? event.getUser().getName() : event.getOldNickname(), true);
        eb.addField("Current", event.getNewNickname() == null ? event.getUser().getName() : event.getNewNickname(), false);
        eb.setFooter("ID: " + mem.getUser().getId() + " | " + d.toString(), null);
        if (eb.isValidLength()) {
            MessageEmbed build = eb.build();
            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
            if (logs != null) {
                logs.sendMessageEmbeds(build).queue();
            }
        }
    }

    public void handleGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        Member mem = event.getMember();
        int roleSize = event.getRoles().size();
        Date d = new Date();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(mem.getUser().getName(), mem.getUser().getAvatarUrl(), mem.getUser().getAvatarUrl());
        eb.setColor(Color.yellow);
        eb.setTitle(roleSize + " role" + (roleSize > 1 ? "s" : "") + " ha" + (roleSize > 1 ? "ve" : "s") + " been added to "  + mem.getUser().getName(), null);
        StringBuilder text = new StringBuilder();
        int roles = 0;
        for (Role r : event.getRoles()) {
            roles++;
            text.append(r.getName());
            if (roles != roleSize) {
                text.append(", ");
            }
        }
        eb.setDescription(text.toString());
        eb.setFooter("ID: " + mem.getUser().getId() + " | " + d.toString(), null);
        if (eb.isValidLength()) {
            MessageEmbed build = eb.build();
            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
            if (logs != null) {
                logs.sendMessageEmbeds(build).queue();
            }
        }
    }

    public void handleGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        Member mem = event.getMember();
        int roleSize = event.getRoles().size();
        Date d = new Date();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor(mem.getUser().getName(), mem.getUser().getAvatarUrl(), mem.getUser().getAvatarUrl());
        eb.setColor(Color.yellow);
        eb.setTitle(roleSize + " role" + (roleSize > 1 ? "s" : "") + " ha" + (roleSize > 1 ? "ve" : "s") + " been removed from "  + mem.getUser().getName(), null);
        StringBuilder text = new StringBuilder();
        int roles = 0;
        for (Role r : event.getRoles()) {
            roles++;
            text.append(r.getName());
            if (roles != roleSize) {
                text.append(", ");
            }
        }
        eb.setDescription(text.toString());
        eb.setFooter("ID: " + mem.getUser().getId() + " | " + d.toString(), null);
        if (eb.isValidLength()) {
            MessageEmbed build = eb.build();
            TextChannel logs = Main.getInstance().getGuild().getTextChannelById(Main.getInstance().getConfig().getLogsChannel());
            if (logs != null) {
                logs.sendMessageEmbeds(build).queue();
            }
        }
    }

}
