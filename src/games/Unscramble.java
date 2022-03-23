package games;

import discord.User;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Unscramble {

    private String answer;
    private String scrambled;
    private int tokenPrize = 15;

    public void startUnscramble() {
        GameManager gameManager = Main.getInstance().getGameManager();
        Config config = Main.getInstance().getConfig();

        getRandomWord();
        EmbedBuilder eb = new EmbedBuilder();
        Member bot = Main.getInstance().getGuild().getSelfMember();

        Member prevWinner = null;
        String w = gameManager.getWinner();
        if (!w.isEmpty()) {
            prevWinner = Main.getInstance().getGuild().getMemberById(w);
        }
        eb.setAuthor("Xzybyte", bot.getAvatarUrl(), bot.getAvatarUrl());
        eb.setColor(Main.hex2Rgb("#a341f4"));
        eb.setDescription("__**Unscramble**__");
        eb.appendDescription("\r\n\r\nU: **" + scrambled +"**");
        eb.appendDescription("\r\n\r\nSubmit your answer by typing it in the chat.");
        eb.appendDescription("\r\n**Previous winner**: " + (prevWinner == null ? "" : prevWinner.getAsMention()));
        MessageEmbed build = eb.build();

        TextChannel unscChannel = Main.getInstance().getGuild().getTextChannelById(config.getUnscrambleChannel());
        if (unscChannel != null) {
            unscChannel.sendMessageEmbeds(build).queue();
        }
    }

    public void checkAnswer(String ans, MessageReceivedEvent event, User user) {
        GameManager gameManager = Main.getInstance().getGameManager();
        Config config = Main.getInstance().getConfig();
        if (ans.equalsIgnoreCase(answer)) {
            int token = tokenPrize;
            EmbedBuilder eb = new EmbedBuilder();
            Member bot = Main.getInstance().getGuild().getSelfMember();
            eb.setAuthor("Xzybyte", bot.getAvatarUrl(), bot.getAvatarUrl());
            eb.setColor(Main.hex2Rgb("#e5f442"));
            eb.setDescription("__**Unscramble**__");
            eb.appendDescription("\r\n\r\nA: " + answer);
            eb.appendDescription("\r\n\r\nWinner: " + event.getAuthor().getAsMention());
            eb.appendDescription("\r\nPrize: **" + token + "** :coin: tokens.");
            MessageEmbed build = eb.build();

            TextChannel unscChannel = Main.getInstance().getGuild().getTextChannelById(config.getUnscrambleChannel());
            if (unscChannel != null) {
                unscChannel.sendMessageEmbeds(build).queue();
            }
            gameManager.setUnscramble(null);
            user.gainTokens(token);
            user.saveUser();
        }
    }

    public String getAnswer() {
        return answer;
    }

    public void getRandomWord() {
        String word;
        Scanner scan = null;
        try {
            scan = new Scanner(new File("wordlist.txt"));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        if(scan != null) {
            while(scan.hasNextLine()) {
                list.add(scan.nextLine());
            }
        }
        Random rand = new Random();
        int index = rand.nextInt(list.size());
        word = list.get(index);
        answer = word;
        List<Character> scr = new ArrayList<>();
        for (int i = 0; i < answer.length(); i++) {
            scr.add(answer.charAt(i));
        }
        Collections.shuffle(scr);
        StringBuilder s = new StringBuilder();
        for (Character character : scr) {
            s.append(character);
        }
        System.out.println(answer);
        scrambled = s.toString();
        scan.close();
    }

}
