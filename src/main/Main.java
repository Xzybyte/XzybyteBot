package main;

import commands.CommandProcessor;
import discord.UserStorage;
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

public class Main {

    private static Main instance = null;

    private JDA jda;
    private JDABuilder builder;
    private String BOT_TOKEN = "";

    private Config config;
    private CommandProcessor commandProcessor;
    private UserStorage userStorage;
    private MySQL mysql;

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

    public JDA getJDA() {
        return jda;
    }

    public Guild getGuild() {
        return jda.getGuilds().get(0);
    }
}
