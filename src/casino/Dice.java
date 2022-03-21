package casino;

import commands.Command;
import discord.User;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Dice implements Command {


    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        if(args.length == 3) {
            User user = Main.getInstance().getUserStorage().getUserById(event.getMember().getId());
            if(User.getCD(event.getAuthor().getId())) {
                int amount;
                int side;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage(":octagonal_sign: | **" + args[1] + "** must be a number.").queue();
                    return;
                }
                try {
                    side = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage(":octagonal_sign: | **" + args[2] + "** must be a number.").queue();
                    return;
                }
                if(side > 0 && side < 7) {
                    if(amount < 1) {
                        event.getChannel().sendMessage(":octagonal_sign: | Amount must be greater than **0**.").queue();
                    } else if (amount > 50) {
                        event.getChannel().sendMessage(":octagonal_sign: | Amount must be less than **50**.").queue();
                    } else if (amount <= user.getTokens()) {
                        User.addToCD(event.getAuthor().getId());
                        rollDie(event, user, amount, side);
                    } else {
                        event.getChannel().sendMessage(":octagonal_sign: | You don't have enough tokens to bet that amount!").queue();
                    }
                } else {
                    event.getChannel().sendMessage(":octagonal_sign: | Number must be less than **7** and greater than **0**!").queue();
                }
            } else {
                long seconds = TimeUnit.MILLISECONDS.toSeconds((User.returnCD(event.getAuthor().getId()) + 3000) - System.currentTimeMillis());
                event.getTextChannel().sendMessage("Please wait **" + seconds + "** seconds before using this.").queue();
            }
        } else {
            event.getTextChannel().sendMessage("Correct syntax: **" + Main.getInstance().getConfig().getPrefix() + "dice <amount> <number>**.").queue();
        }
    }

    @Override
    public void description(String[] args, MessageReceivedEvent event) {

    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        return true;
    }

    public void rollDie(MessageReceivedEvent event, User user, int amount, int side) {
        Random rand = new Random();
        int dice = rand.nextInt(6) + 1;
        EmbedBuilder game = new EmbedBuilder();
        game.setTitle("Casino (Dice Roll)");
        if (dice == side) {
            game.setColor(Color.BLUE);
            game.setDescription("**" + event.getAuthor().getName() + "** rolls the die and lands on..");
            game.appendDescription("\r\n\r\n**" + dice + "**");
            game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** gains **" + (amount * 5) + "** :coin: tokens.");
            user.gainTokens(amount * 5);
            user.saveUser();
            MessageEmbed build = game.build();
            event.getTextChannel().sendMessageEmbeds(build).queue();
        } else {
            game.setColor(Color.RED);
            game.setDescription("**" + event.getAuthor().getName() + "** rolls the die and lands on..");
            game.appendDescription("\r\n\r\n**" + dice + "**");
            game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** loses **" + amount + "** :coin: tokens.");
            user.gainTokens(-amount);
            user.saveUser();
            MessageEmbed build = game.build();
            event.getTextChannel().sendMessageEmbeds(build).queue();
        }
    }
}
