package handlers;

import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {

    public void registerHandlers() {

    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is now online.");
    }
}
