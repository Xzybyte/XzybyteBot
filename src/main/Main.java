package main;

import handlers.EventHandler;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class Main {

    private JDA jda;
    private JDABuilder builder;
    private String BOT_TOKEN = "NTc1MjIyNzk2NTI3ODYxNzYw.XNEz-Q.4a3WY3n4Bpx5IOyLCl6VSbTjYws";

    public static void main(String[] args) {
        Main main = new Main(); // Create a new instance
        main.startSession();
    }

    /*
     *    Creates the session to connect to discord.
     */
    public void startSession() {
        builder = JDABuilder.createDefault(BOT_TOKEN)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setAutoReconnect(true);
        EventHandler eventHandler = new EventHandler();
        builder.addEventListeners(eventHandler);
        eventHandler.registerHandlers();
        try {
            builder.build();
        } catch (IllegalArgumentException | LoginException e) {
            e.printStackTrace();
        }
    }

}
