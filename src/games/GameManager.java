package games;

import discord.User;
import main.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Config;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager {

    private Trivia trivia = null;
    private Unscramble unscramble = null;
    private String winnerId = "";

    public void startGame() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runGame();
            }
        }, 10000, 10000);
    }

    public void runGame() {
        Config config = Main.getInstance().getConfig();
        Random random = new Random();
        int index = random.nextInt(2);
        System.out.println(index);
        if (index == 0) {
            if (config.getTrivia()) {
                trivia = new Trivia();
                trivia.startTrivia();
            }
        } else {
            if (config.getUnscramble()) {
                unscramble = new Unscramble();
                unscramble.startUnscramble();
            }
        }
    }

    public void setTrivia(Trivia trivia) {
        this.trivia = trivia;
    }

    public Trivia getTrivia() {
        return trivia;
    }

    public void setUnscramble(Unscramble unsc) {
        this.unscramble = unsc;
    }

    public Unscramble getUnscramble() {
        return unscramble;
    }

    public void setWinner(String winner) {
        this.winnerId = winner;
    }

    public String getWinner() {
        return winnerId;
    }

    public void processText(String text, MessageReceivedEvent event, User user) {
        if (text.isEmpty()) {
            return;
        }
        if (getUnscramble() != null) {
            getUnscramble().checkAnswer(text, event, user);
        } else if (getTrivia() != null) {
            getTrivia().checkAnswer(text, event, user);
        }
    }

}
