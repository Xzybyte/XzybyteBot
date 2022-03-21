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

public class RPS implements Command {
    @Override
    public void runCommand(String[] args, MessageReceivedEvent event) {
        if(args.length == 3) {
            User user = Main.getInstance().getUserStorage().getUserById(event.getMember().getId());
            if(User.getCD(event.getAuthor().getId())) {
                int amount;
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    event.getChannel().sendMessage(":octagonal_sign: | **" + args[1] + "** must be a number.").queue();
                    return;
                }
                if(args[2].equalsIgnoreCase("rock") || args[2].equalsIgnoreCase("paper") || args[2].equalsIgnoreCase("scissors")) {
                    if(amount < 1) {
                        event.getChannel().sendMessage(":octagonal_sign: | Amount must be greater than **0**.").queue();
                    } else if (amount > 50) {
                        event.getChannel().sendMessage(":octagonal_sign: | Amount must be less than **50**.").queue();
                    } else if (amount <= user.getTokens()) {
                        User.addToCD(event.getAuthor().getId());
                        chooseHand(event, user, amount, args[2].toLowerCase());
                    } else {
                        event.getChannel().sendMessage(":octagonal_sign: | You don't have enough tokens to bet that amount!").queue();
                    }
                } else {
                    event.getTextChannel().sendMessage("You must enter **rock**, **paper** or **scissors**.").queue();
                }
            } else {
                long seconds = TimeUnit.MILLISECONDS.toSeconds((User.returnCD(event.getAuthor().getId()) + 3000) - System.currentTimeMillis());
                event.getTextChannel().sendMessage("Please wait **" + seconds + "** seconds before using this.").queue();
            }
        } else {
            event.getTextChannel().sendMessage("Correct syntax: **" + Main.getInstance().getConfig().getPrefix() + "rps <amount> <rock | paper | scissors>**.").queue();
        }
    }

    @Override
    public void description(String[] args, MessageReceivedEvent event) {

    }

    @Override
    public boolean canUse(EnumSet<Permission> perms) {
        return true;
    }

    public void chooseHand(MessageReceivedEvent event, User user, int amount, String side) {
        Random rand = new Random();
        int hand = rand.nextInt(3);
        int winnings = amount * 2;
        EmbedBuilder game = new EmbedBuilder();
        game.setTitle("Casino (Rock Paper Scissors)");
        switch (hand) {
            case 0:
                switch (side) {
                    case "rock":
                        game.setColor(Color.WHITE);
                        game.appendDescription("\r\n**" + event.getAuthor().getName() + "** chooses: **ROCK**.");
                        game.appendDescription("\r\n\r\nAgainst: **ROCK**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** ties and gains nothing.");
                        break;
                    case "paper":
                        game.setColor(Color.BLUE);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses: **PAPER**.");
                        game.appendDescription("\r\n\r\nAgainst: **ROCK**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** gains **" + (amount * 2) + "** :coin: tokens.");
                        user.gainTokens(winnings);
                        break;
                    case "scissors":
                        game.setColor(Color.RED);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **SCISSORS**.");
                        game.appendDescription("\r\n\r\nAgainst: **ROCK**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** loses **" + amount + "** :coin: tokens.");
                        user.gainTokens(-amount);
                        break;
                }
                break;
            case 1:
                switch (side) {
                    case "rock":
                        game.setColor(Color.RED);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **ROCK**.");
                        game.appendDescription("\r\n\r\nAgainst: **PAPER**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** loses **" + amount + "** :coin: tokens.");
                        user.gainTokens(-amount);
                        break;
                    case "paper":
                        game.setColor(Color.WHITE);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **PAPER**.");
                        game.appendDescription("\r\n\r\nAgainst: **PAPER**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** ties and gains nothing.");
                        break;
                    case "scissors":
                        game.setColor(Color.BLUE);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **SCISSORS**.");
                        game.appendDescription("\r\n\r\nAgainst: **PAPER**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** gains **" + (amount * 2) + "** :coin: tokens.");
                        user.gainTokens(winnings);
                        break;
                }
                break;
            case 2:
                switch (side) {
                    case "rock":
                        game.setColor(Color.BLUE);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **ROCK**.");
                        game.appendDescription("\r\n\r\nAgainst: **SCISSORS**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** gains **" + (amount * 2) + "** :coin: tokens.");
                        user.gainTokens(winnings);
                        break;
                    case "paper":
                        game.setColor(Color.RED);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **PAPER**.");
                        game.appendDescription("\r\n\r\nAgainst: **SCISSORS**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** loses **" + amount + "** :coin: tokens.");
                        user.gainTokens(-amount);
                        break;
                    case "scissors":
                        game.setColor(Color.WHITE);
                        game.appendDescription("**" + event.getAuthor().getName() + "** chooses **SCISSORS**.");
                        game.appendDescription("\r\n\r\nAgainst: **SCISSORS**.");
                        game.appendDescription("\r\n\r\n**" + event.getAuthor().getName() + "** ties and gains nothing.");
                        break;
                }
                break;
            default:
                break;
        }
        user.saveUser();
        MessageEmbed build = game.build();
        event.getTextChannel().sendMessageEmbeds(build).queue();
    }
}
