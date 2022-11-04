package main;

import commands.CommandProcessor;
import discord.UserStorage;
import games.GameManager;
import handlers.EventHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import utils.Config;
import utils.MySQL;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class Main {

    private static Main instance = null;

    private JDA jda;
    private JDABuilder builder;
    private String BOT_TOKEN = "";

    private Config config;
    private CommandProcessor commandProcessor;
    private UserStorage userStorage;
    private MySQL mysql;
    private GameManager gameManager;

    public static void main(String[] args) {
        Main.getInstance().startSession();
        Main.getInstance().loadFeatures();
        Main.getInstance().loadCommands();
        Main.getInstance().initializeSQL();
    }

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }

    /*
        Creates the session to connect to discord.
     */
    public void startSession() {
        builder = JDABuilder.createDefault(BOT_TOKEN)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setAutoReconnect(true);
        EventHandler eventHandler = new EventHandler();
        builder.addEventListeners(eventHandler);
        try {
            jda = builder.build();
        } catch (IllegalArgumentException | LoginException e) {
            e.printStackTrace();
        }
    }

    /*
        Load custom built features
     */
    public void loadFeatures() {
        config = new Config();
        config.loadConfig();
        userStorage = new UserStorage();
        gameManager = new GameManager();
        //gameManager.startGame();
    }

    public void loadCommands() {
        commandProcessor = new CommandProcessor();
        commandProcessor.loadAll();
    }

    public void initializeSQL() {
        mysql = new MySQL(config.getUser(), config.getPassword(), config.getDatabase(), config.getHost(), config.getPort());
        mysql.connectToDb();
    }

    public Config getConfig() {
        return config;
    }

    public CommandProcessor getCommandProcessor() {
        return commandProcessor;
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return jda.getGuilds().get(0);
    }

    public static Color hex2Rgb(String colorStr) {
        return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16), Integer.valueOf(colorStr.substring(5, 7), 16));
    }
}
