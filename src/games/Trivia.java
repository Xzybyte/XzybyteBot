package games;

import discord.User;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Config;

import java.util.Random;

public class Trivia {

    private String question;
    private String answer;
    private String logo;
    private int tokenPrize = 15;
    public String[][] qna = {
            {"A person able to use both hands with equal skill is called what?", "Ambidextrous"},
            {"How many US Supreme Court justices are there? (Put your answer in the numerical form)", "9"}};

    public void startTrivia() {
        GameManager gameManager = Main.getInstance().getGameManager();
        Config config = Main.getInstance().getConfig();

        Random rand = new Random();
        int index = rand.nextInt(qna.length);
        question = qna[index][0];
        answer = qna[index][1];
        EmbedBuilder eb = new EmbedBuilder();
        Member bot = Main.getInstance().getGuild().getSelfMember();

        Member prevWinner = null;
        String w = gameManager.getWinner();
        if (!w.isEmpty()) {
            prevWinner = Main.getInstance().getGuild().getMemberById(w);
        }

        eb.setAuthor("Xzybyte", bot.getAvatarUrl(), bot.getAvatarUrl());
        eb.setColor(Main.hex2Rgb("#e5f442"));
        eb.setDescription("__**Trivia**__");
        eb.appendDescription("\r\n\r\n :bulb: | Q: " + question);
        eb.appendDescription("\r\n\r\nSubmit your answer by typing it in the chat.");
        eb.appendDescription("\r\n**Previous winner**: " + (prevWinner == null ? "" : prevWinner.getAsMention()));
        MessageEmbed build = eb.build();

        TextChannel trivChannel = Main.getInstance().getGuild().getTextChannelById(config.getTriviaChannel());
        if (trivChannel != null) {
            trivChannel.sendMessageEmbeds(build).queue();
        }
    }

    public void checkAnswer(String ans, MessageReceivedEvent event, User user) {
        GameManager gameManager = Main.getInstance().getGameManager();
        Config config = Main.getInstance().getConfig();
        if(ans.equalsIgnoreCase(answer)) {
            int token = tokenPrize;
            EmbedBuilder eb = new EmbedBuilder();
            Member bot = Main.getInstance().getGuild().getSelfMember();
            eb.setAuthor("Xzybyte", bot.getAvatarUrl(), bot.getAvatarUrl());
            eb.setColor(Main.hex2Rgb("#e5f442"));
            eb.setDescription("__**Trivia**__");
            eb.appendDescription("\r\n\r\n " + logo + " | A: " + answer);
            eb.appendDescription("\r\n\r\nWinner: " + event.getAuthor().getAsMention());
            eb.appendDescription("\r\nPrize: **" + token + "** :coin: tokens.");
            MessageEmbed build = eb.build();

            TextChannel trivChannel = Main.getInstance().getGuild().getTextChannelById(config.getUnscrambleChannel());
            if (trivChannel != null) {
                trivChannel.sendMessageEmbeds(build).queue();
            }
            gameManager.setTrivia(null);
            user.gainTokens(token);
            user.saveUser();
        }
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
